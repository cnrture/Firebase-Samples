package com.canerture.firebaseexamples.presentation.auth.nativeproviders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentNativeProvidersBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class NativeProvidersFragment : Fragment(R.layout.fragment_native_providers) {

    private val binding by viewBinding(FragmentNativeProvidersBinding::bind)

    private val viewModel: NativeProvidersViewModel by viewModels()

    private var verificationId: String? = null

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        with(binding) {

            btnSignUp.setOnClickListener {

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signUpWithEmailPassword(email, password)
                }
            }

            btnSignIn.setOnClickListener {

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInWithEmailPassword(email, password)
                }
            }

            btnPhone.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString()

                if (phoneNumber.isNotEmpty()) {
                    sendVerificationCode(phoneNumber)
                }
            }

            btnVerifyCode.setOnClickListener {
                val verifyCode = etVerifyCode.text.toString()

                if (verifyCode.isNotEmpty()) {
                    verificationId?.let {
                        viewModel.verifyCode(it, verifyCode)
                    }
                }
            }

            btnAnonymous.setOnClickListener {
                viewModel.signInAnonymously()
            }
        }
    }

    private fun initObservers() {

        with(viewModel) {

            result.observe(viewLifecycleOwner) { firebaseUser ->
                firebaseUser?.let {
                    Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCode(
        phoneNumber: String
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    viewModel.signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Invalid Request!", Toast.LENGTH_SHORT).show()
                    } else if (e is FirebaseTooManyRequestsException) {
                        Toast.makeText(requireContext(), "Too many request!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Toast.makeText(requireContext(), "Code sent!", Toast.LENGTH_SHORT).show()
                    this@NativeProvidersFragment.verificationId = verificationId
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}