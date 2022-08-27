package com.canerture.firebaseexamples.presentation.auth.nativeproviders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AuthOperationsWrapper
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

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authOperations.signUpWithEmailAndPassword(email, password, {
                        findNavController().navigate(R.id.authToFirestoreOperations)
                        Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                }
            }

            btnSignIn.setOnClickListener {

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authOperations.signInWithEmailAndPassword(email, password, {
                        findNavController().navigate(R.id.authToFirestoreOperations)
                        Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                }
            }

            btnPhone.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString()

                if (phoneNumber.isNotEmpty()) {
                    authOperations.sendVerificationCode(phoneNumber, {
                        Toast.makeText(requireContext(), "Code sent!", Toast.LENGTH_SHORT).show()
                    }, {
                        findNavController().navigate(R.id.authToFirestoreOperations)
                        Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                }
            }

            btnVerifyCode.setOnClickListener {
                val verifyCode = etVerifyCode.text.toString()

                if (verifyCode.isNotEmpty()) {
                    authOperations.verifyCode(verifyCode, {
                        findNavController().navigate(R.id.authToFirestoreOperations)
                        Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                }
            }

            btnAnonymous.setOnClickListener {
                authOperations.signInAnonymously({
                    findNavController().navigate(R.id.authToFirestoreOperations)
                    Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}