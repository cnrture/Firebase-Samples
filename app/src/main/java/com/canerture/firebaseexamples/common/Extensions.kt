package com.canerture.firebaseexamples.common

import android.view.View
import android.widget.RadioButton
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

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun RadioButton.setCheckedTrue() {
    isChecked = true
}

fun RadioButton.setCheckedFalse() {
    isChecked = false
}

fun radioButtonCheckedListener(
    rbOne: RadioButton,
    rbTwo: RadioButton,
    rbThree: RadioButton,
    selectedPriority: (String) -> Unit = {}
) {
    rbOne.checked(rbTwo, rbThree) { selectedPriority(it) }
    rbTwo.checked(rbOne, rbThree) { selectedPriority(it) }
    rbThree.checked(rbOne, rbTwo) { selectedPriority(it) }
}

fun RadioButton.checked(
    otherRbOne: RadioButton,
    otherRbTwo: RadioButton,
    selectedPriority: (String) -> Unit = {}
) {

    setOnClickListener {

        when (it.id) {
            this.id -> {
                otherRbOne.setCheckedFalse()
                otherRbTwo.setCheckedFalse()
                selectedPriority(this.text.toString())
            }
            this.id -> {
                otherRbOne.setCheckedFalse()
                otherRbTwo.setCheckedFalse()
                selectedPriority(this.text.toString())
            }
            this.id -> {
                otherRbOne.setCheckedFalse()
                otherRbTwo.setCheckedFalse()
                selectedPriority(this.text.toString())
            }
        }
    }
}