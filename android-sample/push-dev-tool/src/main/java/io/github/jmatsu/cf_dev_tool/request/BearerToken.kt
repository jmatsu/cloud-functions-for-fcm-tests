package io.github.jmatsu.cf_dev_tool.request

data class BearerToken(
        val token: String
) {
    override fun toString(): String = "Bearer $token"
}