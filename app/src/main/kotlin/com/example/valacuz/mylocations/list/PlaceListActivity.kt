package com.example.valacuz.mylocations.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.ViewModelHolder
import com.example.valacuz.mylocations.data.PlaceDataSource
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.MemoryPlaceDataSource
import com.example.valacuz.mylocations.form.PlaceFormActivity
import com.example.valacuz.mylocations.util.*

class PlaceListActivity : AppCompatActivity(), PlaceNavigator, PlaceItemNavigator {

    private val VIEW_MODEL_TAG = "LIST_VM_TAG"
    private val REQUEST_FORM = 1001

    private lateinit var mViewModel: PlaceListViewModel

    // Long click menu action sources
    private lateinit var mMapDisplaySource: MapDisplaySource
    private lateinit var mShareContentSource: ShareContentSource

    private var mChoiceDialog: PlaceActionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)
        setupToolbar()

        mViewModel = findOrCreateViewModel()
        mViewModel.setNavigator(this)

        mMapDisplaySource = GoogleMapDisplaySource(this)
        mShareContentSource = GoogleMapShareContentSource(this)

        val fragment: PlaceListFragment = findOrCreateFragment()
        fragment.setViewModel(mViewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FORM -> {
                    mViewModel.loadItems()  // Refresh items
                }
            }
        }
    }

    override fun onDestroy() {
        mViewModel.onActivityDestroyed()
        super.onDestroy()
    }

    override fun addLocation() {
        val intent = Intent(this@PlaceListActivity, PlaceFormActivity::class.java)
        startActivityForResult(intent, REQUEST_FORM)
    }

    override fun displayDetail(id: String) {
        val intent = Intent(this@PlaceListActivity, PlaceFormActivity::class.java)
                .putExtra("PLACE_ID", id)
        startActivityForResult(intent, REQUEST_FORM)
    }

    override fun displayItemAction(place: PlaceItem) {
        if (mChoiceDialog == null) {
            mChoiceDialog = PlaceActionDialog.getInstance(place)
                    .setListener(object : PlaceActionDialog.Listener {
                        override fun onShowOnMapClick(place: PlaceItem) {
                            mMapDisplaySource.displayOnMap(place.latitude, place.longitude)
                        }

                        override fun onShareClick(place: PlaceItem) {
                            mShareContentSource.shareContent(place.name, place.latitude, place.longitude)
                        }

                        override fun onDeleteClick(place: PlaceItem) {
                            mViewModel.onDeletePlaceClick(place)
                        }
                    })
        }
        mChoiceDialog!!.show(supportFragmentManager, PlaceActionDialog::class.java.name)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.title = getString(R.string.app_name)
        }
    }

    private fun findOrCreateViewModel(): PlaceListViewModel {
        @Suppress("UNCHECKED_CAST")
        val holder: ViewModelHolder<PlaceListViewModel>? = supportFragmentManager
                .findFragmentByTag(VIEW_MODEL_TAG) as ViewModelHolder<PlaceListViewModel>?
        return if (holder != null) {
            // If the ViewModel was retained, return it.
            holder.getViewModel()!!
        } else {
            // If there no ViewModel yet, create it.
            val itemDataSource: PlaceDataSource = MemoryPlaceDataSource.INSTANCE
            val viewModel = PlaceListViewModel(itemDataSource)
            supportFragmentManager
                    .beginTransaction()
                    .add(ViewModelHolder<PlaceListViewModel>().createContainer(viewModel),
                            VIEW_MODEL_TAG)
                    .commit()
            // return view model
            viewModel
        }
    }

    private fun findOrCreateFragment(): PlaceListFragment {
        var fragment: PlaceListFragment? = supportFragmentManager
                .findFragmentById(R.id.frame) as PlaceListFragment?
        if (fragment == null) {
            fragment = PlaceListFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame, fragment)
                    .commit()
        }
        return fragment
    }
}
