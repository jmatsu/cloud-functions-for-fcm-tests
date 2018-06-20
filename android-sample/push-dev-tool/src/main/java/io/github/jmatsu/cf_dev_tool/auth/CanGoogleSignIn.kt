package io.github.jmatsu.cf_dev_tool.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.github.jmatsu.cf_dev_tool.R
import kotlin.coroutines.experimental.suspendCoroutine

private const val GOOGLE_SIGNIN_REQ_CODE = 100

private val DOMAIN_NAME: String? = "gmail.com" // "your gsuite domain name"

interface CanGoogleSignIn {
    fun requireActivity(): Activity

    fun openGoogleSign() {
        with(requireActivity()) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
                requestIdToken(getString(R.string.default_web_client_id))
                requestEmail()
                DOMAIN_NAME?.also { setHostedDomain(it) }
            }.build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)

            startActivityForResult(googleSignInClient.signInIntent, GOOGLE_SIGNIN_REQ_CODE)
        }
    }

    suspend fun handleOnGoogleSignInActivityResult(requestCode: Int, resultCode: Int, data: Intent?): String = suspendCoroutine { routine ->
        with(requireActivity()) {
            if (requestCode == GOOGLE_SIGNIN_REQ_CODE) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)

                    if (DOMAIN_NAME != null && account.email?.endsWith(DOMAIN_NAME) == false) {
                        routine.resumeWithException(IllegalStateException("filtered by host-domain check"))
                    } else {
                        routine.resume(account.idToken ?: TODO(":shrug:"))
                    }
                } catch (e: ApiException) {
                    routine.resumeWithException(e)
                }
            } else {
                TODO("here is a dead flow in this app")
            }
        }
    }
}