package com.github.admitrevskiy.githubauth.repository

import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import io.reactivex.Single

/**
 * Repository logic to get access to github.com user's repositories
 */
interface GitHubRepository {

    /**
     * Authorizes GitHub.com user and returns Single with list of user's repositories
     *
     * @param username Username
     * @param password Password
     * @param otp      One Time Password (2FA)
     */
    fun getRepos(username: String,
                 password: String,
                 otp: String? = null) : Single<List<GitHubRepo>>

    /**
     * Triggers 2FA OTP sending
     */
    fun trigger2FAOTP(username: String, password: String) : Single<Void>
}