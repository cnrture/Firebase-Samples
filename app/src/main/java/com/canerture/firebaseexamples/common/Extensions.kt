package com.canerture.firebaseexamples.common

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnack(text: String) = Snackbar.make(this, text, 1000).show()