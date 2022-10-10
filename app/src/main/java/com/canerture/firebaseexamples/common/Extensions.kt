package com.canerture.firebaseexamples.common

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.Constants.NOTIFICATION_ID
import com.canerture.firebaseexamples.presentation.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun showLogDebug(tag: String, msg: String) = Log.d(tag, msg)

fun View.showSnack(text: String) = Snackbar.make(this, text, 1000).show()

fun Dialog.showSnack(text: String) = window?.decorView?.showSnack(text)

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
    selectedPriority: (String) -> Unit
) {
    rbOne.check(rbTwo, rbThree) { selectedPriority(it) }
    rbTwo.check(rbOne, rbThree) { selectedPriority(it) }
    rbThree.check(rbOne, rbTwo) { selectedPriority(it) }
}

fun RadioButton.check(
    otherRbOne: RadioButton,
    otherRbTwo: RadioButton,
    selectedPriority: (String) -> Unit
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

fun Fragment.resultLauncher(onSuccess: (Bitmap) -> Unit = {}, onFailure: (String) -> Unit = {}) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            val resultData = result.data

            if (resultData != null && resultData.data != null) {

                val selectedImageUri = resultData.data

                try {
                    val selectedImageBitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            selectedImageUri
                        )
                    } else {
                        selectedImageUri?.let {
                            val source =
                                ImageDecoder.createSource(requireActivity().contentResolver, it)
                            ImageDecoder.decodeBitmap(source)
                        }
                    }

                    if (selectedImageBitmap != null) {
                        onSuccess(selectedImageBitmap)
                    } else {
                        onFailure("Something went wrong!")
                    }
                } catch (e: Exception) {
                    onFailure(e.message.orEmpty())
                }
            }
        }
    }

fun showNotification(context: Context, title: String, description: String) {

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        Intent(context, MainActivity::class.java),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) PendingIntent.FLAG_IMMUTABLE else 0
    )

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            context.getString(R.string.notification_channel_id),
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_check)
        .setContentTitle(title)
        .setContentText(description)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    notificationManager.notify(NOTIFICATION_ID, builder.build())
}