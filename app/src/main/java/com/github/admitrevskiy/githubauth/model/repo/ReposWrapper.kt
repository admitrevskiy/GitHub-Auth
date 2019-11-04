package com.github.admitrevskiy.githubauth.model.repo

/**
 * Wrapper for list of GitHubRepos and owner
 */
class ReposWrapper (val owner: String, val repos: List<GitHubRepo>)