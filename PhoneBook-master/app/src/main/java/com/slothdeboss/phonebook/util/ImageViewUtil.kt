package com.slothdeboss.phonebook.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}
