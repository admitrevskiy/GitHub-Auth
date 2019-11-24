package com.github.admitrevskiy.githubauth.di

import com.github.admitrevskiy.githubauth.model.rest.GitHubApi
import com.github.admitrevskiy.githubauth.repository.GitHubRepository
import com.github.admitrevskiy.githubauth.repository.GitHubRepositoryImpl
import com.github.admitrevskiy.githubauth.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 60L
private const val BASE_URL = "https://api.github.com"

val appModule = module {

    single { createOkHttpClient() }
    single { createRetrofit(get()) }
    single { createGitHubApi(get()) }
    single { createGithubRepository(get()) }

    // ViewModel
    viewModel { MainViewModel(get()) }
}

private fun createGithubRepository(api: GitHubApi) : GitHubRepository = GitHubRepositoryImpl(api)

/**
 * Creates OkHttpClient
 */
private fun createOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

/**
 * Creates Retrofit
 */
private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

/**
 * Creates GitHubApi
 */
private fun createGitHubApi(retrofit: Retrofit): GitHubApi = retrofit.create(
    GitHubApi::class.java
)