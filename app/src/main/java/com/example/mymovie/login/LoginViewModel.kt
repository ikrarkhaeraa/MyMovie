package com.example.mymovie.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: Repository): ViewModel() {

    fun setUserLoginState() {
        viewModelScope.launch {
            repo.setUserLoginState()
        }
    }


}