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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(private val localDb : LocalDatabase, private val apiService: ApiService){

    fun getMovieDetail(auth: String, id: Int) = flow {
        emit(apiService.getMovieDetail(auth, id))
    }

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

    suspend fun setUsername(userName: String) {
        localDb.setUsername(userName)
    }

    fun getUsername(): Flow<String> {
        return localDb.getUsername()
    }

    suspend fun userLogout() {
        localDb.logout()
    }

}