package com.canerture.firebaseexamples.common

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.showSnack(text: String) = Snackbar.make(this, text, 1000).show()

fun getDateTimeAsFormattedString(): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("tr"))
    val date = Date()
    return dateFormat.format(date).toString()
}