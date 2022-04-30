package com.client.fire_and_water

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.client.fire_and_water.databinding.FragmentGameMenuBinding


class GameMenuFragment : Fragment() {
    var role : Player.Role = Player.Role.FIRE
    private var _binding: FragmentGameMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun changeView() {
        binding.gameMenuImageViewFire.visibility = View.INVISIBLE
        binding.gameMenuImageViewWater.visibility = View.INVISIBLE
        if (role == Player.Role.FIRE)
            binding.gameMenuImageViewFire.visibility = View.VISIBLE
        else
            binding.gameMenuImageViewWater.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val network : Network = (activity as MainActivity).network
        changeView()
        binding.gameMenuFireButton.setOnClickListener {
            role = Player.Role.FIRE
            changeView()
        }

        binding.gameMenuWaterButton.setOnClickListener {
            role = Player.Role.WATER
            changeView()
        }

        binding.gameMenuStartGameButton.setOnClickListener {
            network.createGame(1, role)
        }

        binding.gameMenuConnectButton.setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_ForthFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}