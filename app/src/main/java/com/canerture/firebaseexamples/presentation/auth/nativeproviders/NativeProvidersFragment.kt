package com.canerture.firebaseexamples.presentation.auth.nativeproviders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AuthOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentNativeProvidersBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NativeProvidersFragment : Fragment(R.layout.fragment_native_providers) {

    private val binding by viewBinding(FragmentNativeProvidersBinding::bind)

    @Inject
    lateinit var authOperations: AuthOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnSignUp.setOnClickListener {

                checkEmailAndPassword { email, password ->
                    authOperations.signUpWithEmailAndPassword(email, password,
                        onSuccess = {
                            findNavController().navigate(R.id.authToTodos)
                        },
                        onFailure = {
                            requireView().showSnack(it)
                        })
                }
            }

            btnSignIn.setOnClickListener {

                checkEmailAndPassword { email, password ->
                    authOperations.signInWithEmailAndPassword(email, password,
                        onSuccess = {
                            findNavController().navigate(R.id.authToTodos)
                        },
                        onFailure = {
                            requireView().showSnack(it)
                        })
                }
            }

            btnPhone.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString()

                if (phoneNumber.isNotEmpty()) {
                    authOperations.sendVerificationCode(phoneNumber,
                        onCodeSent = {
                            requireView().showSnack("Code sent!")
                        },

                        onSuccess = {
                            findNavController().navigate(R.id.authToTodos)
                            requireView().showSnack("Successful!")
                        },

                        onFailure = {
                            requireView().showSnack(it)
                        }
                    )
                }
            }

            btnVerifyCode.setOnClickListener {
                val verifyCode = etVerifyCode.text.toString()

                if (verifyCode.isNotEmpty()) {
                    authOperations.verifyCode(verifyCode,
                        onSuccess = {
                            findNavController().navigate(R.id.authToTodos)
                        },

                        onFailure = {
                            requireView().showSnack(it)
                        }
                    )
                }
            }

            btnAnonymous.setOnClickListener {
                authOperations.signInAnonymously(
                    onSuccess = {
                        findNavController().navigate(R.id.authToTodos)
                    },

                    onFailure = {
                        requireView().showSnack(it)
                    }
                )
            }
        }
    }

    private fun checkEmailAndPassword(
        onSuccess: (String, String) -> Unit
    ) {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            onSuccess(email, password)
        } else {
            requireView().showSnack("Email or password cannot be empty!")
        }
    }
}