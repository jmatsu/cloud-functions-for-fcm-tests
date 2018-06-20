package io.github.jmatsu.cf_pushee

import com.google.firebase.iid.FirebaseInstanceIdService

class RefreshedTokenListenerService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // no-op
    }
}