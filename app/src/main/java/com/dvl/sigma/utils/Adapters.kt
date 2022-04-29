package com.dvl.sigma.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.dvl.sigma.R

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImageFromUrl(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this.context).load(Constants.IMAGE_BASE_URL + url)
            .placeholder(R.drawable.placeholder_image).into(this);
    }
}