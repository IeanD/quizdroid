package edu.washington.ieand.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v7.app.AlertDialog
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
import java.io.FileWriter
import java.lang.reflect.Type


class MainV2Activity : AppCompatActivity() {

    private val instance = QuizApp.getSingletonInstance()
    private val receiver = JsonDownloadReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_v2)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        // Register BroadcastReceiver
        val filter = IntentFilter()
        filter.addAction(JsonDownloadReceiver.DOWNLOAD_JSON)
        registerReceiver(receiver, filter)

        // Variables
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

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

        val nextUrl = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_url), "ERROR")
        val nextTimeout = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_refresh), "5")

        Toast.makeText(this, "The quiz data will be refreshed from $nextUrl in $nextTimeout minutes!", Toast.LENGTH_SHORT).show()

        beginNetworkCheck()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == URL_DOWNLOAD_REQUEST_CODE) {
            when (resultCode) {
                DownloadIntentService.INVALID_URL_CODE -> {
                    Log.i(TAG, "Fail, invalid URL.")
                    Toast.makeText(this, "Uh oh! The quiz data download failed! Go to settings & check the URL.", Toast.LENGTH_SHORT).show()
                }
                DownloadIntentService.ERROR_CODE -> {
                    Log.i(TAG, "Fail, error code.")
                    Toast.makeText(this, "Uh oh! The quiz data download failed! Go to settings & check the URL.", Toast.LENGTH_SHORT).show()
                }
                DownloadIntentService.RESULT_CODE -> {
                    Log.i(TAG, "Download received")
                    saveJsonToSdCard(data.extras[DownloadIntentService.URL_RESULT_EXTRA].toString())
                }
            }
        }

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

    override fun onResume() {
        Log.i(TAG, "Activity resumed")

        super.onResume()
    }

    override fun onStop() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(PendingIntent.getBroadcast(applicationContext, 0,
                Intent(JsonDownloadReceiver.DOWNLOAD_JSON), PendingIntent.FLAG_UPDATE_CURRENT))
        unregisterReceiver(receiver)
        super.onStop()
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

    private fun airplaneModeIsOn(): Boolean {
        return Settings.Global.getInt(this.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    private fun beginNetworkCheck() {
        // Variables
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        // Check if there's an internet connection.
        if (networkIsAvailable()) { // IF INTERNET AVAILABLE
            val jsonUrl = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_url), "DEFAULT")
            val timeout = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_refresh), "5").toInt()
            downloadJson(jsonUrl, timeout)
            tryToBuildUi()
        }
        else {
            if (airplaneModeIsOn()) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("It looks like Airplane Mode is on! Would you like to go to Settings?")
                        .setTitle("Uh oh!")
                        .setNegativeButton("No",
                                { _, _ ->
                                    tryToBuildUi()
                                })
                        .setPositiveButton("Yes",
                                { _, _ ->
                                    val intent = Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                                    startActivity(intent)
                                })

                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun buildQuizzesFromJson() {
        val extStorage = Environment.getExternalStorageDirectory().absolutePath
        val jsonFile = File("$extStorage/Android/data/edu.washington.ieand.quizdroid/questions.json")
        val result = jsonFile.readText()
        addDataToRepo(result)
        updateListView()
    }

    private fun downloadJson(url: String, timeout: Int) {
        Log.i(TAG, timeout.toString())

        var timeToTrigger = timeout.toLong() * 60 * 1000
        if (jsonIsDownloaded()) {
            timeToTrigger += SystemClock.elapsedRealtime()
        }
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingResult = createPendingResult(
                MainV2Activity.URL_DOWNLOAD_REQUEST_CODE, Intent(), 0)
        val intent = Intent(JsonDownloadReceiver.DOWNLOAD_JSON)
                .putExtra(JsonDownloadReceiver.URL_EXTRA, url)
                .putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult)
        val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                timeToTrigger,
                (timeout.toLong() * 60 * 1000),
                pendingIntent
        )
    }

    private fun jsonIsDownloaded(): Boolean {
        val extStorage = Environment.getExternalStorageDirectory().absolutePath
        val result = File("$extStorage/Android/data/edu.washington.ieand.quizdroid/questions.json")
        return result.exists()
    }

    private fun networkIsAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun saveJsonToSdCard(json: String) {
        try {
            val extStorage = Environment.getExternalStorageDirectory().absolutePath
            val dataFolder = File("$extStorage/Android/data/edu.washington.ieand.quizdroid")
            var firstRun = false
            if (!dataFolder.exists()) {
                dataFolder.mkdir()
                firstRun = true
            }
            var questionsDotJson = File("$extStorage/Android/data/edu.washington.ieand.quizdroid/questions.json")
            if (questionsDotJson.exists()) {
                questionsDotJson.copyTo(File(dataFolder.path + "questionsBackup.json"), true)
                questionsDotJson.delete()
            }
            questionsDotJson = File(dataFolder, "questions.json")
            val writer = FileWriter(questionsDotJson)
            writer.append(json)
            writer.flush()
            writer.close()
            Toast.makeText(applicationContext, "Quiz data saved!", Toast.LENGTH_SHORT).show()
            if (firstRun || this.hasWindowFocus()) {
                tryToBuildUi()
            }
        }
        catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun tryToBuildUi() {
        if (!jsonIsDownloaded() && !networkIsAvailable()) {
            Toast.makeText(this,
                    "Oh no! Looks like there's no internet connection, and no stored quizzes! Please try again later with an internet connection.",
                    Toast.LENGTH_SHORT)
                    .show()
        } else {
            if (!networkIsAvailable()) {
                Toast.makeText(this,
                        "You've got no internet connection, but QuizDroid found some cached quizzes. Have fun!",
                        Toast.LENGTH_SHORT)
                        .show()
            }

            buildQuizzesFromJson()
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
