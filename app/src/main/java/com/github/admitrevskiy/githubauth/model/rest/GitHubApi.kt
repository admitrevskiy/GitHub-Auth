package com.github.admitrevskiy.githubauth.model.rest

import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * GitHub API
 */
interface GitHubApi {

    /**
     * Gets List of repositories
     */
    @GET("user/repos?per_page=100")
    fun getRepos(@Header("Authorization") token: String) : Single<List<GitHubRepo>>

    /**
     * Gets List of repositories with 2FA OTP
     */
    @GET("user/repos?per_page=100")
    fun getRepos(@Header("Authorization") token: String, @Header("x-github-otp") otp: String) : Single<List<GitHubRepo>>

    /**
     * Triggers 2FA code sending
     */
    @POST("authorizations")
    fun trigger2FA(@Header("Authorization") token: String) : Single<Void>

}