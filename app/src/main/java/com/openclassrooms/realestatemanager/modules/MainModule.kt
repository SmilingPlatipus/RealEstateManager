package com.openclassrooms.realestatemanager.modules

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.db.EstateDatabase
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.utils.OffsetDateTimeConverter
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@JvmField
val mainModule = module {
    viewModel { EstateViewModel(get())}

    fun provideDb(context: Context): EstateDatabase {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                .withZone(ZoneOffset.UTC)
        return Room.databaseBuilder(context, EstateDatabase::class.java, "REM.db")
                .allowMainThreadQueries()
                .addCallback(object: RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val poi = listOf("school","hospital","college")
                        var contentValues = ContentValues()

                        contentValues.put("type","appartment")
                        contentValues.put("price",15000.0)
                        contentValues.put("size",120f)
                        contentValues.put("rooms",5)
                        contentValues.put("description","Nice apartment on Long Island")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison1)
                        contentValues.put("address","some address")
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Jean Michel")
                        contentValues.put("latitude","null")
                        contentValues.put("longitude", "null")

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)
                    }

                })
                .build()
    }

    fun provideDao(database: EstateDatabase): EstateDao {
        return database.estateDao
    }

    fun provideEstateRepository(dao: EstateDao): EstateDataRepository {
        return EstateDataRepository(dao)
    }

    single { provideDb(androidContext()) }
    single { provideDao(get()) }
    single { provideEstateRepository(get()) }
}