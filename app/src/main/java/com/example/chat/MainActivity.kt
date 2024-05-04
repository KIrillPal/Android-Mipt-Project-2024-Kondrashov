package com.example.chat

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

interface HasController {
    fun getNavController() : NavController
    fun getData() : ApplicationData
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

    override fun getData() : ApplicationData {
        return appdata
    }

    private fun checkCredentials(uri : Uri?, onSuccess : (Int)->Unit, onFailed : ()->Unit) {
        val paramSet = uri?.getQueryParameterNames()
        // TODO: Adapt logging through database
        val login = uri?.getQueryParameter("login")
        val password = uri?.getQueryParameter("password")
        if (login != null && password != null)
            getData()?.login(login, password, onFailed, onSuccess, onFailed)

    }

    private fun processIntent() {
        val action: String? = intent?.action
        val data: Uri? = intent?.data

        controller.clearBackStack()

        when (data?.host) {
            "login" -> controller.openLoginScreen()
            "signup" -> {
                controller.openLoginScreen()
                controller.openSignupScreen()
            }
            "chatlist" -> {
                checkCredentials(data, {
                    Log.i("ddd", it.toString())
                    controller.openChatListFragment()
                }, {
                    controller.openLoginScreen()
                })
            }
            "chat" -> {
                checkCredentials(data, {
                    val id = data?.getQueryParameter("id")?.toInt()
                    val name = data?.getQueryParameter("chatname")
                    val descr = data?.getQueryParameter("descr") ?: ""
                    val iconid =
                        data?.getQueryParameter("iconid")?.toInt() ?: R.drawable.green_kitty

                    controller.openChatListFragment()
                    if (id != null && name != null)
                        controller.openChat(id, name, descr, iconid)
                }, {
                    controller.openLoginScreen()
                })
            }
            "profile" -> {
                checkCredentials(data, {
                    val name = data?.getQueryParameter("profile")
                    val info = data?.getQueryParameter("info") ?: ""
                    val status = data?.getQueryParameter("status") ?: ""
                    val iconid = data?.getQueryParameter("iconid")?.toInt() ?: R.drawable.green_kitty

                    controller.openChatListFragment()
                    if (name != null)
                        controller.openProfile(1, name, info, status, iconid)
                }, {
                    controller.openLoginScreen()
                })
            }
        }
    }

    private val controller = NavController(supportFragmentManager)
    private val appdata : ApplicationData by viewModels()
}