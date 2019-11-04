package com.github.admitrevskiy.githubauth.repository

import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import com.github.admitrevskiy.githubauth.model.rest.GitHubApi
import io.reactivex.Single
import okhttp3.Credentials

class GitHubRepositoryImpl(private val api: GitHubApi) : GitHubRepository {

    override fun getRepos(
        username: String,
        password: String,
        otp: String?
    ): Single<List<GitHubRepo>> = otp?.let {
        api.getRepos(Credentials.basic(username, password), otp)
    } ?: api.getRepos(Credentials.basic(username, password))

    override fun trigger2FAOTP(username: String, password: String): Single<Void> =
        api.trigger2FA(Credentials.basic(username, password))
}