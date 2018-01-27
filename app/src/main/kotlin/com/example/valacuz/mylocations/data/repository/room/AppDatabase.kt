package com.example.valacuz.mylocations.data.repository.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.valacuz.mylocations.data.PlaceItem
import com.example.valacuz.mylocations.data.PlaceType
import java.util.concurrent.Executors


@Database(entities = [(PlaceItem::class), (PlaceType::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeItemDao(): PlaceDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "my_places.db"

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME)
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                // Insert pre-populate data in I/O thread
                                Executors.newSingleThreadExecutor().execute({
                                    getInstance(context)
                                            .placeItemDao()
                                            .addPlaceTypes(PRE_POPULATE_DATA)
                                })
                            }
                        })
                        .build()

        val PRE_POPULATE_DATA = listOf(
                PlaceType(1, "Education"),
                PlaceType(2, "Department store"),
                PlaceType(3, "Restaurant"),
                PlaceType(4, "Relaxation"))

    }
}