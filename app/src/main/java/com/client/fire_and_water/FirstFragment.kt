package com.client.fire_and_water

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class FirstFragment : Fragment() {
    private lateinit var mySnackbar : Snackbar

    private var _binding: FragmentStartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!  

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mySnackbar = Snackbar.make(view, "can not connect to server", 10)

        binding.startCheckInButton?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_FifthFragment)
        }

        binding.startSignInButton.setOnClickListener {
            val network : Network = (activity as MainActivity).network
            Thread {
                try {
                    if (!network.isConnected()) {
                        network.startConnection(
                            requireContext().resources.getString(R.string.ip),
                            requireContext().resources.getString(R.string.port).toInt()
                        )
                    }
                    network.sendMessageAndGetMessage("Hello").let { it1 -> Log.i("SERVER MSG", it1) }
                } catch (i: Exception) {
                    i.message?.let { Log.i("Cannot connect", it) }
                }
            }.start()

            if (network.isConnected())
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            else
                mySnackbar.show()
        }

        binding.startGoogleSignInButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
//            updateUI(account)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}