package com.example.valacuz.mylocations.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.valacuz.mylocations.MainApplication
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.ViewModelHolder
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.domain.display.MapDisplaySource
import com.example.valacuz.mylocations.domain.share.ShareContentSource
import com.example.valacuz.mylocations.form.PlaceFormActivity
import com.example.valacuz.mylocations.util.CountingIdlingResource
import com.example.valacuz.mylocations.util.schedulers.SchedulerProvider
import javax.inject.Inject

class PlaceListActivity : AppCompatActivity(), PlaceNavigator, PlaceItemNavigator {

    @Inject
    lateinit var placeDataSource: PlaceDataSource

    @Inject
    lateinit var mapDisplaySource: MapDisplaySource

    @Inject
    lateinit var shareContentSource: ShareContentSource

    private lateinit var viewModel: PlaceListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)
        setupToolbar()

        (application as MainApplication).placeComponent.inject(this)

        viewModel = findOrCreateViewModel()
        viewModel.setNavigator(this)

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
        AlertDialog.Builder(this)
                .setTitle(R.string.select_action)
                .setItems(R.array.item_choices, { _, position ->
                    when (position) {
                        0 -> mapDisplaySource.displayOnMap(place.latitude, place.longitude)
                        1 -> shareContentSource.shareContent(place.name
                                ?: "", place.latitude, place.longitude)
                        2 -> viewModel.onDeletePlaceClick(place)
                    }
                }).show()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun findOrCreateViewModel(): PlaceListViewModel {
        @Suppress("UNCHECKED_CAST")
        val holder = supportFragmentManager
                .findFragmentByTag(VIEW_MODEL_TAG) as ViewModelHolder<PlaceListViewModel>?
        return if (holder?.getViewModel() != null) {
            // If the ViewModel was retained, return it.
            holder.getViewModel()!!
        } else {
            // If there no ViewModel yet, create it.
            val schedulerProvider = SchedulerProvider()
            val viewModel = PlaceListViewModel(placeDataSource, schedulerProvider)
            supportFragmentManager
                    .beginTransaction()
                    .add(ViewModelHolder<PlaceListViewModel>()
                            .createContainer(viewModel), VIEW_MODEL_TAG)
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

    companion object {
        private const val VIEW_MODEL_TAG = "LIST_VM_TAG"
        private const val REQUEST_FORM = 2001
    }
}
