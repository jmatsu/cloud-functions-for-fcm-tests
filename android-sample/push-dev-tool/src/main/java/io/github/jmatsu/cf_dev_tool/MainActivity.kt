package io.github.jmatsu.cf_dev_tool

import android.app.Activity
import android.app.ListActivity
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import io.github.jmatsu.cf_dev_tool.auth.CanFirebaseAuth
import io.github.jmatsu.cf_dev_tool.auth.CanGoogleSignIn
import io.github.jmatsu.cf_dev_tool.request.BearerToken
import io.github.jmatsu.cf_dev_tool.request.PushNotificationSeed
import io.github.jmatsu.cf_dev_tool.response.NotificationResult
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import ru.gildor.coroutines.retrofit.await

class MainActivity : ListActivity(), CanGoogleSignIn, CanFirebaseAuth, HasNotificationService {

    private enum class Notification(val type: Type, val displayText: String) {
        RequestBasedData(Type.FromRequest, "request based data"),
        RequestBasedNotification(Type.FromRequest, "request based notification"),
        SampleData(Type.SampleData, "sample data");

        override fun toString(): String = displayText
    }

    private val notificationService: NotificationService by bindNotificationService()

    private val deviceToken: String?
        get() = findViewById<EditText>(R.id.deviceTokenEdit).editableText.toString().takeIf { it.isNotBlank() }

    private val cbManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    private val clipListener = ClipboardManager.OnPrimaryClipChangedListener {
        Log.d(TAG, "clip manager was changed!")

        if (cbManager.primaryClip.description.label == BuildConfig.DEV_TOOL_ACTION_requestDeviceToken) {
            findViewById<EditText>(R.id.deviceTokenEdit).setText(cbManager.primaryClip.getItemAt(0).text)
        }
    }

    private val broadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == BuildConfig.DEV_TOOL_ACTION_requestDeviceToken) {
                    val token = intent.getStringExtra(BuildConfig.DEV_TOOL_KEY_token)

                    findViewById<EditText>(R.id.deviceTokenEdit).setText(token)
                }
            }
        }
    }

    override fun requireActivity(): Activity = this

    override fun requireContext(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listAdapter = ArrayAdapter<Notification>(this, android.R.layout.simple_list_item_1, Notification.values())

        findViewById<View>(R.id.grabDeviceTokenButton).setOnClickListener {
            val intent = Intent(BuildConfig.DEV_TOOL_PERMISSION).apply {
                putExtra(BuildConfig.DEV_TOOL_KEY_action, BuildConfig.DEV_TOOL_ACTION_requestDeviceToken)
            }

            Log.d(TAG, "requested a device token")
            sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(broadcastReceiver, IntentFilter(BuildConfig.DEV_TOOL_ACTION_requestDeviceToken))
    }

    override fun onPause() {
        unregisterReceiver(broadcastReceiver)

        super.onPause()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        triggerPush { authToken, deviceToken ->
            val notification = Notification.values()[position]

            val seed = PushNotificationSeed(deviceToken).let {
                when (notification) {
                    Notification.RequestBasedData -> it.copy(data = hashMapOf("hi" to "this was built via your request"))
                    Notification.RequestBasedNotification -> it.copy(notification = hashMapOf("title" to "this is title", "body" to "some text body"))
                    Notification.SampleData -> it
                }
            }

            notificationService(BearerToken(authToken), notification.type, seed)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        launch(UI) {
            try {
                val idToken = handleOnGoogleSignInActivityResult(requestCode, resultCode, data)

                try {
                    authorizeFirebase(idToken)
                    showMessage("Firebase Auth succeeded")
                } catch (th: Throwable) {
                    showMessage("Firebase Auth failed", th)
                }
            } catch (th: Throwable) {
                showMessage("Google sign in failed", th)
                return@launch
            }
        }
    }

    private fun triggerPush(request: (authToken: String, deviceToken: String) -> Call<NotificationResult>) {
        val deviceToken = deviceToken ?: return

        launch(UI) {
            try {
                val token = getToken()

                if (token != null) {
                    val result = request(token, deviceToken).await()

                    showMessage(result.message)
                } else {
                    showMessage("Please sign-in and re-click")

                    openGoogleSign()
                }
            } catch (th: Throwable) {
                showMessage("Failed", th)
            }
        }
    }
}