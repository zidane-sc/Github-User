package com.scproduction.githubuser.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.scproduction.githubuser.R
import com.scproduction.githubuser.adapter.SectionsPagerAdapter
import com.scproduction.githubuser.model.UserGithub
import com.scproduction.githubuser.utils.Status
import com.scproduction.githubuser.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_row_user.*


class DetailFragment : Fragment() {

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userGithub: UserGithub
    private val detailFragmentArgs: DetailFragmentArgs by navArgs()
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeDetailUser()
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container_detail.transitionName = detailFragmentArgs.username
        fab_favorite.setOnClickListener { addOrRemoveFavorite(view) }

        val tabList = arrayOf(
            resources.getString(R.string.followers),
            resources.getString(R.string.following)
        )

        sectionsPagerAdapter = SectionsPagerAdapter(tabList, detailFragmentArgs.username, this)
        view_pager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun observeDetailUser() {
        detailViewModel.detailUser(detailFragmentArgs.username).observe(viewLifecycleOwner, Observer {
            if(it.status == Status.SUCCESS){
                userGithub = it.data!!
                initDetailUser(userGithub)
            }
        })

        detailViewModel.checkFavorite.observe(viewLifecycleOwner, Observer { fav ->
            isFavorite = fav
            changeFavorite(fav)
        })
    }

    private fun initDetailUser(userGithub: UserGithub) {
        Glide.with(requireContext())
            .load(userGithub.avatar_url)
            .circleCrop()
            .placeholder(R.drawable.error)
            .error(R.drawable.error)
            .into(avatar)
        username.text = userGithub.login
        tv_followers.text = userGithub.followers.toString()
        tv_following.text = userGithub.following.toString()
        tv_repository.text = userGithub.public_repos.toString()
        tv_name.text = userGithub.name
        tv_company.text = userGithub.company
        tv_location.text = userGithub.location
        tv_detail_html_url.text = userGithub.html_url
    }

    private fun addOrRemoveFavorite(view: View) {
        if (!isFavorite){
            detailViewModel.addFavorite(userGithub)

            Snackbar.make(view, resources.getString(R.string.add_favorite, userGithub.login), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.add))
                .setAction(R.string.cancel){
                    detailViewModel.deleteFavorite(userGithub)
                }.show()
        } else {
            detailViewModel.deleteFavorite(userGithub)

            Snackbar.make(view, resources.getString(R.string.remove_favorite, userGithub.login), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.remove))
                .setAction(R.string.cancel){
                    detailViewModel.addFavorite(userGithub)
                }.show()
        }
    }

    private fun changeFavorite(fav: Boolean) {
        if (fav){
            fab_favorite.setImageResource(R.drawable.favorite)
        } else {
            fab_favorite.setImageResource((R.drawable.unfavorite))
        }
    }
}