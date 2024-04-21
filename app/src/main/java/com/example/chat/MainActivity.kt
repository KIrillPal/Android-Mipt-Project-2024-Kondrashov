package com.example.chat

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.chat.ui.theme.ChatTheme

interface HasController {
    fun getNavController() : NavController
}

class MainActivity : AppCompatActivity(), HasController {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (intent?.action != ACTION_MAIN) {
            processIntent()
        }
        else if (savedInstanceState == null) {
            controller.openSplashScreen()
        }
        // Else we restore automatically
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        processIntent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun getNavController() : NavController {
        return controller
    }

    fun checkCredentials(uri : Uri?) : Boolean {
        val paramSet = uri?.getQueryParameterNames()
        val login = uri?.getQueryParameter("login")
        val password = uri?.getQueryParameter("password")
        return (login == resources.getString(R.string.user_login) &&
                password == resources.getString(R.string.user_password))
    }

    private fun processIntent() {
        val action: String? = intent?.action
        val data: Uri? = intent?.data

        getNavController().clearBackStack()

        when (data?.host) {
            "login" -> controller.openLoginScreen()
            "signup" -> {
                controller.openLoginScreen()
                controller.openSignupScreen()
            }
            "chatlist" -> {
                if (checkCredentials(data))
                    controller.openChatListFragment()
                else controller.openLoginScreen()
            }
            "chat" -> {
                if (!checkCredentials(data)) {
                    controller.openLoginScreen()
                    return
                }
                val name = data?.getQueryParameter("chatname")
                val descr = data?.getQueryParameter("descr") ?: ""
                val iconid = data?.getQueryParameter("iconid")?.toInt() ?: R.drawable.green_kitty

                controller.openChatListFragment()
                if (name != null)
                    controller.openChat(name, descr, iconid)
            }
            "profile" -> {
                if (!checkCredentials(data)) {
                    controller.openLoginScreen()
                    return
                }
                val name = data?.getQueryParameter("profile")
                val info = data?.getQueryParameter("info") ?: ""
                val status = data?.getQueryParameter("status") ?: ""
                val iconid = data?.getQueryParameter("iconid")?.toInt() ?: R.drawable.green_kitty

                controller.openChatListFragment()
                if (name != null)
                    controller.openProfile(name, info, status, iconid)
            }
        }
    }

    private val controller = NavController(supportFragmentManager)
}