package com.kalok.simpleituneslist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kalok.simpleituneslist.databinding.AlbumItemRowBinding
import com.kalok.simpleituneslist.models.Album
import com.squareup.picasso.Picasso

class AlbumAdapter(private val albums: ArrayList<Album>): RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AlbumItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            // Bind data to row item view
            with(binding) {
                albumNameTextview.text = album.collectionName
                Picasso.get().load(album.artworkUrl60).fit()
                    .placeholder(android.R.color.darker_gray).into(artworkImageView)
            }
        }
    }

    fun setDataset(data : ArrayList<Album>) {
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