package com.example.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.placeholder.PlaceholderContent

class ChatListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chatlist_fragment, container, false)

        var chat_fragment = ChatFragment
            .newInstance("Some chat", "3 members")

        parentFragmentManager
            .beginTransaction()
            .add(R.id.main_fragment_container, chat_fragment)
            .addToBackStack(null)
            .commit()

        val chatlist = view.findViewById<RecyclerView>(R.id.chatlist)

        with(chatlist) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatListAdapter(PlaceholderContent.ITEMS)
        }

        return view
    }
}