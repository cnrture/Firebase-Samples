package com.canerture.firebaseexamples.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val documentId: String? = null,
    val todo: String? = null,
    val priority: String? = null,
    val isDone: Boolean = false,
    val date: String? = null
) : Parcelable