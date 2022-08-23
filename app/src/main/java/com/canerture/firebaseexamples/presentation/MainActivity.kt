package com.canerture.firebaseexamples.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}