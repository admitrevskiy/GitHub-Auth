package com.github.admitrevskiy.githubauth.model.rest

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Helper methods for REST API communication
 */
object RestApiHelper {

    private const val TIMEOUT = 60L
    private const val BASE_URL = "https://api.github.com"

    /**
     * Creates OkHttpClient
     */
    fun createOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    /**
     * Creates Retrofit
     */
    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    /**
     * Creates GitHubApi
     */
    fun createGitHubApi(retrofit: Retrofit): GitHubApi = retrofit.create(
        GitHubApi::class.java
    )
}