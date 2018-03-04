package com.example.valacuz.mylocations.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.ViewModelHolder
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceDataSource
import com.example.valacuz.mylocations.domain.display.GoogleMapDisplaySource
import com.example.valacuz.mylocations.domain.display.MapDisplaySource
import com.example.valacuz.mylocations.domain.share.GoogleMapShareSource
import com.example.valacuz.mylocations.domain.share.ShareContentSource
import com.example.valacuz.mylocations.form.PlaceFormActivity
import com.example.valacuz.mylocations.util.DefaultScheduleStrategy

class PlaceListActivity : AppCompatActivity(), PlaceNavigator, PlaceItemNavigator {

    private val VIEW_MODEL_TAG = "LIST_VM_TAG"
    private val REQUEST_FORM = 2001

    private lateinit var viewModel: PlaceListViewModel

    // Long click menu action sources
    private lateinit var mapDisplaySource: MapDisplaySource
    private lateinit var shareContentSource: ShareContentSource

    private var choiceDialog: PlaceActionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)
        setupToolbar()

        viewModel = findOrCreateViewModel()
        viewModel.setNavigator(this)

        mapDisplaySource = GoogleMapDisplaySource(this)
        shareContentSource = GoogleMapShareSource(this)

        val fragment: PlaceListFragment = findOrCreateFragment()
        fragment.setViewModel(viewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FORM -> {
                    viewModel.loadItems()  // Refresh items
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.onActivityDestroyed()
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
        if (choiceDialog == null) {
            choiceDialog = PlaceActionDialog.getInstance(place)
                    .setListener(object : PlaceActionDialog.Listener {
                        override fun onShowOnMapClick(place: PlaceItem) {
                            mapDisplaySource.displayOnMap(place.latitude, place.longitude)
                        }

                        override fun onShareClick(place: PlaceItem) {
                            shareContentSource.shareContent(place.name!!, place.latitude, place.longitude)
                        }

                        override fun onDeleteClick(place: PlaceItem) {
                            viewModel.onDeletePlaceClick(place)
                        }
                    })
        }
        choiceDialog!!.show(supportFragmentManager, PlaceActionDialog::class.java.name)
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
            val itemDataSource = RoomPlaceDataSource.getInstance(this)
            val scheduleStrategy = DefaultScheduleStrategy()
            val viewModel = PlaceListViewModel(itemDataSource, scheduleStrategy)
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

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return CountingIdlingResource(PlaceListActivity::class.java.name)
    }
}
