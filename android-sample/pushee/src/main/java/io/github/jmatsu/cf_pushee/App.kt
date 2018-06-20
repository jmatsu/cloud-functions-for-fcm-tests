package io.github.jmatsu.cf_pushee

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

class App : Application() {
    private val broadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == BuildConfig.DEV_TOOL_PERMISSION) {
                    Log.d(TAG, "received a dev tool action")

                    when (intent.getStringExtra(BuildConfig.DEV_TOOL_KEY_action)) {
                        BuildConfig.DEV_TOOL_ACTION_requestDeviceToken -> {
                            val token = FirebaseInstanceId.getInstance().token

                            Log.d(TAG, token)
                            sendBroadcast(Intent(BuildConfig.DEV_TOOL_ACTION_requestDeviceToken).putExtra(BuildConfig.DEV_TOOL_KEY_token, token), BuildConfig.DEV_TOOL_PERMISSION)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate")

        registerReceiver(broadcastReceiver, IntentFilter(BuildConfig.DEV_TOOL_PERMISSION))
    }
}