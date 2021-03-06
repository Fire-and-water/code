package com.client.fire_and_water

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentLoadingSpinnerBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

class LoadingSpinnerFragment : Fragment() {

    private var _binding: FragmentLoadingSpinnerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingSpinnerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = (activity as MainActivity).gameId
        binding.LoadingSpinnerTextView.text = context?.resources?.getString(R.string.your_game_id, gameId)
        binding.LoadingSpinnerReturnButton.setOnClickListener {
            (activity as MainActivity).network.cancelGame()
            (activity as MainActivity).pressBack()
        }


        GlobalScope.launch {
            (activity as MainActivity).network.waitGameStart()
            (activity as MainActivity).runOnUiThread{
                findNavController().navigate(R.id.action_SixthFragment_to_SeventhFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}