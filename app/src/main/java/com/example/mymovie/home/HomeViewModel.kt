package com.example.mymovie.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.repository.Repository
import com.example.core.retrofit.response.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: Repository): ViewModel() {

    val getMovieList: () -> Flow<PagingData<Movies>> = {
        Log.d("cekViewModel", "keHitViewModelnya")
        repo.getMovieList().cachedIn(viewModelScope)
    }

    fun getUserLoginState(): Flow<Boolean> {
        return repo.getUserLoginState()
    }

    fun userLogout() {
        viewModelScope.launch {
            repo.userLogout()
        }
    }

}