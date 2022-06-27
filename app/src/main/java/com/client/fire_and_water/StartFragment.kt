package com.client.fire_and_water

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import mu.KLogger
import mu.KotlinLogging

class StartFragment : Fragment() {
    private var logger : KLogger = KotlinLogging.logger("Start fragment")
    private var _binding: FragmentStartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!  

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.debug("onCreateView start..")
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        logger.debug("onCreateView end")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.debug("onViewCreated start..")
        binding.startSignUpButton?.setOnClickListener {
            logger.debug("startSignUpButton pressed")
            findNavController().navigate(R.id.action_FirstFragment_to_FifthFragment)
        }

        binding.startLogInButton?.setOnClickListener {
            logger.debug("startLogInButton pressed")
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.startGoogleSignInButton.setOnClickListener {
            logger.debug("startGoogleSignInButton pressed")
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        }
        logger.debug("onViewCreated end")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        logger.debug("")
    }
}