package ru.olegkravtsov.lab20

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_RESET_COLOR = "ru.olegkravtsov.lab20.RESET_COLOR"
        const val ACTION_SET_COLOR = "ru.olegkravtsov.lab20.SET_COLOR"
        const val KEY_TEXT_REPLY = "key_text_reply"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_RESET_COLOR -> {
                val mainIntent = Intent(context, MainActivity::class.java).apply {
                    putExtra("action", "reset")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(mainIntent)
            }
            ACTION_SET_COLOR -> {
                val remoteInput = RemoteInput.getResultsFromIntent(intent)
                val colorText = remoteInput?.getCharSequence(KEY_TEXT_REPLY)?.toString()

                if (!colorText.isNullOrEmpty()) {
                    val mainIntent = Intent(context, MainActivity::class.java).apply {
                        putExtra("action", "set_color")
                        putExtra("color", colorText)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(mainIntent)
                }
            }
        }
    }
}