package com.kalok.simpleituneslist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kalok.simpleituneslist.R
import com.kalok.simpleituneslist.databinding.AlbumItemRowBinding
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel

class AlbumAdapter(private val albums: ArrayList<AlbumViewModel>): RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    inner class ViewHolder(private val _binding: AlbumItemRowBinding): RecyclerView.ViewHolder(_binding.root) {
        fun bind(album: AlbumViewModel) {
            // Bind data to row item view
            with(_binding) {
                albumNameTextview.text = album.albumName
                album.artwork?.into(artworkImageView)

                // Set bookmark icon according to bookmark flag in album
                if (album.bookmarked) {
                    bookmarkImageView.setImageResource(R.drawable.outline_bookmark_24)
                } else {
                    bookmarkImageView.setImageResource(R.drawable.outline_bookmark_border_24)
                }

                // Set on click listener for bookmark icon
                bookmarkImageView.setOnClickListener {
                    if (!album.bookmarked) {
                        // If album is not bookmarked, bookmark the album and set the icon to solid
                        bookmarkImageView.setImageResource(R.drawable.outline_bookmark_24)
                        album.bookmarked = true
                    } else {
                        // If album is bookmarked, remove the album from bookmark and set the icon to outline
                        bookmarkImageView.setImageResource(R.drawable.outline_bookmark_border_24)
                        album.bookmarked = false
                    }
                }
            }
        }
    }

    fun setDataset(data : ArrayList<AlbumViewModel>) {
        // Check data differences
        val diffCallback = ItemListDiffUtil(albums, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        // Update data when they are different
        diffResult.dispatchUpdatesTo(this)

        // Clear album and fill data set
        albums.clear()
        albums.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Pass parent and set attachToParent to false to ensures the constraints of the parent are used
        val binding = AlbumItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(albums[position])

    override fun getItemCount(): Int = albums.size
}