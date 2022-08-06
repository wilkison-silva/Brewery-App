package com.ciandt.ciandtbrewery.di

import android.content.Context
import androidx.room.Room
import com.ciandt.ciandtbrewery.BuildConfig
import com.ciandt.ciandtbrewery.database.AppDatabase

import com.ciandt.ciandtbrewery.database.AppDatabase.Companion.DATABASE_NAME
import com.ciandt.ciandtbrewery.database.dao.BreweryDao
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.domain.FavoriteUseCaseImpl
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.ui.viewmodel.*
import com.ciandt.ciandtbrewery.webclient.BreweryWebClient
import com.ciandt.ciandtbrewery.webclient.service.BreweryService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val URL_BASE = "https://bootcamp-mobile-01.eastus.cloudapp.azure.com"


val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

val daoModule = module {
    single<BreweryDao> { get<AppDatabase>().breweryDao() }
}

val retrofitModule = module {
    single<OkHttpClient> {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(Level.BASIC)
            client.addInterceptor(logging)
        }
        client.build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}

val serviceModule = module {
    single<BreweryService> { get<Retrofit>().create(BreweryService::class.java) }
}

val webClientModule = module {
    single<BreweryWebClient> { BreweryWebClient(get<BreweryService>()) }
}

val repositoryModule = module {
    single<BreweryRepository> { BreweryRepository(get<BreweryWebClient>(), get<BreweryDao>()) }
}

val viewModelModule = module {
    viewModel<HomeViewModel> { HomeViewModel(get<BreweryRepository>(),get()) }
    viewModel<BreweryDetailsViewModel> { BreweryDetailsViewModel(get<BreweryRepository>(), get()) }
    viewModel<EvaluationDialogViewModel> { EvaluationDialogViewModel(get<BreweryRepository>()) }
    viewModel<FavoriteBreweryViewModel> { FavoriteBreweryViewModel(get<BreweryRepository>(),get()) }
    viewModel<ChooseImageDialogViewModel> { ChooseImageDialogViewModel(get<BreweryRepository>()) }
    viewModel<EvaluatedViewModel> { EvaluatedViewModel(get<BreweryRepository>()) }


    factory<FavoriteUseCase> { FavoriteUseCaseImpl(get()) }
}



