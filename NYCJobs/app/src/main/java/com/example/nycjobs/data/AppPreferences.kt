package com.example.nycjobs.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.nycjobs.util.TAG

/**
 * App Shared Preferences Interface.
 *
 * This interface defines the shared preferences for the app.
 */
interface AppSharedPreferences {
    fun getSharedPreferences(): SharedPreferences
}

/**
 * App Shared Preferences Implementation.
 *
 * This class implements the app shared preferences interface.
 */
class AppPreferences(private val context: Context) : AppSharedPreferences {
    private val appPreferencesKey = "app_prefs"

    /**
     * Get The Shared Preferences.
     *
     * @return the shared preferences
     */
    override fun getSharedPreferences(): SharedPreferences {
        Log.i(TAG, "getting shared preferences")
        return context.getSharedPreferences(appPreferencesKey, MODE_PRIVATE)
    }
}