package com.example.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.imageview.ShapeableImageView

class AuthLoginFragment : ControlledFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val controller = getNavController()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.auth_login_fragment, container, false)

        val submit = view.findViewById<Button>(R.id.loginbutton)
        val loginView = view.findViewById<EditText>(R.id.logintext)
        val passwordView = view.findViewById<EditText>(R.id.passwordtext)

        submit.setOnClickListener {
            val trueLogin = "nickname"
            val truePassword = "password"

            val gotLogin = loginView.text.toString()
            val gotPassword = passwordView.text.toString()

            if (gotLogin != trueLogin || gotPassword != truePassword) {
                // TODO: Show 'Invalid login or password' tip
                Log.i("ddd", "Invalid login or password. Got '${gotLogin}' and '${gotPassword}'")
            }
            else controller?.openChatListFragment()
        }

        val registerButton = view.findViewById<Button>(R.id.signupbutton)
        val settingsButton = view.findViewById<ShapeableImageView>(R.id.authsettings)

        registerButton.setOnClickListener {
            controller?.openSignupScreen()
        }

        settingsButton.setOnClickListener {
            controller?.openSettingsScreen()
        }

        return view
    }
}