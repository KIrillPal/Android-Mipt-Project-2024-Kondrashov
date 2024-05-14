package com.example.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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
        
        val chatListTitle = view.findViewById<TextView>(R.id.chatlistittle)
        myIconView.setOnClickListener {
            val c = context
            if (c != null)
                getDataController()?.getLoc(requireActivity(), c) { lat, long ->
                    Log.i("ddd", "Lat " + lat.toString() + " Long " + long.toString())
                    chatListTitle.text = lat.toString() + " " + long.toString()
                    chatListTitle.setTextColor(0xFFFF0000.toInt())
                    // Timer to reset
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            chatListTitle.text = getString(R.string.all_chats)
                            chatListTitle.setTextColor(0xFF555555.toInt())
                        }, {})
                }
        }


        return view
    }
}