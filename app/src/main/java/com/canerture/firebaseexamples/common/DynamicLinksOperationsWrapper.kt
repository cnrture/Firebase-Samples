package com.canerture.firebaseexamples.common

import android.content.Intent
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class DynamicLinksOperationsWrapper {

    fun subscribeDynamicLinks(
        intent: Intent,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                val lastPathSegment = pendingDynamicLinkData?.link?.lastPathSegment

                if (lastPathSegment != null) {
                    onSuccess(lastPathSegment)
                } else {
                    onFailure()
                }
            }.addOnFailureListener { e ->
                onFailure()
                showLogDebug(TAG, "getDynamicLink:onFailure: $e")
            }
    }

    companion object {
        private const val TAG = "DynamicLinksOperationsWrapper"
    }
}