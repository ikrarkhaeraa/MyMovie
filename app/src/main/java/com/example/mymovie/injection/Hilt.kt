package com.example.mymovie.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.core.dataStore.LocalDatabase
import com.example.core.repository.Repository
import com.example.core.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Hilt {

    private val Context.database: DataStore<Preferences> by preferencesDataStore("application")

    @Singleton
    @Provides
    fun provideDatabaseContext(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.database
    }

    @Singleton
    @Provides
    fun provideDataStore(dataStore: DataStore<Preferences>): LocalDatabase {
        return LocalDatabase(dataStore)
    }

    @Provides
    fun provideRepository(
        localDb: LocalDatabase,
        apiService: ApiService
    ): Repository {
        return Repository(localDb, apiService)
    }

    @Singleton
    @Provides
    fun provideChucker(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideApiService(context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

}