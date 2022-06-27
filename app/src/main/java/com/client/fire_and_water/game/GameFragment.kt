package com.client.fire_and_water.game

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.client.fire_and_water.GameMenuFragment
import com.client.fire_and_water.MainActivity
import com.client.fire_and_water.UserClient
import com.client.fire_and_water.databinding.FragmentLoadingSpinnerBinding

class GameFragment : Fragment() {
    private var _binding: FragmentLoadingSpinnerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val displayMetrics = DisplayMetrics()
        (activity as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.heightPixels
        val height = displayMetrics.widthPixels
        if ((activity as MainActivity).role == UserClient.Role.FIRE) {
            return Game(
                requireContext(),
                height,
                width,
                (activity as MainActivity).network,
                PlayerType.FIRE
            )
        } else {
            return Game(
                requireContext(),
                height,
                width,
                (activity as MainActivity).network,
                PlayerType.WATER
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        _binding = null
    }
}