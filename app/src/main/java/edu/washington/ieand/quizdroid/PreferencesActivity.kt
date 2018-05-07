package edu.washington.ieand.quizdroid


import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        toolbar.title = getString(R.string.title_activity_preferences)
        setSupportActionBar(toolbar)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val savedUrlPref = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_url), "http://tednewardsandbox.site44.com/questions.json")
        val savedRefreshPref = sharedPref.getString(getString(R.string.sharedPrefs_storedUserPrefs_refresh), "5")
        val givenUrlView = findViewById<EditText>(R.id.textEdit_preferences_urlInput)
        givenUrlView.setText(savedUrlPref)
        val givenRefreshTimeView = findViewById<EditText>(R.id.textEdit_preferences_timeInput)
        givenRefreshTimeView.setText(savedRefreshPref)


        val saveBtn: Button = findViewById(R.id.button_preferences_savePreferences)
        saveBtn.setOnClickListener {
            val givenUrl: String = findViewById<EditText>(R.id.textEdit_preferences_urlInput).text.toString()
            val givenRefreshTime: String = findViewById<EditText>(R.id.textEdit_preferences_timeInput).text.toString()
            var checksHavePassed = true

            if (!URLUtil.isValidUrl(givenUrl) || !URLUtil.isNetworkUrl(givenUrl) || !givenUrl.endsWith(".json")) {
                val toast = Toast.makeText(this, "Error: Invalid URL. Make sure it's the absolute URL to a properly formatted .JSON file for QuizDroid", Toast.LENGTH_SHORT)
                toast.show()
                checksHavePassed = false
            }

            if (givenRefreshTime.toInt() < 5) {
                val toast = Toast.makeText(this, "Error: Please give at least 5 minutes between refreshes.", Toast.LENGTH_SHORT)
                toast.show()
            }

            if (checksHavePassed) {
                try {
                    with(sharedPref.edit()) {
                        putString(getString(R.string.sharedPrefs_storedUserPrefs_url), givenUrl)
                        putString(getString(R.string.sharedPrefs_storedUserPrefs_refresh), givenRefreshTime)
                        commit()
                    }

                    val intent = Intent(applicationContext, MainV2Activity::class.java)
                    startActivity(intent)
                }
                catch(e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    companion object {
        const val TAG = "PreferencesActivity"
    }
}
