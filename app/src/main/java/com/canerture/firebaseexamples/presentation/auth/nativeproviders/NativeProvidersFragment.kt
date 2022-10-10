package com.canerture.firebaseexamples.presentation.auth.nativeproviders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.data.wrapper.AuthOperationsWrapper
import com.canerture.firebaseexamples.databinding.FragmentNativeProvidersBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NativeProvidersFragment : Fragment() {

    private var _binding: FragmentNativeProvidersBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authOperations: AuthOperationsWrapper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNativeProvidersBinding.inflate(layoutInflater)
        return binding.root
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}