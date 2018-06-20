package io.github.jmatsu.cf_dev_tool.response

import com.google.gson.JsonElement

data class NotificationResult(
        val success: Boolean,
        val message: String,
        val reason: JsonElement?
)