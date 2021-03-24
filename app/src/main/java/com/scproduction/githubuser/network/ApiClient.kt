package com.scproduction.githubuser.network

import com.scproduction.githubuser.BuildConfig.API_KEY
import com.scproduction.githubuser.model.SearchResponse
import com.scproduction.githubuser.model.UserGithub
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private val client by lazy {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("Authorization", API_KEY)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        private val retrofitBuilder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .client(client)
        }

        val service: ApiService by lazy {
            retrofitBuilder.build().create(ApiService::class.java)
        }
    }
}

interface ApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") q: String?
    ): SearchResponse

    @GET("users/{username}")
    suspend fun userDetail(
        @Path("username") username: String?
    ): UserGithub

    @GET("users/{username}/followers")
    suspend fun userFollower(
        @Path("username") username: String?
    ): List<UserGithub>

    @GET("users/{username}/following")
    suspend fun userFollowing(
        @Path("username") username: String?
    ): List<UserGithub>

}