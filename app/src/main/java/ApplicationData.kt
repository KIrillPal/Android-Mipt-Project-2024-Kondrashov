package com.example.chat
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.text.SimpleDateFormat


class ApplicationData : ViewModel() {

    private val network = NetworkService()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var userId : Int? = null
    private var fileUrlCache = mapOf<Int, String>()

    val dialogChatType = "dialog"


    fun init(activity : Activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    fun getUser() : Int? {
        return userId
    }

    fun login(
        nickname : String,
        password : String,
        uiConnectionFailed : () -> Unit,
        uiSucceededLogin : (userId : Int) -> Unit,
        uiFailedLogin : () -> Unit
    ) : Disposable
    {
        val request = network.createLoginRequest(nickname, password)

        val subscription = request.subscribe({responseUserId ->
            userId = responseUserId
            uiSucceededLogin(userId!!)
        }, { throwable ->
            if (throwable is HttpException) {
                uiFailedLogin()
            }
            else uiConnectionFailed()
        })
        return subscription
    }

    fun signup(
        nickname : String,
        password : String,
        uiConnectionFailed : () -> Unit,
        uiSucceededSignup : (userId : Int) -> Unit,
        uiFailedSignup : () -> Unit
    ) : Disposable
    {
        val request = network.createSignupRequest(nickname, password)

        val subscription = request.subscribe({responseUserId ->
            userId = responseUserId
            uiSucceededSignup(userId!!)
        }, { throwable ->
            if (throwable is HttpException) {
                uiFailedSignup()
            }
            else uiConnectionFailed()
        })
        return subscription
    }

    private fun updateChatData(
        chat_info : ChatPSQLRow,
        card_info : ChatCardInfo,
        title : String,
        uiCallback: (ChatData) -> Unit
    ) {
        val timestamp = parseTime(card_info.message_time)

        val iconId = if (chat_info.chat_type == dialogChatType ) R.drawable.red_kitty else R.drawable.green_kitty

        uiCallback(
            ChatData(
                chat_info.chat_id,
                title,
                card_info.last_message,
                timestamp ?: 0,
                card_info.unread_count.toInt(),
                iconId,
                chat_info.description
            )
        )
    }

    fun loadChat(chatId : Int, uiCallback: (ChatData) -> Unit) : Disposable {
        val info_request = network.createGetChatInfoRequest(chatId)
        val subscription = info_request.subscribe({responseChatInfo ->
            val info = responseChatInfo[0]
            val card_request = network.createGetChatCardInfoRequest(userId!!, chatId)
            val card_sub = card_request.subscribe({chatCardInfo ->
                val card_info = chatCardInfo[0]
                if (info.chat_type == dialogChatType) {
                    val title_request = network.createGetDialogNameRequest(userId!!, chatId)
                    val title_sub = title_request.subscribe({title ->
                        updateChatData(info, card_info, title, uiCallback)
                    }, this::logThrowable)
                }
                else updateChatData(info, card_info, info.title!!, uiCallback)
            }, this::logThrowable)
        }, this::logThrowable)
        return subscription
    }

    fun loadChatMembersInfo(chatId : Int, uiCallback : (ChatMembersInfo) -> Unit) : Disposable {
        val info_request = network.createGetChatInfoRequest(chatId)
        val subscription = info_request.subscribe({responseChatInfo ->
            val info = responseChatInfo[0]
            if (info.chat_type == dialogChatType) {
                val request1 = network.createGetDialogUserRequest(userId!!, chatId)
                val sub = request1.subscribe({user2Id ->
                    val request2 = network.createGetUserRequest(user2Id)
                    val sub = request2.subscribe({users ->
                        val user = users[0]
                        uiCallback(
                            ChatMembersInfo(
                                chatId,
                                info.chat_type,
                                2,
                                user.status,
                                info.icon_file_id
                            )
                        )
                    }, this::logThrowable)
                }, this::logThrowable)
            }
            val size_request = network.createGetChatSizeRequest(chatId)
            val size_sub = size_request.subscribe({size ->
                uiCallback(ChatMembersInfo(
                    chatId,
                    info.chat_type,
                    size,
                    info.description,
                    info.icon_file_id
                ))
            }, this::logThrowable)
        }, this::logThrowable)
        return subscription
    }

    fun loadChatList(uiCallback : (List<Int>) -> Unit) {
        if (userId == null)
            return
        val request = network.createGetChatsRequest(userId!!)
        val subscription = request.subscribe({responseList ->
            val newChatIds : MutableList<Int> = arrayListOf()
            for (row in responseList) {
                newChatIds.add(row.chat_id)
            }
            uiCallback(newChatIds)
        }, this::logThrowable)
    }

    fun loadMessages(chatId : Int, uiCallback : (List<MessageData>) -> Unit) {
        if (userId == null)
            return
        val request = network.createGetMessagesRequest(userId!!, chatId)
        val subscription = request.subscribe({responseList ->
            val messages : MutableList<MessageData> = arrayListOf()
            var last_author = userId
            for (row in responseList)
            {
                val viewType = if (userId == row.author_id)
                    R.layout.my_message
                else R.layout.other_message
                val nameToDisplay = if (last_author != row.author_id)
                    row.author_name else null
                val iconToDisplay = if (last_author != row.author_id)
                    R.drawable.red_kitty else null

                messages.add(MessageData(
                    row.message_id,
                    row.text,
                    nameToDisplay,
                    iconToDisplay, //row.author_icon_id,
                    parseTime(row.received_time),
                    viewType
                ))
                last_author = row.author_id
            }
            uiCallback(messages)
        }, this::logThrowable)
    }

    fun sendMessage(chatId: Int, text: String, uiCallback : (Int) -> Unit) : Disposable {
        val request = network.createSendMessageRequest(userId!!, chatId, text)
        val subscription = request.subscribe({id ->
            uiCallback(id)
        }, this::logThrowable)
        return subscription
    }

    fun getFileInfo(fileId: Int, uiCallback: (String) -> Unit) {
        if (fileUrlCache.contains(fileId)) {
            uiCallback(fileUrlCache[fileId]!!)
            return
        }
        val request = network.createGetFileInfoRequest(fileId)
        val subscription = request.subscribe({info ->
            uiCallback(info[0].file_path)
        }, this::logThrowable)
    }

    fun setIconByGlide(glide: RequestManager, iconView: ImageView, fileId: Int) {
        getFileInfo(fileId) {fileUrl ->
            glide
                .load(fileUrl)
                .centerCrop()
                .placeholder(R.drawable.green_kitty)
                .into(iconView)
        }
    }

    fun getLoc(activity: FragmentActivity, context: Context, uiCallback: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf<String>(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(activity, permissions, 0)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.run {
                    uiCallback(latitude, longitude)
                }
            }
    }

    private fun logThrowable(throwable: Throwable) {
        if (throwable is HttpException) {
            Log.e("ddd", "HTTP Exception. Code " + (throwable as HttpException).code())
        }
        else Log.e("ddd", "Connection failed " + throwable.toString())
    }

    private fun parseTime(timeStr : String) : Long {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .parse(timeStr)?.time ?: 0
    }
}