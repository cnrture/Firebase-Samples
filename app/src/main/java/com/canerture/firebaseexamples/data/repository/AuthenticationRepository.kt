package com.canerture.firebaseexamples.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class AuthenticationRepository(private val firebaseAuth: FirebaseAuth) {

    val result = MutableLiveData<FirebaseUser?>()

    val errorMessage = MutableLiveData<String>()

    fun signUpWithEmailPassword(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    result.value = it
                }
            }.addOnFailureListener {
                errorMessage.value = it.message.toString()
            }
    }

    fun signInWithEmailPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    result.value = it
                }
            }.addOnFailureListener {
                errorMessage.value = it.message.toString()
            }
    }

    fun verifyCode(verificationId: String, verifyCode: String) {
        verificationId.let {
            val credential = PhoneAuthProvider.getCredential(it, verifyCode)
            signInWithCredential(credential)
        }
    }

    fun signInWithCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener { authResult ->
            authResult.user?.let {
                result.value = it
            }
        }.addOnFailureListener {
            errorMessage.value = it.message.toString()
        }
    }

    fun signInAnonymously() {
        firebaseAuth.signInAnonymously().addOnSuccessListener { authResult ->
            authResult.user?.let {
                result.value = it
            }
        }.addOnFailureListener {
            errorMessage.value = it.message.toString()
        }
    }
}