package com.example.chat

import android.os.Bundle
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
        setContent {
            ChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    setContentView(R.layout.main_activity)
                    controller.openSplashScreen()
                }
            }
        }
    }

    override fun getNavController() : NavController {
        return controller
    }

    private val controller = NavController(supportFragmentManager)
}