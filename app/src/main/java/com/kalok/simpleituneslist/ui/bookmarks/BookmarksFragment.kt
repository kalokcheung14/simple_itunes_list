package com.kalok.simpleituneslist.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalok.simpleituneslist.adapters.AlbumAdapter
import com.kalok.simpleituneslist.databinding.FragmentBookmarksBinding
import com.kalok.simpleituneslist.databinding.FragmentHomeBinding

class BookmarksFragment : Fragment() {
    private var _binding: FragmentBookmarksBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var _viewAdapter : AlbumAdapter
    private lateinit var _viewManager : RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bookmarksViewModel =
            ViewModelProvider(this)[BookmarksViewModel::class.java]

        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Handle progress bar show and hide
        val progressBar = binding.progressBar
        progressBar.show()

        val albumRecyclerView = binding.albumRecyclerView

        // Set up viewManager to handle recycler view row layout
        _viewManager = LinearLayoutManager(context)
        // Set up view adapter for recycler view dataset
        _viewAdapter = AlbumAdapter(bookmarksViewModel.albumValue.value!!, AlbumAdapter.Type.BOOKMARKED)

        // Fetch data
        bookmarksViewModel.fetchData()

        // Observe for album list
        bookmarksViewModel.albumValue.observe(viewLifecycleOwner) {
            // Update the recycler view data when update is observed
            _viewAdapter.setDataset(it)
            progressBar.hide()
        }

        // Retain recycler view scroll position when fragment reattached
        _viewAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Set up recycler view
        albumRecyclerView.apply {
            setHasFixedSize(true)
            minimumHeight = 90
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