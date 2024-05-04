package com.example.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ChatListFragment : ControlledFragment() {

    private var chatIdList = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chatlist_fragment, container, false)

        val chatlist = view.findViewById<RecyclerView>(R.id.chatlist)
        with(chatlist) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatListAdapter(chatIdList)
        }

        val settingsButton = view.findViewById<ShapeableImageView>(R.id.chatssettingsbutton)
        settingsButton.setOnClickListener {
            getNavController()?.openSettingsScreen()
        }

        getDataController()?.loadChatList {newChatIdList ->
            chatIdList.addAll(newChatIdList)
            chatlist.adapter?.notifyDataSetChanged()
        }

        val myIconView = view.findViewById<ShapeableImageView>(R.id.myicon)
        Glide.with(this)
            .load(getString(R.string.my_static_icon_url))
            .centerCrop()
            .placeholder(R.drawable.kitty)
            .into(myIconView)


        return view
    }
}