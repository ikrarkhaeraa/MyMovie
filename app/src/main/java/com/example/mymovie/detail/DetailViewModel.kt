package com.example.mymovie.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.SealedClass
import com.example.core.dataStore.LocalDatabase
import com.example.core.repository.Repository
import com.example.core.retrofit.response.DetailMovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val localDb: LocalDatabase, private val repository: Repository) : ViewModel() {

    private val _movieDetail = MutableStateFlow<SealedClass<DetailMovieResponse>>(SealedClass.Init)
    val movieDetail = _movieDetail

    fun getDetailProductData(id: Int) = viewModelScope.launch {
        val token = localDb.getAccessToken().first()
        val auth = "Bearer $token"

        _movieDetail.emit(SealedClass.Loading)

        repository.getMovieDetail(auth, id).catch {
            _movieDetail.emit(SealedClass.Error(it))
        }.collect {
            _movieDetail.emit(SealedClass.Success(it))
        }
    }

}