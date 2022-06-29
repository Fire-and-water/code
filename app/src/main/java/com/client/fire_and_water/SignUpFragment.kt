package com.client.fire_and_water

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentSignUpBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun isEmailValid(email: CharSequence?): Boolean {
        if (email == null)
            return false
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun checkPasswords() : Boolean {
        return !(binding.signUpTextPassword.text.toString().isEmpty()
            || binding.signUpRepeatPassword.text.toString().isEmpty()
            || binding.signUpTextPassword.text.toString() != binding.signUpRepeatPassword.text.toString())
    }

    private fun checkEditTexts(): Boolean {
        val network: Network = (activity as MainActivity).network
        val email = binding.signUpEditTextEmailAddress.text.toString()
        val nickname = binding.signUpEditTextPersonName.text.toString()
        val password = binding.signUpTextPassword.text.toString()
        if (!isEmailValid(email)) {
          makeToast("Invalid email", activity as MainActivity)
        } else if (!network.checkUserEmail(email)) {
            makeToast("email is empty or not free", activity as MainActivity)
        } else if (!network.checkNickName(nickname)) {
            makeToast("nickname is empty or not free", activity as MainActivity)
        } else if (password.length < 6) {
            makeToast("Password is too short", activity as MainActivity)

        } else if (!checkPasswords()) {
            makeToast("password is empty or doesn't match", activity as MainActivity)
        } else if (!network.registerByEmail(email, nickname, password)) {
            makeToast("something went wrong, try later", activity as MainActivity)
        } else {
            makeToast("success", activity as MainActivity)
            return true
        }
        return false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpSignUpButton.setOnClickListener {
            GlobalScope.launch {
                if (checkEditTexts()) {
                    (activity as MainActivity).runOnUiThread{
                        findNavController().navigate(R.id.action_FifthFragment_to_FirstFragment)
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}