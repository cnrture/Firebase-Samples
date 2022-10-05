package com.canerture.firebaseexamples.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AdsOperationsWrapper
import com.canerture.firebaseexamples.common.gone
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.common.visible
import com.canerture.firebaseexamples.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Firebase.messaging.token.addOnSuccessListener {
            Log.d(TAG, "token -> $it")
        }.addOnFailureListener {
            Log.d(TAG, it.message.orEmpty())
        }

        Firebase.messaging.subscribeToTopic("todo").addOnSuccessListener {
            Log.d(TAG, "success -> subscribeToTopic")
        }.addOnFailureListener {
            Log.d(TAG, "failure -> subscribeToTopic")
        }

        adsOperationsWrapper.initMobileAds(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment -> binding.bottomNav.gone()
                else -> binding.bottomNav.visible()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}