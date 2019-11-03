package com.github.admitrevskiy.githubauth.model.repo

/**
 * Wrapper for list of GitHubRepos and owner
 */
class ReposWrapper (internal val owner: String, internal val repos: List<GitHubRepo>)