package com.github.admitrevskiy.githubauth

import android.app.Application
import com.github.admitrevskiy.githubauth.di.appModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

/**
 * Application class
 * Use onCreate callback to call `startCoin`
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule), logger = AndroidLogger(showDebug = true))
    }
}