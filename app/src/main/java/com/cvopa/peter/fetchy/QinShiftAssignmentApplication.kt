package com.cvopa.peter.fetchy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class QinShiftAssignmentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, LOG_TAG_PREFIX + tag!!, message, t)
                }
            })
        }
    }
}

private const val LOG_TAG_PREFIX = "fetchy_"