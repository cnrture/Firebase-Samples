package com.canerture.firebaseexamples.presentation.auth.additionalproviders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.canerture.firebaseexamples.data.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdditionalProvidersViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    var result: MutableLiveData<FirebaseUser?> = authenticationRepository.result

    var errorMessage: MutableLiveData<String> = authenticationRepository.errorMessage

    fun signUpWithEmailPassword(email: String, password: String) {
        authenticationRepository.signUpWithEmailPassword(email, password)
    }

    fun signInWithEmailPassword(email: String, password: String) {
        authenticationRepository.signInWithEmailPassword(email, password)
    }

    fun verifyCode(verificationId: String, verifyCode: String) {
        authenticationRepository.verifyCode(verificationId, verifyCode)
    }

    fun signInWithCredential(credential: PhoneAuthCredential) {
        authenticationRepository.signInWithCredential(credential)
    }

    fun signInAnonymously() {
        authenticationRepository.signInAnonymously()
    }
}