package com.scproduction.githubuser.repositories

import androidx.lifecycle.liveData
import com.scproduction.githubuser.network.ApiClient
import com.scproduction.githubuser.utils.Result
import kotlinx.coroutines.Dispatchers

object FollowRepository {

    fun getFollowers(username: String) = liveData(Dispatchers.IO) {
        emit(Result.loading(null))
        try {
            emit(Result.success(ApiClient.service.userFollower(username)))
        } catch (e: Exception) {
            emit(Result.error(null))
        }
    }

    fun getFollowing(username: String) = liveData(Dispatchers.IO) {
        emit(Result.loading(null))
        try {
            emit(Result.success(ApiClient.service.userFollowing(username)))
        } catch (e: Exception) {
            emit(Result.error(null))
        }
    }

}