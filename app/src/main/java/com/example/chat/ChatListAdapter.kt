package com.example.chat

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.example.chat.placeholder.PlaceholderContent.PlaceholderItem
import com.example.chat.databinding.ChatCardBinding

class ChatListAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ChatCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.content
        holder.cardView.rotation=(-10..10).random() / 10f
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ChatCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
        val cardView: LinearLayout = binding.cardlayout

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}