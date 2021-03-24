package com.scproduction.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scproduction.githubuser.dao.UserGithubDao
import com.scproduction.githubuser.model.UserGithub

@Database(entities = [UserGithub::class], version = 1, exportSchema = false)
abstract class UserGithubDatabase : RoomDatabase() {

    abstract val userGithubDao: UserGithubDao

    companion object {

        @Volatile
        private var INSTANCE: UserGithubDatabase? = null

        fun getInstance(context: Context): UserGithubDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserGithubDatabase::class.java,
                        "github_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}