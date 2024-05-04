package com.example.chat

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.toRange
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import com.google.android.material.imageview.ShapeableImageView

import com.example.chat.databinding.ChatCardBinding
import com.example.chat.databinding.MyMessageBinding
import com.example.chat.databinding.OtherMessageBinding
import java.nio.file.Path
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MessageData (
    val message_id: Int,
    val message: String,
    val authorName: String?,
    val authorIconId: Int?,
    val receivedTime: Long,
    val viewType: Int
)

class ChatAdapter(
    private val values: List<MessageData>,
    private val chatId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    inner class MyMessageViewHolder(binding: MyMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val messageLayout: RelativeLayout = binding.messagelayout
        val messageView: TextView = binding.message
        val receivedTimeView: TextView = binding.receivedtime

        override fun toString(): String {
            return super.toString() + " '" + messageView.text + "'"
        }
    }
    inner class OtherMessageViewHolder(binding: OtherMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val messageLayout: RelativeLayout = binding.messagelayout
        val messageView: TextView = binding.message
        val authorNameView: TextView = binding.authorname
        val authorIconView: ShapeableImageView = binding.authoricon
        val receivedTimeView: TextView = binding.receivedtime

        override fun toString(): String {
            return super.toString() + " '" + messageView.text + "'"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.my_message -> MyMessageViewHolder(
                MyMessageBinding.inflate(inflater, parent, false)
            )
            R.layout.other_message -> OtherMessageViewHolder(
                OtherMessageBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalArgumentException("viewType ${viewType} is incompatible for ChatAdapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val controller = (context as? MainActivity)?.getNavController()
        val item = values[position]

        when (holder.itemViewType) {
            R.layout.my_message -> {
                val hld = (holder as MyMessageViewHolder)
                hld.messageView.text = item.message
                val time = SimpleDateFormat("HH:mm").format(Date(item.receivedTime))
                hld.receivedTimeView.text = time
                if (hld.messageLayout.height * 2 < hld.messageLayout.width)
                    hld.messageLayout.rotation=(-10..10).random() / 10f
            }
            R.layout.other_message -> {
                val hld = (holder as OtherMessageViewHolder)
                hld.messageView.text = item.message
                val time = SimpleDateFormat("HH:mm").format(Date(item.receivedTime))
                hld.receivedTimeView.text = time

                if (item.authorName === null)
                    hld.authorNameView.visibility=View.GONE
                else hld.authorNameView.text = item.authorName
                if (item.authorIconId === null)
                    hld.authorIconView.visibility=View.GONE
                else {
                    hld.authorIconView.setImageResource(item.authorIconId)
                    hld.authorIconView.setOnClickListener {
                        controller?.openProfile(
                            chatId,
                            item.authorName!!,
                            "online",
                            "Unknown description",
                            item.authorIconId
                        )
                    }
                }

                if (hld.messageLayout.height * 2 < hld.messageLayout.width)
                    hld.messageLayout.rotation=(-10..10).random() / 10f
            }
            else -> throw IllegalArgumentException(
                "viewType ${holder.itemViewType} is incompatible for ChatAdapter"
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return values[position].viewType
    }

    override fun getItemCount(): Int = values.size
}