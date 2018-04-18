package com.example.valacuz.mylocations.model

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.valacuz.mylocations.data.repository.PlaceTypeDataSource
import com.example.valacuz.mylocations.data.repository.realm.RealmPlaceTypeDataSource
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class RealmPlaceTypeDataSourceTest : PlaceTypeDataSourceTest() {

    override fun doPrepare() {
        Realm.init(InstrumentationRegistry.getTargetContext())

        // We cannot use in-memory database
        // because all data stored in memory was cleared when realm instance was closed
        // (we start and close realm instance in every operations)
        val config = RealmConfiguration.Builder()
                .name("my_location_test.realm")
                .build()
        Realm.setDefaultConfiguration(config)
    }

    override fun doCleanUp() {
        // Delete realm file on cleanup
        Realm.getDefaultConfiguration()?.let {
            val realmFile = File(it.path)
            if (realmFile.exists()) {
                realmFile.delete()
            }
        }
    }

    override fun getPlaceTypeDataSource(): PlaceTypeDataSource {
        return RealmPlaceTypeDataSource.getInstance(InstrumentationRegistry.getTargetContext())
    }
}