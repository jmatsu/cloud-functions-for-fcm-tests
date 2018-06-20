package io.github.jmatsu.cf_dev_tool

import android.content.Context
import android.util.Log
import android.widget.Toast

const val TAG = "CF_DEV_TOOL"

fun Context.showMessage(msg: String, th: Throwable? = null) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    if (th != null) {
        Log.e(TAG, msg, th)
    } else {
        Log.i(TAG, msg)
    }
}
