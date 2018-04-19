package com.example.valacuz.mylocations.list

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.example.valacuz.mylocations.data.PlaceItem

object PlaceListBindings {

    @BindingAdapter(value = ["place_items"])
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<PlaceItem>) {
        val adapter = recyclerView.adapter
        if (adapter is PlaceAdapter) {
            adapter.replaceItems(items)
        }
    }
}