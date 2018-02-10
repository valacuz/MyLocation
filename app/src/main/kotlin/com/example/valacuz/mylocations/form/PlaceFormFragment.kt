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

    private lateinit var viewModel: PlaceFormViewModel

    private lateinit var fragmentBinding: FragmentPlaceFormBinding

    private lateinit var errorMessageCallback: Observable.OnPropertyChangedCallback

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentPlaceFormBinding.inflate(inflater, container, false)
        fragmentBinding.viewModel = viewModel
        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.create()

        errorMessageCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, p1: Int) {
                // Display error message
                Toast.makeText(activity, viewModel.errorMessage.get(), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorMessage.addOnPropertyChangedCallback(errorMessageCallback)
    }

    override fun onDestroy() {
        viewModel.errorMessage.removeOnPropertyChangedCallback(errorMessageCallback)
        super.onDestroy()
    }

    fun setViewModel(viewModel: PlaceFormViewModel) {
        this.viewModel = viewModel
    }

    companion object {
        fun newInstance(): PlaceFormFragment = PlaceFormFragment()
    }
}
