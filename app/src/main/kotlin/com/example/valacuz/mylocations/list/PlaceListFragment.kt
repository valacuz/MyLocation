package com.example.valacuz.mylocations.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.valacuz.mylocations.databinding.FragmentPlaceListBinding

class PlaceListFragment : Fragment() {

    private lateinit var viewModel: PlaceListViewModel

    private lateinit var fragmentBinding: FragmentPlaceListBinding

    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentPlaceListBinding.inflate(inflater, container, false)
        fragmentBinding.viewModel = viewModel
        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create location adapter with empty item (so it easier to use later)
        placeAdapter = PlaceAdapter()
        // Set item navigator
        if (activity is PlaceItemNavigator) {
            placeAdapter.setItemNavigator(activity as PlaceItemNavigator)
        }
        // Set recyclerView
        fragmentBinding.locationList.apply {
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(activity)
            adapter = placeAdapter
        }
        // Start view model
        viewModel.start()
    }

    fun setViewModel(viewModel: PlaceListViewModel) {
        this.viewModel = viewModel
    }

    companion object {
        /**
         * Use this factory method to create a new INSTANCE of
         * this fragment using the provided parameters.
         *
         * @return A new INSTANCE of fragment PlaceListFragment.
         */
        fun newInstance(): PlaceListFragment = PlaceListFragment()
    }
}
