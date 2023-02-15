package com.kalok.simpleituneslist.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

// Extension function to setup recycler view
fun RecyclerView.setup() {
    setHasFixedSize(true)
    minimumHeight = 90
    // Disable item change default animation
    (itemAnimator as SimpleItemAnimator).apply {
        supportsChangeAnimations = false
    }
}