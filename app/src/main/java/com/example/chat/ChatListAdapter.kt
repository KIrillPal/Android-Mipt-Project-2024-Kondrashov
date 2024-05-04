package com.example.chat

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.example.chat.databinding.ChatCardBinding
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date

data class ChatData (
    val chatId : Int,
    val chatName: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0,
    val unreadMessageCnt: Int = 0,
    val chatIconId: Int? = null,
    val descr: String = ""
)

data class ChatMembersInfo (
    val chatId : Int,
    val chatType : String,
    val memberCnt : Int,
    val chatDescription : String,
    val chatIconId: Int?
)

class ChatListAdapter(
    private val values: List<Int>
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ChatCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val controller = (context as? MainActivity)?.getNavController()
        val dataController = (context as? MainActivity)?.getData()

        val id = values[position]
        dataController?.loadChat(id) { item: ChatData ->
            holder.chatNameView.text = item.chatName
            holder.lastMessageView.text = item.lastMessage

            if (item.unreadMessageCnt > 0)
                holder.lastMessageCountView.text = item.unreadMessageCnt.toString()
            else holder.lastMessageCountView.visibility = View.INVISIBLE

            if (item.chatIconId != null) {
                holder.chatIcon.setImageResource(item.chatIconId)
                holder.chatIcon.setOnClickListener {
                    controller?.openProfile(
                        item.chatId,
                        item.chatName,
                        "online",
                        item.descr,
                        item.chatIconId
                    )
                }
            }

            val time = SimpleDateFormat("HH:mm").format(Date(item.lastMessageTime))
            holder.lastMessageTimeView.text = time

            holder.cardView.rotation = (-10..10).random() / 10f
            holder.cardClickableView.setOnClickListener {
                controller?.openChat(id, item.chatName, item.descr, item.chatIconId!!)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ChatCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val chatNameView: TextView = binding.chatname
        val lastMessageView: TextView = binding.lastmessage
        val lastMessageTimeView: TextView = binding.lastmessagetime
        val lastMessageCountView: TextView = binding.lastmessagecnt
        val chatIcon: ShapeableImageView = binding.chaticon
        val cardView: LinearLayout = binding.cardlayout
        val cardClickableView: LinearLayout = binding.cardclickable

        override fun toString(): String {
            return super.toString() + " '" + chatNameView.text + "'"
        }
    }

}