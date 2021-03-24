package com.scproduction.githubuser.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.scproduction.githubuser.R
import com.scproduction.githubuser.adapter.UserGithubAdapter
import com.scproduction.githubuser.utils.Status
import com.scproduction.githubuser.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var userGithubAdapter: UserGithubAdapter
    private val searchViewModel: SearchViewModel by navGraphViewModels(R.id.main_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initRecycleView()
        sv_user.setOnQueryTextListener(this)
        observeSearchUser()
    }

    private fun initRecycleView() {
        userGithubAdapter = UserGithubAdapter(arrayListOf()) { username, v ->
            findNavController().navigate(SearchFragmentDirections.detailsAction(username),
                FragmentNavigatorExtras(
                    v to username
                )
            )
        }
        rv_user.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_user.adapter = userGithubAdapter
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchViewModel.setQuery(query)
        sv_user.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    private fun observeSearchUser() {
        searchViewModel.searchResult.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when(result.status) {
                    Status.LOADING -> showLayout(Status.LOADING)
                    Status.SUCCESS -> {
                        result.data?.let { users ->
                            if (users.isNullOrEmpty()) {
                                showLayout(Status.ERROR, resources.getString(R.string.title_not_found))
                            } else {
                                showLayout(Status.SUCCESS)
                                userGithubAdapter.addAll(users)
                            }
                        }
                    }
                    Status.ERROR -> showLayout(Status.ERROR, resources.getString(R.string.title_no_internet))
                }
            }
        })
    }

    private fun showLayout(status: Status, message: String? = null) {
        when (status) {
            Status.LOADING -> {
                include_error.visibility = View.GONE
                rv_user.visibility = View.GONE
                progress_animation.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                include_error.visibility = View.GONE
                progress_animation.visibility = View.GONE
                rv_user.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                progress_animation.visibility = View.GONE
                rv_user.visibility = View.GONE
                include_error.visibility = View.VISIBLE

                if(message == resources.getString(R.string.title_no_internet)) {
                    include_error.no_data_animation.setAnimation(R.raw.no_internet)
                    include_error.no_data_animation.playAnimation()
                    include_error.title.text = message
                    include_error.desc.text = resources.getString(R.string.desc_no_internet)

                } else {
                    include_error.no_data_animation.setAnimation(R.raw.find)
                    include_error.no_data_animation.playAnimation()
                    include_error.title.text = message
                    include_error.desc.text = resources.getString(R.string.desc_not_found)
                }
            }
        }
    }

}