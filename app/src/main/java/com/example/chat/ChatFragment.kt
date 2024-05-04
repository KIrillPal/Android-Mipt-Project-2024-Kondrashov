package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import okhttp3.Callback
import okhttp3.internal.notifyAll
import org.w3c.dom.Text
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ARG_CHATID = "chatId"
private const val ARG_CHATNAME = "chatname"
private const val ARG_CHATDESCR = "description"
private const val ARG_CHATICONID = "iconId"

class ChatFragment : ControlledFragment() {
    private var chatId: Int? = null
    private var chatname: String? = null
    private var chatdescr: String? = null
    private var chaticonid: Int? = null
    private var messageDataList = ArrayList<MessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatId = it.getInt(ARG_CHATID)
            chatname = it.getString(ARG_CHATNAME)
            chatdescr = it.getString(ARG_CHATDESCR)
            chaticonid = it.getInt(ARG_CHATICONID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_fragment, container, false)
        setHeader() // Set text to views
        // Renew header with actual values
        val dataController = getDataController()
        dataController?.loadChat(chatId!!) { data ->
            chatname = data.chatName
            if (data.chatIconId != null)
                chaticonid = data.chatIconId
            setHeader()
        }
        dataController?.loadChatMembersInfo(chatId!!) { info ->
            if (info.chatType == "dialog") {
                chatdescr = info.chatDescription
                chaticonid = info.chatIconId
            }
            else chatdescr = info.memberCnt.toString() + " members"
            val chatDescrView = view?.findViewById<TextView>(R.id.chatdescr)
            chatDescrView?.text = chatdescr

        }

        val messagelist = view.findViewById<RecyclerView>(R.id.messagelist)
        // Messages

        with(messagelist) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatAdapter(messageDataList, chatId!!)
        }

        dataController?.loadMessages(chatId!!) { newMessages ->
            updateDisplayedMessages(newMessages)
        }

        val sendButton = view.findViewById<ShapeableImageView>(R.id.sendbutton)

            sendButton.setOnClickListener {
            val sendTextView = view.findViewById<TextView>(R.id.sendtext)
            val messageText = sendTextView.text.toString()
            val messageRecycler = view.findViewById<RecyclerView>(R.id.messagelist)
            dataController?.sendMessage(chatId!!, messageText) {
                sendTextView.text = ""
                dataController.loadMessages(chatId!!) { newMessages ->
                    updateDisplayedMessages(newMessages)
                    messageRecycler.smoothScrollToPosition(newMessages.size - 1)
                }
            }
        }

        return view
    }

    private fun updateDisplayedMessages(newData : List<MessageData>) {
        val messagelist = view?.findViewById<RecyclerView>(R.id.messagelist)
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() : Int = messageDataList.size

            override fun getNewListSize() : Int = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return messageDataList[oldItemPosition].message_id == newData[newItemPosition].message_id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return messageDataList[oldItemPosition] == newData[newItemPosition]
            }
        })
        messageDataList.clear()
        messageDataList.addAll(newData)
        diff.dispatchUpdatesTo(messagelist!!.adapter!!)
    }

    private fun setHeader() {
        val chatNameView = view?.findViewById<TextView>(R.id.chatname)
        chatNameView?.text = chatname

        val chatIconView = view?.findViewById<ShapeableImageView>(R.id.chaticon)
        if (chaticonid != null) {
            chatIconView?.setImageResource(chaticonid!!)
            chatIconView?.setOnClickListener {
                getNavController()?.openProfile(
                    chatId!!,
                    chatname!!,
                    "online",
                    chatdescr!!,
                    chaticonid ?: R.drawable.red_kitty
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(chatId: Int, name: String, description: String, iconId: Int) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CHATID, chatId)
                    putString(ARG_CHATNAME, name)
                    putString(ARG_CHATDESCR, description)
                    putInt(ARG_CHATICONID, iconId)
                }
            }
    }
}