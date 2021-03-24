package com.scproduction.githubuser.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.scproduction.githubuser.R
import com.scproduction.githubuser.adapter.UserGithubAdapter
import com.scproduction.githubuser.utils.Status
import com.scproduction.githubuser.utils.TypeFollow
import com.scproduction.githubuser.viewmodels.FollowViewModel
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.android.synthetic.main.fragment_follow.*
import kotlinx.android.synthetic.main.fragment_follow.include_error
import kotlinx.android.synthetic.main.fragment_search.*

class FollowFragment : Fragment() {

    companion object {
        fun newInstance(username: String, type: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                    putString(TYPE, type)
                }
            }

        private const val USERNAME = "username"
        private const val TYPE = "type"
    }

    private lateinit var userGithubAdapter: UserGithubAdapter
    private lateinit var followViewModel: FollowViewModel
    private lateinit var username: String
    private var typeFollow: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            username = it.getString(USERNAME).toString()
            typeFollow = it.getString(TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView()

        followViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        when(typeFollow) {
            resources.getString(R.string.following) -> followViewModel.setFollow(username, TypeFollow.FOLLOWING)

            resources.getString(R.string.followers) -> followViewModel.setFollow(username, TypeFollow.FOLLOWER)

            else -> showLayout(Status.ERROR)
        }

        observeFollow()
    }

    private fun initRecycleView() {
        userGithubAdapter = UserGithubAdapter(arrayListOf()) { username, v ->
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentSelf(username),
                FragmentNavigatorExtras(
                    v to username
                )
            )
        }
        rv_follow.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_follow.adapter = userGithubAdapter
    }

    private fun observeFollow() {
        followViewModel.dataFollow.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    include_error.visibility = View.GONE
                    rv_follow.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    include_error.visibility = View.GONE
                    rv_follow.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    include_error.visibility = View.GONE
                    rv_follow.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showLayout(status: Status, message: String? = null) {
        when (status) {
            Status.LOADING -> {
                include_error.visibility = View.GONE
                rv_follow.visibility = View.GONE
                progress.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                include_error.visibility = View.GONE
                progress.visibility = View.GONE
                rv_follow.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                progress.visibility = View.GONE
                rv_follow.visibility = View.GONE
                include_error.visibility = View.VISIBLE
                if(message == resources.getString(R.string.title_no_internet)) {
                    include_error.no_data_animation.setAnimation(R.raw.no_internet)
                    include_error.no_data_animation.playAnimation()
                    include_error.title.text = message
                    include_error.desc.text = resources.getString(R.string.desc_no_internet)

                } else {
                    include_error.desc.visibility = View.GONE
                    include_error.no_data_animation.setAnimation(R.raw.find)
                    include_error.no_data_animation.playAnimation()
                    include_error.title.text = message
                }
            }
        }
    }
}