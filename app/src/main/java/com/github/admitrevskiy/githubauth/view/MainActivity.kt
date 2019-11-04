package com.github.admitrevskiy.githubauth.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.admitrevskiy.githubauth.R
import com.github.admitrevskiy.githubauth.extensions.view.hideKeyboard
import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import com.github.admitrevskiy.githubauth.model.rest.ErrorType
import com.github.admitrevskiy.githubauth.model.rest.ErrorWrapper
import com.github.admitrevskiy.githubauth.view.helpers.MutuallyExclusiveViews
import com.github.admitrevskiy.githubauth.view.helpers.RepositoryAdapter
import com.github.admitrevskiy.githubauth.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    /** Views */
    private lateinit var recycler: RecyclerView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var twoFA: EditText
    private lateinit var authWrapper: ViewGroup
    private lateinit var twoFAWrapper: ViewGroup
    private lateinit var button: Button
    private lateinit var progress: ProgressBar

    /** Mutually exclusive views */
    private lateinit var buttonAndProgress: MutuallyExclusiveViews
    private lateinit var authAndRepos: MutuallyExclusiveViews

    /** DI */
    private val viewModel: MainViewModel by viewModel()

    private lateinit var repositoryAdapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Init views
        twoFAWrapper = findViewById(R.id.two_fa_wrapper)
        progress = findViewById(R.id.progress)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        authWrapper = findViewById(R.id.auth)
        twoFA = findViewById(R.id.two_fa)

        button = findViewById<Button>(R.id.btn).apply {
            setOnClickListener {
                this@MainActivity.hideKeyboard()
                if (twoFAWrapper.visibility == View.VISIBLE) {
                    if (!handleEmptyFields(username, password, twoFA)) {
                        return@setOnClickListener
                    }
                    viewModel.loadRepos(username.text.toString(), password.text.toString(), twoFA.text.toString())
                } else {
                    if (!handleEmptyFields(username, password)) {
                        return@setOnClickListener
                    }
                    viewModel.loadRepos(username.text.toString(), password.text.toString())
                }
            }
        }

        recycler = findViewById<RecyclerView>(R.id.repos_list).apply {
            layoutManager = LinearLayoutManager(context)
        }

        // Connect mutually exclusive views
        buttonAndProgress = MutuallyExclusiveViews(button, progress)
        authAndRepos = MutuallyExclusiveViews(authWrapper, recycler)

        // Start ViewModel observation
        observeViewModel()
    }

    /**
     * Hide recycler view
     */
    override fun onBackPressed() {
        if (authAndRepos.initialState) {
            super.onBackPressed()
        } else {
            authAndRepos.excludeVisibility(true)
            viewModel.reset()
        }
    }

    /**
     * Observe ViewModel
     */
    private fun observeViewModel() {
        viewModel.reposWrapper.observe(this, Observer {
            it?.let {
                recycler.visibility = View.VISIBLE
                showRepos(it.repos)
            }
        })

        viewModel.errorWrapper.observe(this, Observer {
            it?.let { Snackbar.make(username, getSnackMessage(it), Snackbar.LENGTH_LONG).show() }
        })

        viewModel.inProgress.observe(this, Observer {
            it?.let { buttonAndProgress.excludeVisibility(!it) }
        })

        viewModel.need2FA.observe(this, Observer {
            it?.let { refresh2FAViews(it) }
        })
    }

    /**
     * @return message for Snackbar
     *
     * @param e ErrorWrapper to get message from
     */
    private fun getSnackMessage(e: ErrorWrapper) : String {
        return when(e.type) {
            ErrorType.TWO_FA -> {
                getString(R.string.error_2fa)
            }
            ErrorType.BAD_CREDENTIALS -> {
                getString(R.string.error_bad_credentials)
            }
            else -> {
                getString(R.string.error_unknown, e.error?.message)
            }
        }
    }

    /**
     * Shows list of repositories in RecyclerView
     */
    private fun showRepos(list: List<GitHubRepo>) {
        refresh2FAViews(false)
        repositoryAdapter = RepositoryAdapter(this, list).apply { recycler.adapter = this }
        authAndRepos.excludeVisibility(false)
    }

    /**
     * Resets 2FA views
     */
    private fun refresh2FAViews(need2FA: Boolean) {
        twoFAWrapper.visibility = if (need2FA) View.VISIBLE else View.GONE
        twoFA.setText("")
    }

    /**
     * Handles if any filed from given is empty. Sets `error` state to empty filed
     *
     * @return {@code false} if any filed was empty
     */
    private fun handleEmptyFields(vararg views: EditText) : Boolean {
        var ok = true
        for (e in views) {
            if (e.text.toString().isEmpty()) {
                ok = false
                e.error = getString(R.string.error_empty)
            }
        }
        return ok
    }
}
