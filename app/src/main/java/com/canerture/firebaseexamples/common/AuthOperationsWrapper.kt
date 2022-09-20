package com.canerture.firebaseexamples.common

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthOperationsWrapper @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    private var verificationId: String? = null

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    onSuccess()
                }
            }.addOnFailureListener {
                onFailure(it.message.orEmpty())
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    onSuccess()
                }
            }.addOnFailureListener {
                onFailure(it.message.orEmpty())
            }
    }

    fun sendVerificationCode(
        phoneNumber: String,
        onCodeSent: () -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential, {
                        onSuccess()
                    }, {
                        onFailure(it)
                    })
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (e is FirebaseAuthInvalidCredentialsException) {

                        onFailure("Invalid Request!")
                    } else if (e is FirebaseTooManyRequestsException) {
                        onFailure("Too many request!")
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent()
                    this@AuthOperationsWrapper.verificationId = verificationId
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(
        verifyCode: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        verificationId?.let { verifId ->
            val credential = PhoneAuthProvider.getCredential(verifId, verifyCode)
            signInWithCredential(credential, {
                onSuccess()
            }, {
                onFailure(it)
            })
        }
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener { authResult ->
            authResult.user?.let {
                onSuccess()
            }
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun signInAnonymously(
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        firebaseAuth.signInAnonymously().addOnSuccessListener { authResult ->
            authResult.user?.let {
                onSuccess()
            }
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun checkCurrentUser(currentUser: (FirebaseUser) -> Unit = {}) {
        firebaseAuth.currentUser?.let {
            currentUser(it)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}