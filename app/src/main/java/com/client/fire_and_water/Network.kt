package com.client.fire_and_water
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.*
import java.net.*
import java.text.SimpleDateFormat
import java.util.*

class Network {

    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null
    private var connected : Boolean = false
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH)
    fun isConnected () : Boolean { return connected }

    fun startConnection(ip: String?, port: Int) {
        val currentDate = sdf.format(Date())
        clientSocket = Socket(ip, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
        Log.i("CLIENT $currentDate", "connection started")
        connected = true
    }

    @Synchronized
    private fun sendMessage(msg: String?) {
        if (!connected)
            throw Exception("not connected")
        val currentDate = sdf.format(Date())
        Log.i("CLIENT $currentDate", "message: \'$msg\' sent")
        out!!.println(msg)
    }

    @Synchronized
    private fun getMessage() : String {
        val currentDate = sdf.format(Date())
        var msg = `in`!!.readLine()
        Log.i("CLIENT $currentDate", "got message\' $msg \' ")
        while (msg == "/field-status") {
            getFieldStatus()
            msg = `in`!!.readLine()
        }
        return msg
    }

    @Synchronized
    fun sendMessageAndGetMessage(msg : String): String {
        sendMessage(msg)
        return getMessage()
    }

    @Synchronized
    fun sendUrlRequest(url : URL) : String {
        Log.i("URL", url.toString())

        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"


        val serveranswer = BufferedReader(InputStreamReader(conn.inputStream)).readLine()
        Log.i("URL", "got $serveranswer")
        return serveranswer
    }

    private fun getFieldStatus() {
        // TODO handle field status
    }


    fun createGame(level : Int, role : Player.Role) : String {
        val gamecode : String = sendMessageAndGetMessage("create-game $level $role")

        Log.w("CLIENT", "got gamecode $gamecode")
        return gamecode
    }

    fun connectToGame(gamecode: String) {
        sendMessageAndGetMessage("connect-to-game $gamecode")
    }

    fun sendStep(step : Player.PlayerStep) {
        sendMessage("send-step $step")
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
        connected = false
        val currentDate = sdf.format(Date())
        Log.i("CLIENT $currentDate", "connection stopped")
    }



    @Serializable
    data class checkUserEmailAnswer (
        val eMail : String,
        val isFree : Boolean
    )

    fun checkUserEmail(email : String) : Boolean {
        if (email.isEmpty())
            return false
        val url = URL("http://185.178.47.135:8082/isFreeEmail?email=$email")
        val serveranswer = sendUrlRequest(url)
        val serverStructedAnswer = Json.decodeFromString<checkUserEmailAnswer>(serveranswer)
        if (serverStructedAnswer.eMail != email)
            throw Exception("not the same email from server")
        return serverStructedAnswer.isFree
    }

    @Serializable
    data class registerByEmailAnswer (
        val status : Int,
        val msg : String,
        val id : Int
    )

    fun registerByEmail(email : String, nickname : String, password : String) : Boolean {
        assert(email.isNotEmpty() && password.isNotEmpty())
        val url = URL("http://185.178.47.135:8082/registerByEmail?nickname=$nickname&email=$email&password=$password")
        val serveranswer = sendUrlRequest(url)
        val serverStructedAnswer = Json.decodeFromString<registerByEmailAnswer>(serveranswer)
        return (serverStructedAnswer.status == 1)
    }

    @Serializable
    data class checkNickNameAnswer (
        val nickName : String,
        val isFree : Boolean
    )

    fun checkNickName(nickname : String) : Boolean {
        if  (nickname.isEmpty())
            return false

        val url = URL("http://185.178.47.135:8082/isFreeNickName?nickname=$nickname")
        val serveranswer = sendUrlRequest(url)
        val serverStructedAnswer = Json.decodeFromString<checkNickNameAnswer>(serveranswer)
        if (serverStructedAnswer.nickName != nickname)
            throw Exception("not the same nickName from server")
        return serverStructedAnswer.isFree
    }

    @Serializable
    data class checkEmailAuthorizationAnswer (
        val status : Int,
        val msg : String,
    )

    fun checkEmailAuthorization(email : String, password : String) : Boolean {
        if  (email.isEmpty() || password.isEmpty())
            return false

        val url = URL("http://185.178.47.135:8082/authByEmail?email=$email&password=$password")
        val serveranswer = sendUrlRequest(url)
        val serverStructedAnswer = Json.decodeFromString<checkEmailAuthorizationAnswer>(serveranswer)
        if (serverStructedAnswer.status != 1)
            Log.i("Email Authorization", serverStructedAnswer.msg)
        return serverStructedAnswer.status == 1
    }
}