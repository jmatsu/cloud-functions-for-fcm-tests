package io.github.jmatsu.cf_pushee

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {
    private val cbManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
    private val clipListener = ClipboardManager.OnPrimaryClipChangedListener {
        Toast.makeText(this, "A clipboard was updated", Toast.LENGTH_SHORT).show()
    }

    private val broadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == ACTION_RECEIVED_DATA_MESSAGE) {
                    val data = intent.getBundleExtra(KEY_DATA)

                    runOnUiThread { textView.text = data.toString() }
                }
            }
        }
    }

    private val textView by lazy { findViewById<TextView>(R.id.message) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun createChannels() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(NotificationChannel("data", "data notification", NotificationManager.IMPORTANCE_DEFAULT))
            manager.createNotificationChannel(NotificationChannel("zero-data", "zero-data notification", NotificationManager.IMPORTANCE_DEFAULT))
        }

        createChannels()

        findViewById<View>(R.id.copyButton).setOnClickListener {
            val token = FirebaseInstanceId.getInstance().token

            cbManager.primaryClip = ClipData.newPlainText("a token", token)
        }
    }

    override fun onResume() {
        super.onResume()

        cbManager.addPrimaryClipChangedListener(clipListener)
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_RECEIVED_DATA_MESSAGE))
    }

    override fun onPause() {
        unregisterReceiver(broadcastReceiver)
        cbManager.removePrimaryClipChangedListener(clipListener)

        super.onPause()
    }
}
