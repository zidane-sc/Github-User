package com.scproduction.githubuser.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.scproduction.githubuser.model.UserGithub

@Dao
interface UserGithubDao {

    @Query("SELECT * FROM userGithub")
    fun getUserList(): LiveData<List<UserGithub>>

    @Query("SELECT * FROM userGithub WHERE login = :username")
    fun getUserDetail(username: String): UserGithub?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteUser(userGithub: UserGithub)

    @Delete
    suspend fun deleteFavoriteUser(userGithub: UserGithub): Int

    @Query("DELETE FROM userGithub")
    suspend fun deleteAllFavorite()

    @Query("SELECT * FROM userGithub ORDER BY login ASC")
    fun getUserListProvider(): Cursor

    @Query("SELECT * FROM userGithub ORDER BY login ASC")
    fun getWidgetList(): List<UserGithub>
}