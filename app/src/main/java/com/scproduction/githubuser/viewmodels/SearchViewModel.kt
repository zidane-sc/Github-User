package com.scproduction.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.scproduction.githubuser.model.UserGithub
import com.scproduction.githubuser.repositories.SearchRepository
import com.scproduction.githubuser.utils.Result

class SearchViewModel() : ViewModel() {
    private val username: MutableLiveData<String> = MutableLiveData()

    fun setQuery(query: String) {
        if (username.value != query) {
            username.value = query
        }
    }

    val searchResult: LiveData<Result<List<UserGithub>?>> = Transformations
        .switchMap(username) {
            SearchRepository.searchUsers(it)
        }
}