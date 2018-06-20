package io.github.jmatsu.cf_dev_tool.auth

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.experimental.suspendCoroutine

interface CanFirebaseAuth {
    fun requireContext(): Context

    suspend fun authorizeFirebase(idToken: String): Unit = suspendCoroutine { routine ->
        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        routine.resume(Unit)
                    } else {
                        routine.resumeWithException(it.exception!!)
                    }
                }
    }

    suspend fun getToken(): String? = suspendCoroutine { routine ->
        val user = FirebaseAuth.getInstance().currentUser ?: let {
            routine.resume(null)
            return@suspendCoroutine
        }

        user.getIdToken(true)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val token = it.result.token ?: TODO(":shrug:")
                        routine.resume(token)
                    } else {
                        routine.resumeWithException(it.exception!!)
                    }
                }
    }
}
