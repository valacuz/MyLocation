package com.example.valacuz.mylocations.list

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.data.PlaceItem

class PlaceActionDialog : DialogFragment() {

    private lateinit var place: PlaceItem

    private var dialog: AlertDialog? = null

    private var listener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (dialog == null) {
            val choices = resources.getStringArray(R.array.item_choices)
            val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, choices)
            dialog = AlertDialog.Builder(requireContext())
                    .setTitle(R.string.item_title)
                    .setAdapter(adapter, { _, position ->
                        when (position) {
                            0 -> listener?.onShowOnMapClick(place)
                            1 -> listener?.onShareClick(place)
                            2 -> listener?.onDeleteClick(place)
                        }
                    })
                    .create()
        }
        return dialog!!
    }

    fun setPlaceItem(place: PlaceItem): PlaceActionDialog {
        this.place = place
        return this
    }

    fun setListener(listener: Listener?): PlaceActionDialog {
        this.listener = listener
        return this
    }

    interface Listener {
        fun onShowOnMapClick(place: PlaceItem)
        fun onShareClick(place: PlaceItem)
        fun onDeleteClick(place: PlaceItem)
    }

    companion object {

        @Volatile
        private var INSTANCE: PlaceActionDialog? = null

        fun getInstance() =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: PlaceActionDialog()
                            .also { INSTANCE = it }
                }
    }
}