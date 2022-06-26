package com.client.fire_and_water

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentLogInBinding

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val network : Network = (activity as MainActivity).network
        binding.logInLogInButton.setOnClickListener {
            GlobalScope.launch {
                val email = binding.logInEmailOrUsernameEdittext.text.toString()
                val password = binding.logInPasswordEdittext.text.toString()
                val mactivity = activity as MainActivity
                if (network.checkEmailAuthorization(email, password) and
                    mactivity.network.startConnection(
                        requireContext().resources.getString(R.string.ip),
                        requireContext().resources.getString(R.string.port).toInt())) {
                    mactivity.runOnUiThread{
//                        mactivity.network.cancelGame()
                        findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
                    }
                } else {
                    makeToast(getString(R.string.log_in_wrong_input_toast),
                            activity as MainActivity)
                }
                (activity as MainActivity).turnOffBackButton = false
            }
        }

        binding.logInReturnButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}