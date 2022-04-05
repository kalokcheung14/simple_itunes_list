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

        viewManager = LinearLayoutManager(context)

        val albumRecyclerView = binding.albumRecyclerView

        homeViewModel.fetchAlbums()
        viewAdapter = AlbumAdapter(homeViewModel.albumValue.value!!)
        homeViewModel.albumValue.observe(viewLifecycleOwner) {
            viewAdapter.setDataset(it)
        }

        albumRecyclerView.apply {
            setHasFixedSize(true)
            minimumHeight = 90
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}