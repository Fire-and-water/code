package com.client.fire_and_water

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.fire_and_water.databinding.FragmentConnectToGameBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ConnectToGameFragment : Fragment() {

    private var _binding: FragmentConnectToGameBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectToGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val network : Network = (activity as MainActivity).network

        binding.connectToGameConnectButton.setOnClickListener {
            GlobalScope.launch {
                network.connectToGame(binding.connectToGameDecimalEditText.text.toString())
            }
        }
    }
}