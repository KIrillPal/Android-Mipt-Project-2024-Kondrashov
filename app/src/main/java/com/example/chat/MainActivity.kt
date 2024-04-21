package com.example.chat

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
        Log.i("debug", supportFragmentManager.backStackEntryCount.toString())
        Log.i("saved", "0")
        super.onCreate(savedInstanceState)
        Log.i("saved", "1")
        Log.i("saved", "2")
        setContentView(R.layout.main_activity)
        Log.i("saved", "3")
        if (savedInstanceState != null) {
            Log.i("saved", "Restore")
            controller.restoreLastFragment(savedInstanceState)
        }
        else {
            Log.i("saved", "Load")
            controller.openSplashScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun getNavController() : NavController {
        return controller
    }

    private val controller = NavController(supportFragmentManager)

    companion object {
        const val LAST_FRAGMENT_KEY = "last_fragment"
    }
}