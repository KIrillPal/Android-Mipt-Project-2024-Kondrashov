package com.example.chat

import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Dictionary
import java.util.concurrent.TimeUnit


data class Credentials (
    val user_name: String,
    val password: String,
    val device_locale: String = "ru_RU",
    val device_time_zone: String = "GMT+3",
)

data class MessagePSQLSend (
    val author: Int,
    val chat: Int,
    val message_text: String,
    val forwarded: Int = 1,
    val files: IntArray = intArrayOf(1)
)

data class FileInfo (
    val file_id: Int,
    val file_path: String
)

data class UserData (
    val user_id: Int,
    val username: String,
    val status: String,
    val icon_file_id: Int,
    val last_activity: String,
    val password_hash: Long
)

data class ChatIdRow(val chat_id : Int)

data class ChatPSQLRow(
    val chat_id : Int,
    val chat_type : String,
    val title : String?,
    val description : String,
    val icon_file_id : Int?
)

data class MessagePSQLRow(
    val message_id : Int,
    val author_id : Int,
    val author_name : String,
    val author_icon_id : Int?,
    val text : String,
    val forwarded_id : Int,
    val received_time : String
)

data class ChatCardInfo(
    val last_message : String,
    val message_time : String,
    val unread_count : Long
)

interface AuthService {
    @POST("rpc/login")
    fun postLogIn(
        @Body body : Credentials
    ): Single<Int>

    @POST("rpc/signup")
    fun postSignUp(
        @Body body : Credentials
    ): Single<Int>

    @GET("users")
    fun getUser(
        @Query("user_id") userId: String
    ): Single<List<UserData>>
}

interface ChatListService {
    @GET("rpc/find_user_chats")
    fun getUserChats(
        @Query("userid") userId: Int
    ): Single<List<ChatIdRow>>

    @GET("rpc/show_chat_card_info")
    fun getChatCardInfo(
        @Query("userid") userId: Int,
        @Query("chat") chatId: Int
    ): Single<List<ChatCardInfo>>

    @GET("rpc/find_dialog_name")
    fun getDialogName(
        @Query("userid") userId: Int,
        @Query("chat") chatId: Int
    ): Single<String>

    @GET("rpc/find_dialog_user2")
    fun getDialogUser(
        @Query("user1") userId: Int,
        @Query("chat") chatId: Int
    ): Single<Int>

    @GET("chats")
    fun getChatInfo(
        @Query("chat_id") chatId: String
    ): Single<List<ChatPSQLRow>>
}

interface ChatService {
    @GET("rpc/show_messages")
    fun getMessages(
        @Query("userid") userId: Int,
        @Query("chat") chatId: Int
    ): Single<List<MessagePSQLRow>>

    @GET("rpc/get_chat_size")
    fun getChatSize(
        @Query("chat") chatId: Int
    ): Single<Int>

    @POST("rpc/send_message")
    fun postSendMessage(
        @Body body : MessagePSQLSend
    ): Single<Int>

    @GET("files")
    fun getFileInfo(
        @Query("file_id") chatId: String
    ): Single<List<FileInfo>>
}

class NetworkService {
    private val baseUrl : String = "http://10.0.2.2:3001"
    private val client = OkHttpClient.Builder()
        .connectTimeout(1000, TimeUnit.MILLISECONDS)
        .readTimeout(1000, TimeUnit.MILLISECONDS)
        .writeTimeout(1000, TimeUnit.MILLISECONDS)
        .build()


    private val authService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(AuthService::class.java)

    private val chatListService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ChatListService::class.java)

    private val chatService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ChatService::class.java)

    fun createLoginRequest(username : String, password : String) : Single<Int> {
        val request = authService.postLogIn(Credentials(username, password))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createSignupRequest(username : String, password : String) : Single<Int> {
        val request = authService.postSignUp(Credentials(username, password))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createGetChatsRequest(userId : Int) : Single<List<ChatIdRow>> {
        val request = chatListService.getUserChats(userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createGetChatInfoRequest(chatId : Int) : Single<List<ChatPSQLRow>> {
        val request = chatListService.getChatInfo("eq." + chatId.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createGetChatCardInfoRequest(userId : Int, chatId : Int) : Single<List<ChatCardInfo>> {
        Log.i("ddd", "gothere")
        Log.i("ddd", "lele " + chatListService.hashCode().toString())
        val request = chatListService.getChatCardInfo(userId, chatId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
        return request
    }

    fun createGetDialogNameRequest(userId : Int, chatId : Int) : Single<String> {
        val request = chatListService.getDialogName(userId, chatId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
        return request
    }

    fun createGetDialogUserRequest(userId : Int, chatId : Int) : Single<Int> {
        val request = chatListService.getDialogUser(userId, chatId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
        return request
    }

    fun createGetMessagesRequest(userId : Int, chatId : Int) : Single<List<MessagePSQLRow>> {
        val request = chatService.getMessages(userId, chatId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createGetChatSizeRequest(chatId : Int) : Single<Int> {
        val request = chatService.getChatSize(chatId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createGetUserRequest(userId : Int) : Single<List<UserData>> {
        val request = authService.getUser("eq." + userId.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createGetFileInfoRequest(fileId : Int) : Single<List<FileInfo>> {
        val request = chatService.getFileInfo("eq." + fileId.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }

    fun createSendMessageRequest(authorId : Int, chatId : Int, message_text : String) : Single<Int> {
        Log.i("ddd", "a " + authorId.toString() + " " + chatId.toString() + " " + message_text)
        val request = chatService.postSendMessage(MessagePSQLSend(authorId, chatId, message_text))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        return request
    }
}
