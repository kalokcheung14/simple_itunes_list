package com.kalok.simpleituneslist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kalok.simpleituneslist.adapters.AlbumAdapter
import com.kalok.simpleituneslist.adapters.HomeListAdapter
import com.kalok.simpleituneslist.adapters.setup
import com.kalok.simpleituneslist.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var _viewAdapter : AlbumAdapter
    private lateinit var _viewManager : RecyclerView.LayoutManager
    private lateinit var _shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        // Use Shimmer Layout to display shimmer loading screen
        _shimmerLayout = binding.shimmerLayout
        _shimmerLayout.visibility = View.VISIBLE
        _shimmerLayout.startShimmer()

        // Get LinearLayoutManager for RecyclerView
        _viewManager = LinearLayoutManager(context)

        val albumRecyclerView = binding.albumRecyclerView
        // Set recycler view invisible at the beginning for loading
        albumRecyclerView.visibility = View.INVISIBLE

        // Fetch data
        homeViewModel.fetchData()

        // Observe for album list to update
        _viewAdapter = HomeListAdapter(homeViewModel.albumValue.value!!)
        homeViewModel.albumValue.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                // Update the recycler view data when update is observed
                _viewAdapter.setDataset(it)
                _shimmerLayout.stopShimmer()
                albumRecyclerView.visibility = View.VISIBLE
            }
        }

        // set up recycler view
        albumRecyclerView.apply {
            setup()
            layoutManager = _viewManager
            adapter = _viewAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}