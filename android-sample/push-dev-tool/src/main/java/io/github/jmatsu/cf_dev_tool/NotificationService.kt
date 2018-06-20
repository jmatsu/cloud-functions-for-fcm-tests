package io.github.jmatsu.cf_dev_tool

import io.github.jmatsu.cf_dev_tool.request.BearerToken
import io.github.jmatsu.cf_dev_tool.request.PushNotificationSeed
import io.github.jmatsu.cf_dev_tool.response.NotificationResult
import retrofit2.Call
import retrofit2.http.*

interface NotificationService {

    @POST("api/messaging/{type}")
    @Headers("Content-Type: application/json; charset=utf-8")
    operator fun invoke(
            @Header("Authorization") token: BearerToken,
            @Path("type") type: Type,
            @Body seed: PushNotificationSeed
    ): Call<NotificationResult>
}

enum class Type {
    FromRequest,
    SampleData;

    override fun toString(): String = name.decapitalize()
}