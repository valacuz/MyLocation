package com.example.valacuz.mylocations.list

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.valacuz.mylocations.GlideApp
import com.example.valacuz.mylocations.R

object PlaceListItemBindings {

    @BindingAdapter(value = ["picture_path"])
    @JvmStatic
    fun setPicture(imageView: ImageView, picturePath: String?) {
        if (!picturePath.isNullOrBlank()) {
            GlideApp.with(imageView.context)
                    .load(picturePath)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .fallback(R.mipmap.icon_no_image)
                    .into(imageView)
        } else {
            imageView.setImageResource(R.mipmap.icon_no_image)
        }
    }

    @BindingAdapter(value = ["location_type"])
    @JvmStatic
    fun setIconType(imageView: ImageView, id: Int) {
        val resId = when (id) {
            1 -> R.mipmap.ic_type_education
            2 -> R.mipmap.ic_type_shop
            3 -> R.mipmap.ic_type_relax
            4 -> R.mipmap.ic_type_restaurant
            else -> 0
        }
        imageView.setImageResource(resId)
    }
}