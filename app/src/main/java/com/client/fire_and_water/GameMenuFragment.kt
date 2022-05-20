package com.client.fire_and_water

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.client.fire_and_water.databinding.FragmentGameMenuBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GameMenuFragment : Fragment() {
    private var role : Player.Role = Player.Role.FIRE
    private var _binding: FragmentGameMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private fun changeView() {
        binding.gameMenuImageViewFire?.visibility = View.INVISIBLE
        binding.gameMenuImageViewWater?.visibility = View.INVISIBLE
        if (role == Player.Role.FIRE)
            binding.gameMenuImageViewFire?.visibility = View.VISIBLE
        else
            binding.gameMenuImageViewWater?.visibility = View.VISIBLE
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
        changeView()
        binding.gameMenuFireButton?.setOnClickListener {
            role = Player.Role.FIRE
            changeView()
        }

        binding.gameMenuWaterButton?.setOnClickListener {
            role = Player.Role.WATER
            changeView()
        }

        binding.gameMenuStartGameButton?.setOnClickListener {
            GlobalScope.launch {
                var gameId : Int? = (activity as MainActivity).network.createGame(1, role)
                if (gameId != null) {
                    findNavController().navigate(R.id.action_ThirdFragment_to_SixthFragment)
                } else {
                    makeToast("Can't create game, try later", activity as MainActivity);
                }
            }
        }

        binding.gameMenuConnectButton?.setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_ForthFragment)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}