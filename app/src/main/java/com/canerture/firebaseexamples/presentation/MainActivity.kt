package com.canerture.firebaseexamples.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.gone
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.common.visible
import com.canerture.firebaseexamples.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.authFragment) {
                binding.bottomNav.gone()
            } else {
                binding.bottomNav.visible()
            }
        }
    }
}