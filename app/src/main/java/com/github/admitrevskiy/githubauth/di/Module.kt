package com.github.admitrevskiy.githubauth.di

import com.github.admitrevskiy.githubauth.model.rest.GitHubApi
import com.github.admitrevskiy.githubauth.model.rest.RestApiHelper
import com.github.admitrevskiy.githubauth.repository.GitHubRepository
import com.github.admitrevskiy.githubauth.repository.GitHubRepositoryImpl
import com.github.admitrevskiy.githubauth.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single { RestApiHelper.createOkHttpClient() }
    single { RestApiHelper.createRetrofit(get()) }
    single { RestApiHelper.createGitHubApi(get()) }
    single { createGithubRepository(get()) }

    // ViewModel
    viewModel { MainViewModel(get()) }
}

private fun createGithubRepository(api: GitHubApi) : GitHubRepository = GitHubRepositoryImpl(api)