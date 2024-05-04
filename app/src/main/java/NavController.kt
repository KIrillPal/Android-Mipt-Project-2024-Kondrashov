package com.example.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

class NavController(
    private val fragmentManager: FragmentManager
) {
    fun clearBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
        userId: Int,
        name: String,
        info: String,
        status: String,
        iconId: Int
    ) {
        val profile = UserProfileFragment.newInstance(userId, name, info, status, iconId)
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

    fun openChat(chatId: Int, name: String, description: String, iconId: Int) {
        val chat = ChatFragment.newInstance(chatId, name, description, iconId)
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
}