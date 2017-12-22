package com.example.valacuz.mylocations

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Non-UI fragment used to retain view models from configuration changes.
 */
class ViewModelHolder<VM> : Fragment() {

    private var mViewModel: VM? = null

    fun <M> createContainer(viewModel: M): ViewModelHolder<M> {
        val viewModelContainer = ViewModelHolder<M>()
        viewModelContainer.setViewModel(viewModel)
        return viewModelContainer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private fun setViewModel(viewModel: VM) {
        this.mViewModel = viewModel
    }

    fun getViewModel(): VM? = this.mViewModel
}