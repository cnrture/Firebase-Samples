package com.canerture.firebaseexamples.presentation.auth.additionalproviders

import android.content.IntentSender
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentAdditionalProvidersBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalProvidersFragment : Fragment(R.layout.fragment_additional_providers) {

    private val binding by viewBinding(FragmentAdditionalProvidersBinding::bind)

    private lateinit var oneTapClient: SignInClient

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnGoogle.setOnClickListener {

                val signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(getString(R.string.default_web_client_id))
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .build()

                oneTapClient = Identity.getSignInClient(requireActivity())

                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(requireActivity()) { result ->
                        try {
                            val intentSenderRequest =
                                IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                                    .build()
                            googleSignInIntentResultLauncher.launch(intentSenderRequest)
                        } catch (e: IntentSender.SendIntentException) {
                            Toast.makeText(
                                requireContext(),
                                "Couldn't start One Tap UI: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener(requireActivity()) { e ->
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
            }

            btnGithub.setOnClickListener {

                val provider = OAuthProvider.newBuilder("github.com")
                provider.addCustomParameter("login", "")

                firebaseAuth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        authResult.user?.let {
                            findNavController().navigate(R.id.authToFirestoreOperations)
                            Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
            }

            btnTwitter.setOnClickListener {
                val provider = OAuthProvider.newBuilder("twitter.com")
                provider.addCustomParameter("lang", "")

                firebaseAuth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        authResult.user?.let {
                            findNavController().navigate(R.id.authToFirestoreOperations)
                            Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener {
                        println(it.message)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private val googleSignInIntentResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result != null) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                idToken?.let {
                    findNavController().navigate(R.id.authToFirestoreOperations)
                    Toast.makeText(requireContext(), "idToken", Toast.LENGTH_SHORT).show()
                }
            }
        }
}