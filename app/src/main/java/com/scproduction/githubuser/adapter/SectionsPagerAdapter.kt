package com.scproduction.githubuser.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.scproduction.githubuser.fragments.FollowFragment

class SectionsPagerAdapter(private val tabList: Array<String>,
                           private val username: String,
                           fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = tabList.size

    override fun createFragment(position: Int): Fragment = FollowFragment.newInstance(username, tabList[position])

}