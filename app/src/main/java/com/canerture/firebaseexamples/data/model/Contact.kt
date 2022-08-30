package com.canerture.firebaseexamples.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val documentId: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null
) : Parcelable