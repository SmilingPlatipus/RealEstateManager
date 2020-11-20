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

                        var poi = listOf("school","hospital")
                        var address = listOf("143","Rue de la chartreuse","46000","Cahors")
                        var contentValues = ContentValues()

                        contentValues.put("type","appartment")
                        contentValues.put("price",150000.0)
                        contentValues.put("size",120f)
                        contentValues.put("rooms",3)
                        contentValues.put("description","Nice apartment on Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison1)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Jean Michel")
                        contentValues.put("latitude",44.447996)
                        contentValues.put("longitude", 1.437470)

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)


                        poi = listOf("park","shops")
                        address = listOf("160","Rue des Cadourques","46000","Cahors")

                        contentValues.put("type","house")
                        contentValues.put("price",300000.0)
                        contentValues.put("size",200f)
                        contentValues.put("rooms",5)
                        contentValues.put("description","Big house in the center of Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison2)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Jean Michel")
                        contentValues.put("latitude",44.449799)
                        contentValues.put("longitude", 1.436512)

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)

                        poi = listOf("park","shops","hospital","shool")
                        address = listOf("292","Rue Joachim Murat","46000","Cahors")

                        contentValues.put("type","penthouse")
                        contentValues.put("price",1000000.0)
                        contentValues.put("size",250f)
                        contentValues.put("rooms",8)
                        contentValues.put("description","Penthouse in the center of Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison3)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","sold")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Bertrand")
                        contentValues.put("latitude",44.448392)
                        contentValues.put("longitude", 1.435320)

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)

                        poi = listOf("park")
                        address = listOf("50","Avenue Edouard Herriot","46000","Cahors")

                        contentValues.put("type","house")
                        contentValues.put("price",100000.0)
                        contentValues.put("size",80f)
                        contentValues.put("rooms",3)
                        contentValues.put("description","House in the suburbs of Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison4)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Bertrand")
                        contentValues.put("latitude",44.462911)
                        contentValues.put("longitude", 1.452225)

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)

                        poi = listOf("park","shops")
                        address = listOf("13","Hameau du Pouget","46090","Pradines")

                        contentValues.put("type","house")
                        contentValues.put("price",200000.0)
                        contentValues.put("size",130f)
                        contentValues.put("rooms",5)
                        contentValues.put("description","Nice house in the suburbs of Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison5)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Bertrand")
                        contentValues.put("latitude",44.466383)
                        contentValues.put("longitude", 1.408581)

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)

                        poi = listOf("park","shops","hospital","shool")
                        address = listOf("108","Rue des Augustins","46000","Cahors")

                        contentValues.put("type","appartment")
                        contentValues.put("price",100000.0)
                        contentValues.put("size",70f)
                        contentValues.put("rooms",3)
                        contentValues.put("description","Appartment in Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison6)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Jean Michel")
                        contentValues.put("latitude",44.450088)
                        contentValues.put("longitude", 1.438579)

                        db.insert("Estate",OnConflictStrategy.IGNORE,contentValues)

                        poi = listOf("hospital","shool")
                        address = listOf("382","Rue Président Wilson","46000","Cahors")

                        contentValues.put("type","house")
                        contentValues.put("price",250000.0)
                        contentValues.put("size",130f)
                        contentValues.put("rooms",5)
                        contentValues.put("description","House in the center of Cahors")
                        contentValues.put("poi", Gson().toJson(poi))
                        contentValues.put("photoUri", "android.resource://com.openclassrooms.realestatemanager/" + R.drawable.maison7)
                        contentValues.put("address",Gson().toJson(address))
                        contentValues.put("status","for sale")
                        contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                        contentValues.put("dealer","Jean Michel")
                        contentValues.put("latitude",44.445850)
                        contentValues.put("longitude", 1.436329)

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