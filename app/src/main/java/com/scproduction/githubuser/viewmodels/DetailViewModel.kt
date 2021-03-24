package com.scproduction.githubuser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.scproduction.githubuser.database.UserGithubDatabase
import com.scproduction.githubuser.model.UserGithub
import com.scproduction.githubuser.repositories.DetailRepository
import com.scproduction.githubuser.utils.Result
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val detailRepository: DetailRepository

    init {
        val favoriteUserDao = UserGithubDatabase.getInstance(application).userGithubDao
        detailRepository = DetailRepository(favoriteUserDao)
    }

    fun detailUser(username: String): LiveData<Result<UserGithub?>> = detailRepository.detailUser(username)

    fun addFavorite(userGithub: UserGithub) = viewModelScope.launch {
        detailRepository.addFavorite(userGithub)
    }

    fun deleteFavorite(userGithub: UserGithub) = viewModelScope.launch {
        detailRepository.deleteFavorite(userGithub)
    }

    val checkFavorite: LiveData<Boolean> = detailRepository.isFavorite
}