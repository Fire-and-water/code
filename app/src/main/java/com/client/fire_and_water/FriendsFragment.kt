package com.client.fire_and_water

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.client.fire_and_water.databinding.FragmentFriendsBinding

class FriendsFragment : Fragment() {

    lateinit var listAdapter: ArrayAdapter<String>
    lateinit var friendNames: ArrayList<String>
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendNames = getDummyFriendsList()

        listAdapter = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            friendNames
        )

        binding.friendsList.adapter = listAdapter

        binding.searchLine.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                if (friendNames.contains(p0)) {
                    listAdapter.filter.filter(p0)
                } else {
                    Toast.makeText(
                        this@FriendsFragment.context,
                        "No friends found..",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                listAdapter.filter.filter(p0)
                return false
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDummyFriendsList(): ArrayList<String> {
        return arrayListOf("Alex", "Alice", "Ashley", "Bob", "Bread", "George", "Martha")
    }
}