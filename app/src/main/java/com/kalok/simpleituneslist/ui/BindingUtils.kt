package com.kalok.simpleituneslist.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingUtils {
    // Function to bind image to ImageView
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(view: ImageView, imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .fit().into(view)
    }
}