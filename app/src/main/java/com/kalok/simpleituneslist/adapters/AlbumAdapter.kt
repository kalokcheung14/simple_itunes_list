package com.kalok.simpleituneslist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kalok.simpleituneslist.R
import com.kalok.simpleituneslist.databinding.AlbumItemRowBinding
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel

abstract class AlbumAdapter(
    protected var _albums: ArrayList<AlbumViewModel>,
    protected val _parentViewModel: ViewModel? = null
): RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    inner class ViewHolder(private val _binding: AlbumItemRowBinding): RecyclerView.ViewHolder(_binding.root) {
        fun bind(album: AlbumViewModel, position: Int) {
            // Bind data to row item view
            with(_binding) {
                this.album = album
                this.executePendingBindings()
                // Set bookmark icon according to bookmark flag in album
                album.bookmarked.let { bookmarked ->
                    if (bookmarked) {
                        bookmarkImageView.setImageResource(R.drawable.outline_bookmark_24)
                    } else {
                        bookmarkImageView.setImageResource(R.drawable.outline_bookmark_border_24)
                    }

                    // Set on click listener for bookmark icon
                    bookmarkImageView.setOnClickListener {
                        if (!bookmarked) {
                            // If album is not bookmarked, bookmark the album and set the icon to solid
                            bookmarkImageView.setImageResource(R.drawable.outline_bookmark_24)
                            // Update display
                            notifyItemChanged(position)
                        } else {
                            // If album is bookmarked, remove the album from bookmark and set the icon to outline
                            bookmarkImageView.setImageResource(R.drawable.outline_bookmark_border_24)
                            handleRemoveBookmark(album, position)
                        }

                        // Update bookmark flag to viewModel and DB
                        album.bookmarked = !bookmarked
                    }
                }
            }
        }
    }

    abstract fun handleRemoveBookmark(album: AlbumViewModel, position: Int)

    fun setDataset(data : ArrayList<AlbumViewModel>) {
        // Check data differences
        val diffCallback = ItemListDiffUtil(_albums, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        // Update data when they are different
        diffResult.dispatchUpdatesTo(this)

        // Clear album and fill data set
        _albums.clear()
        _albums.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Pass parent and set attachToParent to false to ensures the constraints of the parent are used
        val binding = AlbumItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(_albums[position], position)

    override fun getItemCount(): Int = _albums.size
}