package com.example.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class ChatListFragment : ControlledFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chatlist_fragment, container, false)

        val chatlist = view.findViewById<RecyclerView>(R.id.chatlist)

        // Chats
        val chatDataList = ArrayList<ChatData>()
        chatDataList.add(ChatData("Alice, Bob, Charlie", "thx!", timeToStamp("23:02"), 0, R.drawable.green_kitty, "3 участника"))
        chatDataList.add(ChatData("Top memes", "This jokes are neccessary to see", timeToStamp("21:20"), 10, R.drawable.green_kitty, "25 участников"))
        chatDataList.add(ChatData("Bob", "ok, bye", timeToStamp("12:56"), 0, R.drawable.kitty, "Хороший парень, вежливый, изучаю английский"))
        chatDataList.add(ChatData("Новости", "Штраф за ковыряние в носу пришел парню из Махачкалы", timeToStamp("20:12"), 2, R.drawable.green_kitty, "1024 участника"))
        chatDataList.add(ChatData("аче)", "Внимание, анекдот!", timeToStamp("23:40"), 5, R.drawable.green_kitty, "3334 участника"))

        with(chatlist) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatListAdapter(chatDataList)
        }

        val settingsButton = view.findViewById<ShapeableImageView>(R.id.chatssettingsbutton)
        settingsButton.setOnClickListener {
            getNavController()?.openSettingsScreen()
        }

        return view
    }
}