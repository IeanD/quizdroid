package edu.washington.ieand.quizdroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main_v2.*
import java.io.File
import java.lang.reflect.Type
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.net.NetworkInfo
import android.net.ConnectivityManager


class MainV2Activity : AppCompatActivity() {

    private val instance = QuizApp.getSingletonInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_v2)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        // Variables
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val topicDataStored = sharedPref.contains(getString(R.string.sharedPrefs_storedTopicData))
        val emptyTopicDataString = TopicData("","", listOf(QuestionData("", -1, arrayOf("","","","")))).toString()

        // Check if user preferences have been initialized.
        val userPrefsStored = sharedPref.contains(getString(R.string.sharedPrefs_storedUserPrefs_url))
                && sharedPref.contains(getString(R.string.sharedPrefs_storedUserPrefs_refresh))

        // If no stored user preferences were found, set them to the default.
        if (!userPrefsStored) {
            try {
                with(sharedPref.edit()) {
                    putString(getString(R.string.sharedPrefs_storedUserPrefs_url), "http://tednewardsandbox.site44.com/questions.json")
                    putString(getString(R.string.sharedPrefs_storedUserPrefs_refresh), "5")
                    commit()
                }
            }
            catch(e: Exception) {
                Log.e(TopicRepository.TAG, e.toString())
            }
        }

        // Check if there's an internet connection.
        if (checkNetworkAvailability()) { // IF INTERNET AVAILABLE
            val jsonUrl = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_url), "DEFAULT")
            downloadJson(jsonUrl)
        }
        else { // Otherwise, operate only from the local storage.
            if (!topicDataStored) {
                fallbackToLocalJson()
            }
            addDataToRepo(sharedPref.getString(getString(R.string.sharedPrefs_storedTopicData), emptyTopicDataString))
            updateListView()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        if (requestCode == URL_DOWNLOAD_REQUEST_CODE) {
            when (resultCode) {
                DownloadIntentService.INVALID_URL_CODE -> {
                    Log.i(TAG, "Fail, invalid URL.")
                    fallbackToLocalJson()
                }
                DownloadIntentService.ERROR_CODE -> {
                    Log.i(TAG, "Fail, error code.")
                    fallbackToLocalJson()
                }
                DownloadIntentService.RESULT_CODE -> {
                    with(sharedPref.edit()) {
                        putString(getString(R.string.sharedPrefs_storedTopicData), data.extras[DownloadIntentService.URL_RESULT_EXTRA].toString())
                        commit()
                    }
                }
            }
        } else {
            fallbackToLocalJson()
        }

        addDataToRepo(sharedPref.getString(getString(R.string.sharedPrefs_storedTopicData), "DEFAULT"))
        updateListView()

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_action_settings -> {
            val intent = Intent(applicationContext, PreferencesActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
            false
        }
    }

    private fun addDataToRepo(json: String) {
        try {
            val collectionType: Type = object : TypeToken<Collection<TopicData>>() { }.type
            val incomingTopics: Collection<TopicData> = Gson().fromJson(json, collectionType)

            instance.repository.buildTopics(incomingTopics)
        }
        catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun checkNetworkAvailability(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun downloadJson(url: String) {
        val pendingResult = createPendingResult(
                URL_DOWNLOAD_REQUEST_CODE, Intent(), 0)
        val intent = Intent(applicationContext, DownloadIntentService::class.java)
        intent.putExtra(DownloadIntentService.URL_EXTRA, url)
        intent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult)
        startService(intent)
    }

    private fun fallbackToLocalJson() {
        try {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val extStorage = Environment.getExternalStorageDirectory().absolutePath
            val result = File("$extStorage/Android/data/edu.washington.ieand.quizdroid/questions.json").readText()

            with(sharedPref.edit()) {
                putString(getString(R.string.sharedPrefs_storedTopicData), result)
                commit()
            }
        }
        catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun updateListView() {
        val listOfTopics = instance.repository.getTopics()
        val listOfTopicNames = listOfTopics.map { it ->
            it.title
        }
        val listOfTopicNamesToDescriptions = listOfTopics.map { it ->
            arrayOf(it.title, it.desc)
        }
        val list: List<HashMap<String, String>> = listOfTopicNamesToDescriptions.map { it ->
            val item: HashMap<String, String> = HashMap()
            item["line1"] = it[0] as String
            item["line2"] = it[1] as String

            item
        }

        val lv: ListView = findViewById(R.id.listView_topics)
        val simpleAdapter = SimpleAdapter(this,
                list, R.layout.two_line_list_item, arrayOf("line1", "line2"),
                intArrayOf(R.id.line_a, R.id.line_b))
        lv.adapter = simpleAdapter

        lv.setOnItemClickListener { _, _, position, _ ->
            val item = listOfTopicNames[position]

            val intent = Intent(this, ControllerActivity::class.java)
                    .putExtra(ControllerActivity.TOPIC_NAME, item)

            startActivity(intent)
        }
    }

    companion object {
        const val TAG: String = "MainActivity"
        const val URL_DOWNLOAD_REQUEST_CODE = 42
    }
}
