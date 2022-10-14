package com.canerture.firebaseexamples.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.gone
import com.canerture.firebaseexamples.common.showLogDebug
import com.canerture.firebaseexamples.common.visible
import com.canerture.firebaseexamples.data.wrapper.AdsOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.RemoteConfigWrapper
import com.canerture.firebaseexamples.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    @Inject
    lateinit var remoteConfigWrapper: RemoteConfigWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteConfigWrapper.initRemoteConfig()

        adsOperationsWrapper.initMobileAds(this)

        Firebase.messaging.subscribeToTopic("todo").addOnSuccessListener {
            showLogDebug(TAG, "subscribeTopic: onSuccess")
        }.addOnFailureListener {
            showLogDebug(TAG, "subscribeTopic: onFailure")
        }

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