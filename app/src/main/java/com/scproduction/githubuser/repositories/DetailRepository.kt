package com.scproduction.githubuser.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.scproduction.githubuser.dao.UserGithubDao
import com.scproduction.githubuser.model.UserGithub
import com.scproduction.githubuser.network.ApiClient
import com.scproduction.githubuser.utils.Result
import kotlinx.coroutines.Dispatchers

class DetailRepository(private val userGithubDao: UserGithubDao) {

    private val favorite: MutableLiveData<Boolean> = MutableLiveData()

    fun detailUser(username: String) = liveData(Dispatchers.IO) {
        emit(Result.loading(null))

        val user = userGithubDao.getUserDetail(username)

        if (user != null){
            favorite.postValue(true)
            emit(Result.success(user))
        } else {
            favorite.postValue(false)
            try {
                emit(Result.success(ApiClient.service.userDetail(username)))
            } catch (e: Exception){
                emit(Result.error(null))
            }
        }
    }

    suspend fun addFavorite(userGithub: UserGithub){
        userGithubDao.insertFavoriteUser(userGithub)
        favorite.value = true
    }

    suspend fun deleteFavorite(userGithub: UserGithub){
        userGithubDao.deleteFavoriteUser(userGithub)
        favorite.value = false
    }

    val isFavorite: LiveData<Boolean> = favorite
}