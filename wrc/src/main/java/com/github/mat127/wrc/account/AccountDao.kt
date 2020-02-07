package com.github.mat127.wrc.account

import android.content.Context
import androidx.preference.PreferenceManager


object AccountDao {

    var accountId: String? = null
        private set

    private const val PREFS_KEY_ACCOUNT_ID = "account.id"

    fun load(context: Context) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        accountId = sharedPref.getString(
            PREFS_KEY_ACCOUNT_ID, null)
    }

    fun set(id:String?, context: Context) {
        accountId = id
        save(context)
    }

    private fun save(context: Context) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context) ?: return
        with(sharedPref.edit()) {
            putString(
                PREFS_KEY_ACCOUNT_ID,
                accountId
            )
            apply()
        }
    }
}