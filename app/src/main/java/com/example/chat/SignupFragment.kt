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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

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

        submit.setOnClickListener {

            val password1 = passwordView.text.toString()
            val password2 = passwordConfirmView.text.toString()

            if (password1 != password2) {
                // TODO: Passwords are not the same!
                Log.i("ddd", "Passwords are not the same")
            }
            else parentFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, ChatListFragment())
                .commit()
        }

        val settingsButton = view.findViewById<ShapeableImageView>(R.id.regsettings)
        settingsButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}