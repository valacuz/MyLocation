package com.example.valacuz.mylocations.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.databinding.PlaceItemBinding

class PlaceAdapter :
        RecyclerView.Adapter<PlaceAdapter.LocationViewHolder>() {

    private var items: MutableList<PlaceItem> = mutableListOf()

    private var itemNavigator: PlaceItemNavigator? = null

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item: PlaceItem = items[position]
        holder.bindViewModel(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = PlaceItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding, itemNavigator)
    }

    fun replaceItems(newItems: List<PlaceItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: PlaceItem) {
        val itemIndex: Int = items.indexOfFirst { it.id == item.id }
        if (itemIndex >= 0) {
            items.removeAt(itemIndex)
            notifyItemRemoved(itemIndex)
            notifyDataSetChanged()
        }
    }

    fun setItemNavigator(itemNavigator: PlaceItemNavigator?) {
        this.itemNavigator = itemNavigator
    }

    class LocationViewHolder(binding: PlaceItemBinding, itemNavigator: PlaceItemNavigator?) :
            RecyclerView.ViewHolder(binding.root) {

        private val mBinding: PlaceItemBinding = binding

        private val mItemNavigator: PlaceItemNavigator? = itemNavigator

        fun bindViewModel(item: PlaceItem) {
            mBinding.viewModel = PlaceItemViewModel(item).apply { setNavigator(mItemNavigator) }
            mBinding.executePendingBindings() // Force binding run immediately instead of delay until next frame
        }
    }
}