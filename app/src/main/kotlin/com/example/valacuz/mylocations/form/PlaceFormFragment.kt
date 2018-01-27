package com.example.valacuz.mylocations.form


import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.valacuz.mylocations.databinding.FragmentPlaceFormBinding

class PlaceFormFragment : Fragment() {

    private lateinit var mViewModel: PlaceFormViewModel

    private lateinit var mFragmentBinding: FragmentPlaceFormBinding

    private lateinit var mErrorMessageCallback: Observable.OnPropertyChangedCallback

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

        mErrorMessageCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, p1: Int) {
                // Display error message
                Toast.makeText(activity, mViewModel.errorMessage.get(), Toast.LENGTH_SHORT).show()
            }
        }
        mViewModel.errorMessage.addOnPropertyChangedCallback(mErrorMessageCallback)
    }

    override fun onDestroy() {
        mViewModel.errorMessage.removeOnPropertyChangedCallback(mErrorMessageCallback)
        super.onDestroy()
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
