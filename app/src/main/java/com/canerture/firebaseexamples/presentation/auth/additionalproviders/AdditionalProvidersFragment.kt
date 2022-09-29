package com.canerture.firebaseexamples.presentation.auth.additionalproviders

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AuthOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentAdditionalProvidersBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalProvidersFragment : Fragment(R.layout.fragment_additional_providers) {

    private val binding by viewBinding(FragmentAdditionalProvidersBinding::bind)

    private lateinit var oneTapClient: SignInClient

    @Inject
    lateinit var authOperationsWrapper: AuthOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTapClient = Identity.getSignInClient(requireActivity())

        with(binding) {

            btnGoogle.setOnClickListener {

                authOperationsWrapper.signInWithGoogle(requireActivity(), oneTapClient, {
                    googleSignInIntentResultLauncher.launch(it)
                }, {
                    requireView().showSnack(it)
                })
            }

            btnGithub.setOnClickListener {

                authOperationsWrapper.signInWithGithub(requireActivity(), {
                    findNavController().navigate(R.id.authToTodos)
                    requireView().showSnack("Successful!")
                }, {
                    requireView().showSnack(it)
                })
            }

            btnTwitter.setOnClickListener {

                authOperationsWrapper.signInWithTwitter(requireActivity(), {
                    findNavController().navigate(R.id.authToTodos)
                    requireView().showSnack("Successful!")
                }, {
                    requireView().showSnack(it)
                })
            }
        }
    }

    private val googleSignInIntentResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result != null) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                idToken?.let {
                    findNavController().navigate(R.id.authToTodos)
                }
            }
        }
}