package com.example.valacuz.mylocations.form


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.valacuz.mylocations.databinding.FragmentPlaceFormBinding

class PlaceFormFragment : Fragment() {

    private lateinit var mViewModel: PlaceFormViewModel

    private lateinit var mFragmentBinding: FragmentPlaceFormBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mFragmentBinding = FragmentPlaceFormBinding.inflate(inflater, container, false)
        mFragmentBinding.viewModel = mViewModel
        return mFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.create()
    }

    fun setViewModel(viewModel: PlaceFormViewModel) {
        mViewModel = viewModel
    }

    companion object {
        /**
         * Use this factory method to create a new INSTANCE of
         * this fragment using the provided parameters.
         *
         * @return A new INSTANCE of fragment PlaceFormFragment.
         */
        fun newInstance(): PlaceFormFragment = PlaceFormFragment()
    }
}
