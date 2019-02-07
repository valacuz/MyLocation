package com.example.valacuz.mylocations.form

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.valacuz.mylocations.MainApplication
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.ViewModelHolder
import com.example.valacuz.mylocations.data.repository.PlaceDataSource
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.picker.PlacePickerActivity
import com.example.valacuz.mylocations.util.CountingIdlingResource
import com.example.valacuz.mylocations.util.schedulers.SchedulerProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PlaceFormActivity : AppCompatActivity(), PlaceFormNavigator {

    @Inject
    lateinit var placeDataSource: PlaceDataSource

    @Inject
    lateinit var placeTypeDataSource: PlaceTypeDataSource

    private lateinit var mViewModel: PlaceFormViewModel

    private var mPicturePath: String? = null

    private var mPlaceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_form)

        (application as MainApplication).placeComponent.inject(this)

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
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_LOCATION -> {
                    data?.extras?.run {
                        mViewModel.setCoordinate(
                                lat = getDouble("LATITUDE", 0.0),
                                lon = getDouble("LONGITUDE", 0.0))
                    }
                }
                REQUEST_TAKE_PICTURE -> {
                    mPicturePath?.let {
                        // Binding picture
                        mViewModel.picturePath.set(it)
                        // Notify picture was added.
                        broadcastNewPicture(it)
                    }
                }
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

    override fun dispatchToPlaceList() {
        // Assume add or edit operation is successfully.
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun dispatchPickLocation(latitude: Double?, longitude: Double?) {
        val intent = Intent(PlaceFormActivity@ this, PlacePickerActivity::class.java)
                .putExtra("LATITUDE", mViewModel.latitude.get())
                .putExtra("LONGITUDE", mViewModel.longitude.get())
        startActivityForResult(intent, REQUEST_PICK_LOCATION)
    }

    override fun dispatchTakePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val file = createImageFile()
            if (file != null) {
                mPicturePath = file.absolutePath
                val photoUri = FileProvider.getUriForFile(PlaceFormActivity@ this,
                        getString(R.string.picture_authorities), file)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PICTURE)
            }
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.ENGLISH).format(Date())
        val imageFileName = "IMAGE_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun broadcastNewPicture(picturePath: String) {
        val file = File(picturePath)
        if (file.exists()) {
            val uri = Uri.fromFile(file)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = uri
            sendBroadcast(intent)
        }
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
        return if (holder?.getViewModel() != null) {
            // If the ViewModel was retained, return it.
            holder.getViewModel()!!
        } else {
            val schedulerProvider = SchedulerProvider()
            val messageProvider = ResourcePlaceFormMessageProvider(this)
            val viewModel = PlaceFormViewModel(messageProvider, placeDataSource, placeTypeDataSource,
                    schedulerProvider, mPlaceId)
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
        private const val REQUEST_TAKE_PICTURE = 1002
    }
}
