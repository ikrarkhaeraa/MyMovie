package com.example.core.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.dataStore.LocalDatabase
import com.example.core.paging.PagingSource
import com.example.core.retrofit.ApiService
import com.example.core.retrofit.response.Movies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val localDb : LocalDatabase, private val apiService: ApiService){

    fun getMovieList(): Flow<PagingData<Movies>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                PagingSource(apiService, localDb)
            }
        ).flow
    }
    suspend fun setUserLoginState() {
        localDb.setUserLoginState()
    }

    fun getUserLoginState(): Flow<Boolean> {
        return localDb.getUserLoginState()
    }

    suspend fun userLogout() {
        localDb.logout()
    }

}