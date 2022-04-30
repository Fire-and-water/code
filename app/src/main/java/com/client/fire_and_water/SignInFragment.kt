package com.client.fire_and_water

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentSignInBinding

import android.widget.TextView


class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val network : Network = (activity as MainActivity).network
        var s = "123"
        binding.signInSignInButton.setOnClickListener {
            val thread = Thread {
                s = network.sendMessageAndGetMessage((requireActivity().findViewById(R.id.signInNameEdittext) as TextView).text.toString()
                + ' ' + (requireActivity().findViewById(R.id.signInPasswordEdittext) as TextView).text.toString())
                Log.i("SEND MESSAGE BUTTON", "got $s")
            }
            thread.start()
            thread.join()
            val myAwesomeTextView = requireActivity().findViewById(R.id.signInGreetingsTextView) as TextView
            myAwesomeTextView.text = s
            findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)

        }
        binding.signInReturnButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}