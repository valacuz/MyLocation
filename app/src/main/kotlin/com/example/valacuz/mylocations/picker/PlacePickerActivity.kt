package com.example.valacuz.mylocations.picker

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.ViewModelHolder
import com.example.valacuz.mylocations.data.repository.FusedLocationSource

class PlacePickerActivity : AppCompatActivity(), PlacePickerNavigator {

    private lateinit var viewModel: PlacePickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_picker)
        setupToolbar()

        viewModel = findOrCreateViewModel()
        viewModel.setNavigator(this)

        val fragment: PlacePickerFragment = findOrCreateFragment()
        fragment.setViewModel(viewModel)

        intent?.extras?.let {
            viewModel.setCenterLocation(
                    latitude = it.getDouble("LATITUDE", 0.0),
                    longitude = it.getDouble("LONGITUDE", 0.0))
        }
    }

    override fun onDestroy() {
        viewModel.onActivityDestroyed()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun goBackToForm(latitude: Double, longitude: Double) {
        val intent = Intent()
                .putExtra("LATITUDE", latitude)
                .putExtra("LONGITUDE", longitude)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.coordinate_title)
        }
    }

    private fun findOrCreateViewModel(): PlacePickerViewModel {
        @Suppress("UNCHECKED_CAST")
        val holder: ViewModelHolder<PlacePickerViewModel>? = supportFragmentManager
                .findFragmentByTag(VIEW_MODEL_TAG) as ViewModelHolder<PlacePickerViewModel>?
        return if (holder != null) {
            // If the ViewModel was retained, return it.
            holder.getViewModel()!!
        } else {
            // If there no ViewModel yet, create it.
            val locationSource = FusedLocationSource.getInstance(this)
            val viewModel = PlacePickerViewModel(locationSource)
            supportFragmentManager
                    .beginTransaction()
                    .add(ViewModelHolder<PlacePickerViewModel>().createContainer(viewModel),
                            VIEW_MODEL_TAG)
                    .commit()
            // return view model
            viewModel
        }
    }

    private fun findOrCreateFragment(): PlacePickerFragment {
        var fragment: PlacePickerFragment? = supportFragmentManager
                .findFragmentById(R.id.frame) as PlacePickerFragment?
        if (fragment == null) {
            fragment = PlacePickerFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame, fragment)
                    .commit()
        }
        return fragment
    }

    companion object {

        private const val VIEW_MODEL_TAG = "MAP_VM_TAG"
    }
}
