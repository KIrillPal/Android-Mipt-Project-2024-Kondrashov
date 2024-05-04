package com.example.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.android.material.imageview.ShapeableImageView

class AuthLoginFragment : ControlledFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getDataController()?.getUser() != null) {
            getNavController()?.openChatListFragment()
        }
        if (savedInstanceState != null) {
            getNavController()?.openLoginScreen()
        }
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val controller = getNavController()

        val view = inflater.inflate(R.layout.auth_login_fragment, container, false)

        val submit = view.findViewById<Button>(R.id.loginbutton)
        val loginView = view.findViewById<EditText>(R.id.logintext)
        val passwordView = view.findViewById<EditText>(R.id.passwordtext)

        var state = savedInstanceState

        if (state != null) {
            loginView.setText(state.getString(LOGIN_KEY) ?: "")
            passwordView.setText(state.getString(PASSWORD_KEY) ?: "")
        }

        submit.setOnClickListener {
            val gotLogin = loginView.text.toString()
            val gotPassword = passwordView.text.toString()

            getDataController()?.login(
                gotLogin,
                gotPassword,
                this::onConnectionFailed,
                this::onLoginSucceeded,
                this::onLoginFailed
            )
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val loginView = view?.findViewById<EditText>(R.id.logintext)
        val passwordView = view?.findViewById<EditText>(R.id.passwordtext)
        outState.putString(LOGIN_KEY, loginView?.text.toString())
        outState.putString(PASSWORD_KEY, passwordView?.text.toString())
    }

    private fun onConnectionFailed() {
        val errorTextView = view?.findViewById<TextView>(R.id.autherrortext)
        errorTextView?.text = "Нет соединения с сервером"
        errorTextView?.visibility = View.VISIBLE
    }

    private fun onLoginFailed() {
        val errorTextView = view?.findViewById<TextView>(R.id.autherrortext)
        errorTextView?.text = "Неверные логин или пароль"
        errorTextView?.visibility = View.VISIBLE
    }

    private fun onLoginSucceeded(userId : Int) {
        getNavController()?.openChatListFragment()
    }

    companion object {
        val LOGIN_KEY = "AuthLoginFragment_login"
        val PASSWORD_KEY = "AuthLoginFragment_password"
    }
}