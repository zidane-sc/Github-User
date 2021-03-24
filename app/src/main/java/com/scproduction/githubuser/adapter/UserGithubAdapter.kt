package com.scproduction.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.scproduction.githubuser.R
import com.scproduction.githubuser.model.UserGithub
import kotlinx.android.synthetic.main.item_row_user.view.*

class UserGithubAdapter(
    private val list: MutableList<UserGithub>? = mutableListOf(),
    private val onClick: (String, View) -> Unit
) : RecyclerView.Adapter<UserGithubAdapter.ViewHolder>() {

    fun addAll(items: List<UserGithub>?) {
        if (items != null) {
            list?.clear()
            list?.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserGithubAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(holder: UserGithubAdapter.ViewHolder, position: Int) {
        holder.bind(list?.get(position), onClick)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(userGithub: UserGithub?, onClick: (String, View) -> Unit) {
            with(itemView) {
                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.setColorSchemeColors(R.color.colorPrimary)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(context)
                    .load(userGithub?.avatar_url)
                    .circleCrop()
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.error)
                    .into(itemView.iv_avatar)
                itemView.tv_username.text = userGithub?.login
                itemView.tv_type.text = userGithub?.type
                itemView.tv_html_url.text = userGithub?.html_url
                itemView.transitionName = userGithub?.login
                itemView.setOnClickListener { onClick(userGithub!!.login, itemView) }
            }
        }
    }
}