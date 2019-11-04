package com.github.admitrevskiy.githubauth.model.repo


class GitHubRepo {
    var name: String? = null
    var owner: String? = null
    var url: String? = null
    var homepage: String? = null
    var description: String? = null
    var fork: Boolean? = false
    var forks: Int? = 0
    var language: String? = null
    var htmlUrl: String? = null
    var creationTime: String? = null
    var lastUpdateTime: String? = null
    var stars: Int? = null
    var issues: Int? = null
    var watchers: Int? = null
}