package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment

open class ControlledFragment() : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun getNavController() : NavController? {
        return (requireActivity() as? MainActivity)?.getNavController()
    }
}