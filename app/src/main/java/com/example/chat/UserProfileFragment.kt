package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView


// TODO: Rename parameter arguments, choose names that match
private const val ARG_NICKNAME = "nickname"
private const val ARG_STATUS = "status"
private const val ARG_INFO = "info"
private const val ARG_ICON = "iconId"

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var nickname: String? = "Nickname"
    private var status: String? = ""
    private var info: String? = ""
    private var iconId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nickname = it.getString(ARG_NICKNAME)
            status = it.getString(ARG_STATUS)
            info = it.getString(ARG_INFO)
            iconId = it.getInt(ARG_ICON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.user_profile_fragment, container, false)
        val profileName = view.findViewById<TextView>(R.id.profilename)
        val profileInfo = view.findViewById<TextView>(R.id.profileinfo)
        val profileDescr = view.findViewById<TextView>(R.id.profiledescr)
        val profileIcon = view.findViewById<ShapeableImageView>(R.id.profileicon)

        profileName.text = nickname
        profileInfo.text = info
        profileDescr.text = status
        profileIcon.setImageResource(iconId!!)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param nickname Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(nickname: String, info: String, status: String, iconId: Int) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NICKNAME, nickname)
                    putString(ARG_STATUS, status)
                    putString(ARG_INFO, info)
                    putInt(ARG_ICON, iconId)
                }
            }
    }
}