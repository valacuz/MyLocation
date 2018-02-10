package com.example.valacuz.mylocations.form

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.example.valacuz.mylocations.data.PlaceType

object PlaceFormBindings {

    @BindingAdapter(value = ["bind:typePickAttrChanged", "bind:type_pick", "bind:type_items"], requireAll = false)
    @JvmStatic
    fun setSelectedPlaceType(spinner: AppCompatSpinner,
                             bindingListener: InverseBindingListener,
                             type: PlaceType?,
                             items: List<PlaceType>?) {
        if (items != null && items.isNotEmpty()) {
            // Create adapter when its null.
            spinner.adapter = ArrayAdapter<PlaceType>(spinner.context,
                    android.R.layout.simple_spinner_item, items)

            // Set index from given items
            if (type != null) {
                val itemIndex = items.indexOfFirst { it.id == type.id }
                // Prevent loop
                if (spinner.selectedItemPosition != itemIndex) {
                    spinner.setSelection(itemIndex)
                }
            }
        }
        // Create item selected listener when its null.
        if (spinner.onItemSelectedListener == null) {
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // Do nothing.
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    bindingListener.onChange()
                }
            }
        }
    }

    @InverseBindingAdapter(attribute = "bind:type_pick", event = "bind:typePickAttrChanged")
    @JvmStatic
    fun getSelectedPlaceType(spinner: AppCompatSpinner): PlaceType {
        return spinner.selectedItem as PlaceType
    }
}