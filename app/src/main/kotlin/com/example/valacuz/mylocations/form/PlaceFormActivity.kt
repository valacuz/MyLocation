package com.example.valacuz.mylocations.form

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.ViewModelHolder
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.room.RoomPlaceDataSource
import com.example.valacuz.mylocations.di.MainApplication
import com.example.valacuz.mylocations.picker.PlacePickerActivity
import com.example.valacuz.mylocations.util.DefaultScheduleStrategy
import javax.inject.Inject

class PlaceFormActivity : AppCompatActivity(), PlaceFormNavigator {

    @Inject
    lateinit var placeTypeDataSource: PlaceTypeDataSource

    private lateinit var mViewModel: PlaceFormViewModel

    private var mPlaceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_form)

        (application as MainApplication).placeTypeComponent.inject(this)

        mPlaceId = intent?.extras?.getString("PLACE_ID")
        setupToolbar()

        mViewModel = findOrCreateViewModel()
        mViewModel.setNavigator(this)

        val fragment: PlaceFormFragment = findOrCreateFragment()
        fragment.setViewModel(mViewModel)
    }

    override fun onDestroy() {
        mViewModel.onActivityDestroyed()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK_LOCATION) {
            data?.extras?.let {
                mViewModel.setCoordinate(
                        lat = it.getDouble("LATITUDE", 0.0),
                        lon = it.getDouble("LONGITUDE", 0.0))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.place_form_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_action_save -> {
                mViewModel.saveButtonClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun goBackToList() {
        // Assume add or edit operation is successfully.
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun goPickLocation(latitude: Double?, longitude: Double?) {
        val intent = Intent(PlaceFormActivity@ this, PlacePickerActivity::class.java)
                .putExtra("LATITUDE", mViewModel.latitude.get())
                .putExtra("LONGITUDE", mViewModel.longitude.get())
        startActivityForResult(intent, REQUEST_PICK_LOCATION)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = if (mPlaceId == null) {
                getString(R.string.form_title_add)
            } else {
                getString(R.string.form_title_edit)
            }
        }
    }

    private fun findOrCreateViewModel(): PlaceFormViewModel {
        @Suppress("UNCHECKED_CAST")
        val holder: ViewModelHolder<PlaceFormViewModel>? = supportFragmentManager
                .findFragmentByTag(VIEW_MODEL_TAG) as ViewModelHolder<PlaceFormViewModel>?
        return if (holder != null) {
            // If the ViewModel was retained, return it.
            holder.getViewModel()!!
        } else {
            val itemDataSource = RoomPlaceDataSource.getInstance(this)
            val scheduleStrategy = DefaultScheduleStrategy()
            val viewModel = PlaceFormViewModel(this, itemDataSource, placeTypeDataSource,
                    scheduleStrategy, mPlaceId)
            supportFragmentManager
                    .beginTransaction()
                    .add(ViewModelHolder<PlaceFormViewModel>().createContainer(viewModel),
                            VIEW_MODEL_TAG)
                    .commit()
            // return view model.
            viewModel
        }
    }

    private fun findOrCreateFragment(): PlaceFormFragment {
        var fragment: PlaceFormFragment? = supportFragmentManager
                .findFragmentById(R.id.frame) as PlaceFormFragment?
        if (fragment == null) {
            fragment = PlaceFormFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame, fragment)
                    .commit()
        }
        return fragment
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return CountingIdlingResource(PlaceFormActivity::class.java.name)
    }

    companion object {
        private const val VIEW_MODEL_TAG = "FORM_VM_TAG"
        private const val REQUEST_PICK_LOCATION = 1001
    }
}
