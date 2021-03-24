package com.scproduction.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.scproduction.githubuser.model.UserGithub
import com.scproduction.githubuser.repositories.FollowRepository
import com.scproduction.githubuser.utils.Result
import com.scproduction.githubuser.utils.TypeFollow

class FollowViewModel : ViewModel() {

    private val username: MutableLiveData<String> = MutableLiveData()
    private lateinit var type: TypeFollow

    val dataFollow: LiveData<Result<List<UserGithub>?>> = Transformations
        .switchMap(username) {
            when (type) {
                TypeFollow.FOLLOWER -> FollowRepository.getFollowers(it)

                TypeFollow.FOLLOWING -> FollowRepository.getFollowing(it)
            }
        }

    fun setFollow(user: String, typeView: TypeFollow) {
        if (username.value != user) {
            username.value = user
            type = typeView
        }
    }
}
