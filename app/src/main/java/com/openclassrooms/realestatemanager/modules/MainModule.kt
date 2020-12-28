package com.openclassrooms.realestatemanager.modules

import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.db.EstateDatabase
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.db.EstateDatabase.utils.provideDb
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import com.openclassrooms.realestatemanager.viewModels.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


@JvmField
val mainModule = module {
    viewModel { EstateViewModel(get())}
    viewModel { DetailViewModel(get())}
    viewModel { SearchViewModel(get())}


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