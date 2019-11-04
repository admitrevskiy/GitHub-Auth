package com.github.admitrevskiy.githubauth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import com.github.admitrevskiy.githubauth.model.repo.ReposWrapper
import com.github.admitrevskiy.githubauth.model.rest.ErrorType
import com.github.admitrevskiy.githubauth.model.rest.ErrorWrapper
import com.github.admitrevskiy.githubauth.model.rest.GitHubApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import retrofit2.HttpException

/**
 * ViewModel
 */
class MainViewModel(private val api: GitHubApi) : ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    /** LiveData */
    val reposWrapper: MutableLiveData<ReposWrapper> = MutableLiveData()
    val errorWrapper: MutableLiveData<ErrorWrapper> = MutableLiveData()
    val inProgress: MutableLiveData<Boolean> = MutableLiveData()
    val need2FA: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var username: String
    private lateinit var password: String

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    /**
     * Loads List of repositories from GitHub
     */
    fun loadRepos(username: String, password: String, twoFa: String? = null) {
        inProgress.value = true
        this.username = username
        this.password = password
        val response: Single<List<GitHubRepo>> = if (twoFa == null) { api.getRepos(Credentials.basic(username, password)) } else {
            api.getRepos(Credentials.basic(username, password), twoFa)
        }

        disposable.add(response
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(ReposLoaderObserver()))
    }

    fun reset() {
        need2FA.value = false
        inProgress.value = false
        reposWrapper.value = null
        errorWrapper.value = null
    }

    /**
     * Triggers 2FA OTP sending
     */
    private fun trigger2FAOTPSending() {
        need2FA.value = true
        disposable.add(api.trigger2FA(Credentials.basic(username, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(Trigger2FAObserver()))
    }

    inner class ReposLoaderObserver : DisposableSingleObserver<List<GitHubRepo>>() {
        override fun onError(e: Throwable) {
            handleException(e)
        }

        override fun onSuccess(value: List<GitHubRepo>) {
            inProgress.value = false
            errorWrapper.value = null
            need2FA.value = false
            reposWrapper.value = ReposWrapper(username, value)
        }

        private fun handleException(e: Throwable) {
            when (e) {
                is HttpException -> {
                    if (e.code() == 401) {
                        if (e.response().headers().get("x-github-otp") != null) {
                            trigger2FAOTPSending()
                        } else {
                            errorWrapper.value = ErrorWrapper(e, ErrorType.BAD_CREDENTIALS)
                            inProgress.value = false
                        }
                    } else {
                        errorWrapper.value = ErrorWrapper(e, ErrorType.UNKNOWN)
                    }
                }
            }
        }
    }

    inner class Trigger2FAObserver : DisposableSingleObserver<Void>() {
        override fun onError(e: Throwable) {
            stopExecution()
        }

        override fun onSuccess(value: Void) {
            stopExecution()
        }

        private fun stopExecution() {
            inProgress.value = false
            errorWrapper.value = ErrorWrapper(null, ErrorType.TWO_FA)
        }

    }
}