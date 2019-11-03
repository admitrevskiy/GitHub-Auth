package com.github.admitrevskiy.githubauth.model.repo


class GitHubRepo {
    internal var name: String? = null
    internal var owner: String? = null
    internal var url: String? = null
    internal var homepage: String? = null
    internal var description: String? = null
    internal var fork: Boolean? = false
    internal var forks: Int? = 0
    internal var language: String? = null
    internal var htmlUrl: String? = null
    internal var creationTime: String? = null
    internal var lastUpdateTime: String? = null
    internal var stars: Int? = null
    internal var issues: Int? = null
    internal var watchers: Int? = null
}