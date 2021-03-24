package com.scproduction.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "userGithub")
@Parcelize
data class UserGithub (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "avatar_url")
    val avatar_url: String,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "type")
    val type: String?,
    @ColumnInfo(name = "company")
    val company: String?,
    @ColumnInfo(name = "html_url")
    val html_url: String,
    @ColumnInfo(name = "public_repos")
    val public_repos: Int,
    @ColumnInfo(name = "followers")
    val followers: Int,
    @ColumnInfo(name = "following")
    val following: Int
) : Parcelable

@Parcelize
data class SearchResponse(
    val total_count : Int,
    val incomplete_results: Boolean? = null,
    val items : List<UserGithub>
): Parcelable