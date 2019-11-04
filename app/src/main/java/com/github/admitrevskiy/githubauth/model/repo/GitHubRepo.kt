package com.github.admitrevskiy.githubauth.model.repo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Representation of GitHub repository with some info
 */
class GitHubRepo {
    @SerializedName("name")             @Expose var name:           String? = null
    @SerializedName("description")      @Expose var description:    String? = null
    @SerializedName("updated_at")       @Expose var lastUpdateTime: String? = null
    @SerializedName("created_at")       @Expose var creationTime:   String? = null
    @SerializedName("language")         @Expose var language:       String? = null
    @SerializedName("stargazers_count") @Expose var stars:          Int = 0
    @SerializedName("open_issues")      @Expose var issues:         Int = 0
    @SerializedName("watchers")         @Expose var watchers:       Int = 0
    @SerializedName("forks")            @Expose var forks:          Int = 0
}