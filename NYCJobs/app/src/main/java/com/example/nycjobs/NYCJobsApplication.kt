package com.example.nycjobs

import android.app.Application
import android.util.Log
import com.example.nycjobs.data.AppContainer
import com.example.nycjobs.data.DefaultAppContainer
import com.example.nycjobs.util.TAG

/**
 * NYC Open Jobs Application
 */
class NYCOpenJobsApplication : Application() {
    /**
     * App Container
     *
     * This property is used to get the app container.
     */
    lateinit var container: AppContainer
    /**
     * Application onCreate
     *
     * Initializes the app container using the default app container.
     */
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Application: Starting App")
        container = DefaultAppContainer(this)
    }
}