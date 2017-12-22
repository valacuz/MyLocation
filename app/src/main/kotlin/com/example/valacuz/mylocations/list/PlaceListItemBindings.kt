package com.example.valacuz.mylocations.list

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.example.valacuz.mylocations.R

object PlaceListItemBindings {

    @BindingAdapter("bind:location_type")
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