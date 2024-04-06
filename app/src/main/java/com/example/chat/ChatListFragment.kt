package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chat.placeholder.PlaceholderContent

class ChatListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chats_fragment_list, container, false)

        val chatlist = view.findViewById<RecyclerView>(R.id.chatlist)

        with(chatlist) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatListAdapter(PlaceholderContent.ITEMS)
        }
        return view
    }
}