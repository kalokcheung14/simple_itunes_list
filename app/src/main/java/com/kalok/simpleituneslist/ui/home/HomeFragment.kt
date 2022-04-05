package com.kalok.simpleituneslist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalok.simpleituneslist.adapters.AlbumAdapter
import com.kalok.simpleituneslist.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewAdapter : AlbumAdapter
    private lateinit var viewManager : RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        // Get LinearLayoutManager for RecyclerView
        viewManager = LinearLayoutManager(context)

        val albumRecyclerView = binding.albumRecyclerView

        // Fetch data
        homeViewModel.fetchAlbums()

        // Observe for album list to update
        viewAdapter = AlbumAdapter(homeViewModel.albumValue.value!!)
        homeViewModel.albumValue.observe(viewLifecycleOwner) {
            // Update the recycler view data when update is observed
            viewAdapter.setDataset(it)
        }

        // set up recycler view
        albumRecyclerView.apply {
            setHasFixedSize(true)
            minimumHeight = 90
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Handle progress bar show and hide
        val progressBar = binding.progressBar
        progressBar.show()

        homeViewModel.dataLoaded.observe(viewLifecycleOwner) { loaded ->
            // If data is loaded, hide the progress bar
            if (loaded) {
                progressBar.hide()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}