package com.canerture.firebaseexamples.presentation.auth.additionalproviders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentAdditionalProvidersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdditionalProvidersFragment : Fragment(R.layout.fragment_additional_providers) {

    private val binding by viewBinding(FragmentAdditionalProvidersBinding::bind)

    private val viewModel: AdditionalProvidersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        with(binding) {


        }
    }

    private fun initObservers() {

        with(viewModel) {


        }
    }
}