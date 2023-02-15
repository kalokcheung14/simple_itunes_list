package com.kalok.simpleituneslist.ui.bookmarks

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
import com.kalok.simpleituneslist.adapters.BookmarkListAdapter
import com.kalok.simpleituneslist.adapters.setup
import com.kalok.simpleituneslist.databinding.FragmentBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment() {
    private var _binding: FragmentBookmarksBinding? = null
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
        val bookmarksViewModel =
            ViewModelProvider(this)[BookmarksViewModel::class.java]

        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Use Shimmer Layout to display shimmer loading screen
        _shimmerLayout = binding.shimmerLayout
        _shimmerLayout.visibility = View.VISIBLE
        _shimmerLayout.startShimmer()

        val albumRecyclerView = binding.albumRecyclerView
        // Set recycler view invisible at the beginning for loading
        albumRecyclerView.visibility = View.INVISIBLE

        // Set up viewManager to handle recycler view row layout
        _viewManager = LinearLayoutManager(context)
        // Set up view adapter for recycler view dataset
        _viewAdapter = BookmarkListAdapter(bookmarksViewModel.albumValue.value!!, bookmarksViewModel, bookmarksViewModel.bookmarkRepository)

        // Set no bookmark notice invisible when loading
        val noBookmarkTextView = binding.noBookmarkTextview
        noBookmarkTextView.visibility = View.GONE

        // Fetch data
        bookmarksViewModel.fetchData()

        // Observe for album list
        bookmarksViewModel.albumValue.observe(viewLifecycleOwner) {
            // Stop Shimmer animation
            _shimmerLayout.stopShimmer()

            // Update the UI display when update on album list is observed by passing data variable to the UI
            binding.albumListIsEmpty = it.isEmpty()

            // Update the adapter when update on album list is observed
            if (it.isNotEmpty()) {
                // Load the data to adapter if the list is not empty
                _viewAdapter.setDataset(it)
            }
        }

        // Set up recycler view
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