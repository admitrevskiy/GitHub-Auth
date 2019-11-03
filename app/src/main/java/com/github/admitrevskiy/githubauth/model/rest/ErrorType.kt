package com.github.admitrevskiy.githubauth.model.rest

/**
 * Types of error from REST API
 */
enum class ErrorType {
    /** 2FA is enabled. We need to send OTP to get access */
    TWO_FA,

    /** Bad credentials */
    BAD_CREDENTIALS,

    /** Something's wrong */
    UNKNOWN
}