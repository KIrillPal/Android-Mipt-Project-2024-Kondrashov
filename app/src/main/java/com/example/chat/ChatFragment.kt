package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.placeholder.PlaceholderContent
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ARG_CHATNAME = "chatname"
private const val ARG_CHATDESCR = "description"

fun timeToStamp(time: String): Long {
    return SimpleDateFormat("HH:mm", Locale.US).parse(time)!!.time
}

class ChatFragment : Fragment() {
    private var chatname: String? = null
    private var chatdescr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatname = it.getString(ARG_CHATNAME)
            chatdescr = it.getString(ARG_CHATDESCR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_fragment, container, false)
        val chatNameView = view.findViewById<TextView>(R.id.chatname)
        chatNameView.text = chatname

        val chatDescrView = view.findViewById<TextView>(R.id.chatdescr)
        chatDescrView.text = chatdescr

        val messagelist = view.findViewById<RecyclerView>(R.id.messagelist)

        // Messages
        val messageDataList = ArrayList<MessageData>()
        messageDataList.add(MessageData("Hello there! Hi, Alice!", "Bob", R.drawable.kitty, timeToStamp("22:45"), R.layout.other_message))
        messageDataList.add(MessageData("Ohi! How are you?", null, null, timeToStamp("22:47"), R.layout.my_message))
        messageDataList.add(MessageData("More than perfect", "Bob", R.drawable.kitty, timeToStamp("22:47"), R.layout.other_message))
        messageDataList.add(MessageData("wanna meme?", null, null, timeToStamp("22:48"), R.layout.other_message))
        messageDataList.add(MessageData("i have some", null, null, timeToStamp("22:48"), R.layout.other_message))
        messageDataList.add(MessageData("gimme", null, null, timeToStamp("22:48"), R.layout.my_message))
        messageDataList.add(MessageData("yes, of course we want))", "Charlie", R.drawable.red_kitty, timeToStamp("22:49"), R.layout.other_message))
        messageDataList.add(MessageData("A man pulls up to the curb and asks the policeman, \"Can I park here?\"\n" +
                "\"No,\" says the cop.\n" +
                "\"What about all these other cars?\"\n" +
                "\"They didn't ask!\"", "Bob", R.drawable.kitty, timeToStamp("22:51"), R.layout.other_message))

        messageDataList.add(MessageData("Hehe. If only it worked with taxes!","Charlie", R.drawable.red_kitty, timeToStamp("22:52"), R.layout.other_message))
        messageDataList.add(MessageData("XD", null, null, timeToStamp("22:53"), R.layout.my_message))
        messageDataList.add(MessageData("Do u have one more?", null, null, timeToStamp("22:53"), R.layout.my_message))

        messageDataList.add(MessageData("Yeep. One sec", "Bob", R.drawable.kitty, timeToStamp("22:54"), R.layout.other_message))
        messageDataList.add(MessageData("A farmer was sitting in the neighborhood bar getting drunk. A man came in and asked the farmer, «Hey, why are you sitting here on this beautiful day, getting drunk?» The farmer shook his head and replied, «Some things you just can’t explain.»\n" +
                "«So what happened that’s so horrible?» the man asked as he sat down next to the farmer.\n" +
                "«Well,» the farmer said, «today I was sitting by my cow, milking her. Just as I got the bucket full, she lifted her left leg and kicked over the bucket.»\n" +
                "«Okay,» said the man, «but that’s not so bad.» «Some things you just can’t explain,» the farmer replied. «So what happened then?» the man asked. The farmer said, «I took her left leg and tied it to the post on the left.»\n" +
                "«And then?»\n" +
                "«Well, I sat back down and continued to milk her. Just as I got the bucket full, she took her right leg and kicked over the bucket.»\n" +
                "The man laughed and said, «Again?» The farmer replied, «Some things you just can’t explain.» «So, what did you do then?» the man asked.\n" +
                "«I took her right leg this time and tied it to the post on the right.»\n" +
                "«And then?»\n" +
                "«Well, I sat back down and began milking her again. Just as I got the bucket full, the stupid cow knocked over the bucket with her tail.»\n" +
                "«Hmmm,» the man said and nodded his head. «Some things you just can’t explain,» the farmer said.\n" +
                "«So, what did you do?» the man asked.\n" +
                "«Well,» the farmer said, «I didn’t have anymore rope, so I took off my belt and tied her tail to the rafter. In that moment, my pants fell down and my wife walked in … Some things you just can’t explain.»",
            null, null, timeToStamp("22:54"), R.layout.other_message))

        messageDataList.add(MessageData("wow it was a looooooooong story", null, null, timeToStamp("22:56"), R.layout.my_message))
        messageDataList.add(MessageData("but i laughed. It's really funny", null, null, timeToStamp("22:56"), R.layout.my_message))
        messageDataList.add(MessageData("thx!", "Bob", R.drawable.kitty, timeToStamp("23:02"), R.layout.other_message))


        with(messagelist) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatAdapter(messageDataList)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, description: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CHATNAME, name)
                    putString(ARG_CHATDESCR, description)
                }
            }
    }
}