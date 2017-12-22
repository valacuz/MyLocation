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

    private lateinit var mViewModel: PlaceListViewModel

    private lateinit var mFragmentBinding: FragmentPlaceListBinding

    private lateinit var mPlaceAdapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mFragmentBinding = FragmentPlaceListBinding.inflate(inflater, container, false)
        mFragmentBinding.viewModel = mViewModel
        return mFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create location adapter with empty item (so it easier to use later)
        mPlaceAdapter = PlaceAdapter()
        // Set item navigator
        if (activity is PlaceItemNavigator) {
            mPlaceAdapter.setItemNavigator(activity as PlaceItemNavigator)
        }
        // Set recyclerView
        mFragmentBinding.locationList.apply {
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(activity)
            adapter = mPlaceAdapter
        }
        // Start view model
        mViewModel.start()
    }

    fun setViewModel(viewModel: PlaceListViewModel) {
        mViewModel = viewModel
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
