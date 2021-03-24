package com.scproduction.githubuser.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.scproduction.githubuser.network.ApiClient
import com.scproduction.githubuser.utils.Result
import kotlinx.coroutines.Dispatchers

object SearchRepository {

    fun searchUsers(q: String) = liveData(Dispatchers.IO) {
        emit(Result.loading(null))
        try {
            val users = ApiClient.service.searchUsers(q)
            emit(Result.success(users.items))
        } catch (e: Exception) {
            emit(Result.error(null))
        }
    }
}