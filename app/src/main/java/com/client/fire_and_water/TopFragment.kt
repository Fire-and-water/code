package com.client.fire_and_water
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.client.fire_and_water.databinding.FragmentTopBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KLogger
import mu.KotlinLogging

class TopFragment : Fragment() {

    private var logger : KLogger = KotlinLogging.logger("Top fragment")
    private var _binding: FragmentTopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.debug("onCreateView start..")
        _binding = FragmentTopBinding.inflate(inflater, container, false)
        logger.debug("onCreateView end")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.TopFragmentReturn?.setOnClickListener {
            findNavController().navigate(R.id.action_TopFragment_to_ThirdFragment)
        }
        GlobalScope.launch {
            val topList = (activity as MainActivity).network.getTop()
            binding.textView.text = topList[0].nickname
            binding.textView2.text = topList[1].nickname
            binding.textView3.text = topList[2].nickname
            binding.textView4.text = topList[3].nickname
            binding.textView5.text = topList[4].nickname
            binding.textView6.text = topList[5].nickname
            binding.textView7.text = topList[6].nickname
            binding.textView8.text = topList[7].nickname
            binding.textView9.text = topList[8].nickname
            binding.textView10.text = topList[9].nickname
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        logger.debug("")
    }
}