package com.openclassrooms.realestatemanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Estate::class], version = 1)
abstract class EstateDatabase : RoomDatabase() {
    abstract fun estateDao(): EstateDao

    companion object {
        @Volatile
        var INSTANCE: EstateDatabase? = null

        fun getEstateDataBase(context: Context, scope: CoroutineScope): EstateDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        EstateDatabase::class.java,
                        "REM_database"
                ).addCallback(EstateDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class EstateDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.estateDao())
                }
            }
        }

        suspend fun populateDatabase(estateDao: EstateDao) {
            // Todo : insert some real estates examples here later on
        }
    }

}
