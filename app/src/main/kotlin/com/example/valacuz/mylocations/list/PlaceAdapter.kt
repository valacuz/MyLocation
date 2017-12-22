package com.example.valacuz.mylocations.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.databinding.PlaceItemBinding

class PlaceAdapter :
        RecyclerView.Adapter<PlaceAdapter.LocationViewHolder>() {

    private var mItems: MutableList<PlaceItem> = mutableListOf()

    private var mItemNavigator: PlaceItemNavigator? = null

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item: PlaceItem = mItems[position]
        holder.bindViewModel(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationViewHolder {
        val binding = PlaceItemBinding.inflate(
                LayoutInflater.from(parent!!.context), parent, false)
        return LocationViewHolder(binding, mItemNavigator)
    }

    fun replaceItems(newItems: List<PlaceItem>) {
        mItems.clear()
        mItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: PlaceItem) {
        val itemIndex: Int = mItems.indexOfFirst { it.id == item.id }
        if (itemIndex >= 0) {
            mItems.removeAt(itemIndex)
            notifyItemRemoved(itemIndex)
            notifyDataSetChanged()
        }
    }

    fun setItemNavigator(itemNavigator: PlaceItemNavigator?) {
        mItemNavigator = itemNavigator
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