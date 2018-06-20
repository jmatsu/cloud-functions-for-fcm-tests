package io.github.jmatsu.cf_dev_tool.request

data class PushNotificationSeed(
        val deviceToken: String,
        val data: Map<String, Any>? = null,
        val notification: Map<String, Any>? = null
)