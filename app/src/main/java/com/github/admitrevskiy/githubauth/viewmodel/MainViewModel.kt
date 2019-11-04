package com.github.admitrevskiy.githubauth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import com.github.admitrevskiy.githubauth.model.rest.ErrorType
import com.github.admitrevskiy.githubauth.model.rest.ErrorWrapper
import com.github.admitrevskiy.githubauth.repository.GitHubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * ViewModel
 */
class MainViewModel(private val repository: GitHubRepository) : ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    /** LiveData */
    val reposWrapper: MutableLiveData<List<GitHubRepo>> = MutableLiveData()
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
    fun loadRepos(username: String, password: String, otp: String? = null) {
        inProgress.value = true
        this.username = username
        this.password = password

        disposable.add(repository.getRepos(username, password, otp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value -> success(value) },
                { e -> handleException(e) }
            ))
    }

    /**
     * Resets all LiveData values
     */
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
        disposable.add(repository.trigger2FAOTP(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { notify2FAOTPSent() },
                { notify2FAOTPSent() }
            ))
    }

    /**
     * Notifies that 2FA token has been sent to user
     */
    private fun notify2FAOTPSent() {
        need2FA.value = true
        notifyErrorWrapper(null, ErrorType.TWO_FA)
    }

    /**
     * Sets LiveData values to `success` state
     */
    private fun success(value: List<GitHubRepo>) {
        inProgress.value = false
        errorWrapper.value = null
        need2FA.value = false
        reposWrapper.value = value
    }

    private fun notifyErrorWrapper(e: Throwable?, type: ErrorType) {
        errorWrapper.value = ErrorWrapper(e, type)
        inProgress.value = false
    }

    /**
     * Handles exception on execution
     */
    private fun handleException(e: Throwable) {
        when (e) {
            is HttpException -> {
                if (e.code() == 401) {
                    e.response().headers().get("x-github-otp")?.let {
                        need2FA.value?.let {
                            if (it) {
                                // 2FA has been sent already.
                                notifyErrorWrapper(e, ErrorType.BAD_CREDENTIALS)
                            } else {
                                // 2FA hasn't been sent. Trigger sending
                                trigger2FAOTPSending()
                            }
                        } ?: notifyErrorWrapper(e, ErrorType.BAD_CREDENTIALS)
                    } ?: notifyErrorWrapper(e, ErrorType.BAD_CREDENTIALS)
                } else {
                    notifyErrorWrapper(e, ErrorType.UNKNOWN)
                }
            }
        }
    }
}