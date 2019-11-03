package com.github.admitrevskiy.githubauth.model.rest

data class ErrorWrapper(val error: Throwable?, val type: ErrorType)