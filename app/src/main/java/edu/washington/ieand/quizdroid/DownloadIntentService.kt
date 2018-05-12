package edu.washington.ieand.quizdroid

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.util.Log

import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

class DownloadIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {
        val reply = intent!!.getParcelableExtra<PendingIntent>(PENDING_RESULT_EXTRA)
        try {
            try {
                val url = URL(intent?.getStringExtra(URL_EXTRA)).readText()
                Log.i(TAG, url)
                val result = Intent()
                result.putExtra(URL_RESULT_EXTRA, url)
//
                reply.send(this, RESULT_CODE, result)
            } catch (exc: MalformedURLException) {
                reply.send(INVALID_URL_CODE)
                Log.i(TAG, "DIT.kt, ln26: " + exc.toString())

            } catch (exc: Exception) {
                reply.send(ERROR_CODE)
                Log.i(TAG, "DIT.kt, ln 30: " + exc.toString())
            }

        } catch (exc: PendingIntent.CanceledException) {
            Log.i(TAG, "reply cancelled", exc)
        }
    }

    companion object {

        private val TAG = "DownloadIntentService:"

        const val PENDING_RESULT_EXTRA = "pending_result"
        const val URL_EXTRA = "url"
        const val URL_RESULT_EXTRA = "url"

        const val RESULT_CODE = 0
        const val INVALID_URL_CODE = 1
        const val ERROR_CODE = 2
    }
}