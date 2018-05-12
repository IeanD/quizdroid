package edu.washington.ieand.quizdroid

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class JsonDownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(MainV2Activity.TAG, "Broadcast received")
        when (intent?.action) {
            DOWNLOAD_JSON -> {
                val replyIntent = intent!!.getParcelableExtra<PendingIntent>(DownloadIntentService.PENDING_RESULT_EXTRA)
                var newIntent = Intent(context, DownloadIntentService::class.java)
                newIntent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, replyIntent)
                newIntent.putExtra(DownloadIntentService.URL_EXTRA, intent.extras[URL_EXTRA] as String)
                context?.startService(newIntent)
            }
        }
    }

    companion object {
        const val TAG = "JsonDownloadReceiver"
        const val DOWNLOAD_JSON = "edu.washington.ieand.quizdroid.DOWNLOAD_JSON_FROM_URL"
        const val URL_EXTRA = "url"
        const val DOWNLOAD_FAILURE = "edu.washington.ieand.quizdroid.DOWNLOAD_JSON_FAILED"
    }
}