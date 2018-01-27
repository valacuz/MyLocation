package com.example.valacuz.mylocations.list

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.data.PlaceItem

class PlaceActionDialog : DialogFragment() {

    private lateinit var mPlace: PlaceItem

    private var mDialog: AlertDialog? = null

    private var mListener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mDialog == null) {
            val choices = resources.getStringArray(R.array.item_choices)
            val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, choices)
            mDialog = AlertDialog.Builder(activity)
                    .setTitle(R.string.item_title)
                    .setAdapter(adapter, { _, position ->
                        when (position) {
                            0 -> mListener?.onShowOnMapClick(mPlace)
                            1 -> mListener?.onShareClick(mPlace)
                            2 -> mListener?.onDeleteClick(mPlace)
                        }
                    })
                    .create()
        }
        return mDialog!!
    }

    private fun setPlaceItem(place: PlaceItem): PlaceActionDialog {
        mPlace = place
        return this
    }

    fun setListener(listener: Listener?): PlaceActionDialog {
        mListener = listener
        return this
    }

    companion object {

        @Volatile
        private var INSTANCE: PlaceActionDialog? = null

        fun getInstance(place: PlaceItem) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: PlaceActionDialog().setPlaceItem(place)
                }
    }

    interface Listener {
        fun onShowOnMapClick(place: PlaceItem)
        fun onShareClick(place: PlaceItem)
        fun onDeleteClick(place: PlaceItem)
    }
}