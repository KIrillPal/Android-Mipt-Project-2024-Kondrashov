package com.example.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

class NavController(
    private val fragmentManager: FragmentManager
) {
    fun clearBackStack() {
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
    fun openSplashScreen() {
        fragmentManager.beginTransaction()
            .add(R.id.main_fragment_container, SplashFragment())
            .commit()
        Handler(Looper.getMainLooper()).postDelayed({
            openLoginScreen()
        }, 1500)
    }
    fun openLoginScreen() {
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
            )
            .add(R.id.main_fragment_container, AuthLoginFragment())
            .commit()
    }

    fun openSignupScreen() {
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            .add(R.id.main_fragment_container, SignupFragment())
            .addToBackStack(null)
            .commit()
    }

    fun openChatListFragment() {
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.no,
                R.anim.fade_out,
                R.anim.no,
                R.anim.fade_out
            )
            .add(R.id.main_fragment_container, ChatListFragment())
            .commit()
        clearBackStack()
    }

    fun openSettingsScreen() {
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            .add(R.id.main_fragment_container, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    fun openProfile(
        name: String,
        info: String,
        status: String,
        iconId: Int
    ) {
        val profile = UserProfileFragment.newInstance(name, info, status, iconId)
        fragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            .add(R.id.main_fragment_container, profile)
            .addToBackStack(null)
            .commit()
    }

    fun openChat(name: String, description: String, iconId: Int) {
        val chat = ChatFragment.newInstance(name, description, iconId)
        fragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            .add(R.id.main_fragment_container, chat)
            .addToBackStack(null)
            .commit()
    }

    fun restoreLastFragment(state: Bundle) {
        val screenId = state.getInt(LAST_FRAGMENT_KEY) ?: SPLASH_SCREEN
        Log.i("scrid", screenId.toString())
        when (screenId) {
            SPLASH_SCREEN -> openSplashScreen()
            LOGIN_FRAGMENT -> openLoginScreen()
            SIGNUP_FRAGMENT -> openSignupScreen()
            CHATLIST_FRAGMENT -> openChatListFragment()
            CHAT_FRAGMET -> openChat(
                state.getString(CURR_CHAT_NAME_KEY) ?: "Unknown",
                state.getString(CURR_CHAT_DESCR_KEY) ?: "Unknown",
                state.getInt(CURR_CHAT_ICON_KEY) ?: R.drawable.green_kitty
            )
            PROFILE_FRAGMENT -> openProfile(
                state.getString(CURR_PROFILE_NAME_KEY) ?: "Unknown",
                state.getString(CURR_PROFILE_INFO_KEY) ?: "Unknown",
                state.getString(CURR_PROFILE_STATUS_KEY) ?: "Unknown",
                state.getInt(CURR_PROFILE_ICON_KEY) ?: R.drawable.green_kitty,
            )
            SETTINGS_FRAGMENT -> openSettingsScreen()
        }
    }

    public companion object {
        const val SPLASH_SCREEN     = 0
        const val LOGIN_FRAGMENT    = 1
        const val SIGNUP_FRAGMENT   = 2
        const val CHATLIST_FRAGMENT = 3
        const val CHAT_FRAGMET      = 4
        const val PROFILE_FRAGMENT  = 5
        const val SETTINGS_FRAGMENT = 6

        const val LAST_FRAGMENT_KEY   = "last_fragment"
        const val CURR_CHAT_NAME_KEY  = "curr_chat_name"
        const val CURR_CHAT_DESCR_KEY = "curr_chat_descr"
        const val CURR_CHAT_ICON_KEY  = "curr_chat_icon"
        const val CURR_PROFILE_NAME_KEY   = "curr_profile_name"
        const val CURR_PROFILE_INFO_KEY   = "curr_profile_info"
        const val CURR_PROFILE_STATUS_KEY = "curr_profile_status"
        const val CURR_PROFILE_ICON_KEY   = "curr_profile_icon"
    }
}