package com.client.fire_and_water

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO use standard logger

class StartFragment : Fragment() {

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
        binding.startSignUpButton?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_FifthFragment)
        }

        binding.startLogInButton?.setOnClickListener {
            val network : Network = (activity as MainActivity).network
            GlobalScope.launch {
                try {
                    if (!network.isConnected()) {
                        network.startConnection(
                            requireContext().resources.getString(R.string.ip),
                            requireContext().resources.getString(R.string.port).toInt()
                        )
                    }
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                } catch (i: Exception) {
                    makeToast("can not connect to server", activity as MainActivity)
                }
            }
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