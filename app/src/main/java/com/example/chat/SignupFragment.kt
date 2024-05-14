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
import com.google.android.material.imageview.ShapeableImageView


class SignupFragment : ControlledFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_fragment, container, false)

        val submit = view.findViewById<Button>(R.id.regsignupbutton)
        val loginView = view.findViewById<EditText>(R.id.newlogintext)
        val passwordView = view.findViewById<EditText>(R.id.newpasswordtext)
        val passwordConfirmView = view.findViewById<EditText>(R.id.newpasswordconfirm)

        var state = savedInstanceState

        if (state != null) {
            loginView.setText(state.getString(LOGIN_KEY) ?: "")
            passwordView.setText(state.getString(PASSWORD_KEY) ?: "")
            passwordConfirmView.setText(state.getString(CONFIRM_PASSWORD_KEY) ?: "")
        }

        submit.setOnClickListener {
            val nickname = loginView.text.toString()
            val password1 = passwordView.text.toString()
            val password2 = passwordConfirmView.text.toString()

            if (password1 != password2) {
                val errorTextView = view?.findViewById<TextView>(R.id.autherrortext)
                errorTextView?.text = getString(R.string.passw_not_match)
                errorTextView?.visibility = View.VISIBLE
            }
            else getDataController()?.signup(
                nickname,
                password1,
                this::onConnectionFailed,
                this::onSignupSucceeded,
                this::onSignupFailed
            )
        }

        val settingsButton = view.findViewById<ShapeableImageView>(R.id.regsettings)
        settingsButton.setOnClickListener {
            getNavController()?.openSettingsScreen()
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val loginView = view?.findViewById<EditText>(R.id.newlogintext)
        val passwordView = view?.findViewById<EditText>(R.id.newpasswordtext)
        val passwordConfirmView = view?.findViewById<EditText>(R.id.newpasswordconfirm)

        outState.putString(LOGIN_KEY, loginView?.text.toString())
        outState.putString(PASSWORD_KEY, passwordView?.text.toString())
        outState.putString(CONFIRM_PASSWORD_KEY, passwordConfirmView?.text.toString())
    }

    private fun onConnectionFailed() {
        val errorTextView = view?.findViewById<TextView>(R.id.autherrortext)
        errorTextView?.text = getString(R.string.no_connection)
        errorTextView?.visibility = View.VISIBLE
    }

    private fun onSignupFailed() {
        val errorTextView = view?.findViewById<TextView>(R.id.autherrortext)
        errorTextView?.text = getString(R.string.name_already_in_use)
        errorTextView?.visibility = View.VISIBLE
    }

    private fun onSignupSucceeded(userId : Int) {
        getNavController()?.openChatListFragment()
    }

    data class Backup (
        val login: String,
        val password: String,
        val passwordConfirm: String
    )

    var backup : Backup = Backup("", "", "")

    companion object {
        val LOGIN_KEY = "SignupFragment_login"
        val PASSWORD_KEY = "SignupFragment_password"
        val CONFIRM_PASSWORD_KEY = "SignupFragment_confirm_password"
    }
}