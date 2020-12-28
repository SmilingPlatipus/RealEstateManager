package com.openclassrooms.realestatemanager.db

import android.content.ContentValues
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activities.CreateEstateActivity
import com.openclassrooms.realestatemanager.activities.SearchActivity
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstatePhoto
import com.openclassrooms.realestatemanager.utils.ListOfEstatePhotoConverter
import com.openclassrooms.realestatemanager.utils.ListOfStringConverter
import com.openclassrooms.realestatemanager.utils.OffsetDateTimeConverter
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Database(entities = [Estate::class], version = 1)
@TypeConverters(OffsetDateTimeConverter::class, ListOfStringConverter::class, ListOfEstatePhotoConverter::class)
abstract class EstateDatabase : RoomDatabase() {
    abstract val estateDao: EstateDao

    companion object utils{
        fun provideDb(context: Context): EstateDatabase {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                    .withZone(ZoneOffset.UTC)
            return Room.databaseBuilder(context, EstateDatabase::class.java, "REM.db")
                    .allowMainThreadQueries()
                    .addCallback(object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            var address = listOf("143","Rue de la chartreuse","46000","Cahors")
                            var contentValues = ContentValues()
                            var checkboxesStatus = EnumSet.noneOf(CreateEstateActivity.NearbyServices::class.java)
                            var poiList : MutableList<String> = emptyList<String>().toMutableList()
                            var photoList: MutableList<EstatePhoto> = emptyList<EstatePhoto>().toMutableList()

                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.HOSPITAL)
                            checkboxesStatus.forEach {
                                poiList.add(it.name.toLowerCase())
                            }
                            var somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison1,"Juste un appart")
                            photoList.add(somePhoto)

                            contentValues.put("type", SearchActivity.EstateTypes.HOUSE.name.toLowerCase())
                            contentValues.put("price",120000.0)
                            contentValues.put("size",120f)
                            contentValues.put("rooms",3)
                            contentValues.put("description","Nice house in Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.FORSALE.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", "null")
                            contentValues.put("saler","Jean Michel")
                            contentValues.put("latitude",44.447996)
                            contentValues.put("longitude", 1.437470)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)
                            photoList.clear()

                            address = listOf("160","Rue des Cadourques","46000","Cahors")
                            somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison2,"Juste un appart")
                            photoList.add(somePhoto)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.SCHOOL)
                            checkboxesStatus.forEach {
                                poiList.add(it.name.toLowerCase())
                            }

                            contentValues.put("type", SearchActivity.EstateTypes.HOUSE.name.toLowerCase())
                            contentValues.put("price",300000.0)
                            contentValues.put("size",200f)
                            contentValues.put("rooms",5)
                            contentValues.put("description","Big house in the center of Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.FORSALE.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", "null")
                            contentValues.put("saler","Jean Michel")
                            contentValues.put("latitude",44.449799)
                            contentValues.put("longitude", 1.436512)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)

                            photoList.clear()

                            address = listOf("292","Rue Joachim Murat","46000","Cahors")
                            somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison3,"Juste un appart")
                            photoList.add(somePhoto)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.PARK)
                            checkboxesStatus.forEach {
                                poiList.add(it.name.toLowerCase())
                            }

                            contentValues.put("type", SearchActivity.EstateTypes.PENTHOUSE.name.toLowerCase())
                            contentValues.put("price",1000000.0)
                            contentValues.put("size",250f)
                            contentValues.put("rooms",8)
                            contentValues.put("description","Penthouse in the center of Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.SOLD.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saler","Bertrand")
                            contentValues.put("latitude",44.448392)
                            contentValues.put("longitude", 1.435320)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)

                            photoList.clear()

                            address = listOf("50","Avenue Edouard Herriot","46000","Cahors")
                            somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison4,"Juste un appart")
                            photoList.add(somePhoto)
                            poiList.clear()

                            contentValues.put("type", SearchActivity.EstateTypes.HOUSE.name.toLowerCase())
                            contentValues.put("price",100000.0)
                            contentValues.put("size",80f)
                            contentValues.put("rooms",3)
                            contentValues.put("description","House in the suburbs of Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.FORSALE.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", "null")
                            contentValues.put("saler","Bertrand")
                            contentValues.put("latitude",44.462911)
                            contentValues.put("longitude", 1.452225)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)
                            photoList.clear()

                            address = listOf("13","Hameau du Pouget","46090","Pradines")
                            somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison5,"Juste un appart")
                            photoList.add(somePhoto)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.SHOPS)
                            checkboxesStatus.forEach {
                                poiList.add(it.name.toLowerCase())
                            }

                            contentValues.put("type", SearchActivity.EstateTypes.HOUSE.name.toLowerCase())
                            contentValues.put("price",200000.0)
                            contentValues.put("size",130f)
                            contentValues.put("rooms",5)
                            contentValues.put("description","Nice house in the suburbs of Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.FORSALE.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", "null")
                            contentValues.put("saler","Bertrand")
                            contentValues.put("latitude",44.466383)
                            contentValues.put("longitude", 1.408581)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)
                            photoList.clear()
                            poiList.clear()

                            address = listOf("108","Rue des Augustins","46000","Cahors")
                            somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison6,"Juste un appart")
                            photoList.add(somePhoto)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.PARK)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.HOSPITAL)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.SCHOOL)
                            checkboxesStatus.add(CreateEstateActivity.NearbyServices.SHOPS)
                            checkboxesStatus.forEach {
                                poiList.add(it.name.toLowerCase())
                            }

                            contentValues.put("type", SearchActivity.EstateTypes.APARTMENT.name.toLowerCase())
                            contentValues.put("price",100000.0)
                            contentValues.put("size",70f)
                            contentValues.put("rooms",3)
                            contentValues.put("description","Appartment in Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.FORSALE.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", "null")
                            contentValues.put("saler","Jean Michel")
                            contentValues.put("latitude",44.450088)
                            contentValues.put("longitude", 1.438579)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)
                            photoList.clear()

                            address = listOf("382","Rue Président Wilson","46000","Cahors")
                            somePhoto = EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison7,"Juste un appart")
                            photoList.add(somePhoto)

                            contentValues.put("type", SearchActivity.EstateTypes.HOUSE.name.toLowerCase())
                            contentValues.put("price",260000.0)
                            contentValues.put("size",130f)
                            contentValues.put("rooms",5)
                            contentValues.put("description","House in the center of Cahors")
                            contentValues.put("poi", Gson().toJson(poiList))
                            contentValues.put("photosUriWithDescriptions", Gson().toJson(photoList))
                            contentValues.put("address", Gson().toJson(address))
                            contentValues.put("status", SearchActivity.SearchStatus.SOLD.name.toLowerCase())
                            contentValues.put("creationDate", OffsetDateTime.now().format(formatter))
                            contentValues.put("saleDate", "null")
                            contentValues.put("saler","Jean Michel")
                            contentValues.put("latitude",44.445850)
                            contentValues.put("longitude", 1.436329)

                            db.insert("Estate", OnConflictStrategy.IGNORE,contentValues)
                        }

                    })
                    .build()
        }
    }
}
