package com.client.fire_and_water
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
        clientSocket = Socket(ip, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
        LOGGER.logger.info( "connection started")
        connected = true
    }

    @Synchronized
    private fun sendMessage(msg: String?) {
        if (!connected)
            throw Exception("not connected")
        LOGGER.logger.info( "sending message: \'$msg\' sent")
        out!!.println(msg)
    }

    @Synchronized
    private fun getMessage() : String {
        var msg = `in`!!.readLine()
        LOGGER.logger.info("got message\' $msg \' ")
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
        LOGGER.logger.info("sending URL: $url")

        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"


        val serverAnswer = BufferedReader(InputStreamReader(conn.inputStream)).readLine()
        LOGGER.logger.info("URL got $serverAnswer")
        return serverAnswer
    }

    private fun getFieldStatus() {
        // TODO handle field status
    }

    @Serializable
    data class CreateGameJson (
        val status : Int,
        val `game-id`: Int?
    )

    fun createGame(level : Int, role : Player.Role) : Int? {
        val serverAnswer = sendMessageAndGetMessage("create-game $level $role")
        val serverStructAnswer = Json.decodeFromString<CreateGameJson>(serverAnswer)
        if (serverStructAnswer.status == 1) {
            LOGGER.logger.info("Game created with game-id ${serverStructAnswer.`game-id`}")
        }
        else {
            LOGGER.logger.info("Server can't create game")
        }
        return serverStructAnswer.`game-id`
    }

    @Serializable
    data class ConnectToGameJson (
        val status : Int,
        val message: String
    )


    fun connectToGame(gamecode: String?) : Int {
        if (gamecode == null || gamecode.isEmpty())
            return 2
        val serverAnswer = sendMessageAndGetMessage("connect-to-game $gamecode")
//        val serverStructAnswer = Json.decodeFromString<ConnectToGameJson>(serverAnswer)
//        if (serverStructAnswer.status == 1) {
//            Log.i("CLIENT", "Connected to game with message ${serverStructAnswer.message}")
//        }
//        else {
//            Log.i("CLIENT", "Can't connect to game, got message ${serverStructAnswer.message}")
//        }
//        return serverStructAnswer.status
        return 2
    }

    fun sendStep(step : Player.PlayerStep) {
        sendMessage("send-step $step")
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
        connected = false
        LOGGER.logger.info("connection stopped")
    }

    @Serializable
    data class CheckUserEmailAnswer (
        val eMail : String,
        val isFree : Boolean
    )

    fun checkUserEmail(email : String) : Boolean {
        if (email.isEmpty())
            return false
        val url = URL("http://185.178.47.135:8082/isFreeEmail?email=$email")
        val serverAnswer = sendUrlRequest(url)
        val serverStructuredAnswer = Json.decodeFromString<CheckUserEmailAnswer>(serverAnswer)
        if (serverStructuredAnswer.eMail != email)
            throw Exception("not the same email from server")
        return serverStructuredAnswer.isFree
    }

    @Serializable
    data class RegisterByEmailAnswer (
        val status : Int,
        val msg : String,
        val id : Int
    )

    fun registerByEmail(email : String, nickname : String, password : String) : Boolean {
        assert(email.isNotEmpty() && password.isNotEmpty())
        val url = URL("http://185.178.47.135:8082/registerByEmail?nickname=$nickname&email=$email&password=$password")
        val serverAnswer = sendUrlRequest(url)
        val serverStructuredAnswer = Json.decodeFromString<RegisterByEmailAnswer>(serverAnswer)
        return (serverStructuredAnswer.status == 1)
    }

    @Serializable
    data class CheckNickNameAnswer (
        val nickName : String,
        val isFree : Boolean
    )

    fun checkNickName(nickname : String) : Boolean {
        if  (nickname.isEmpty())
            return false

        val url = URL("http://185.178.47.135:8082/isFreeNickName?nickname=$nickname")
        val serveranswer = sendUrlRequest(url)
        val serverStructedAnswer = Json.decodeFromString<CheckNickNameAnswer>(serveranswer)
        if (serverStructedAnswer.nickName != nickname)
            throw Exception("not the same nickName from server")
        return serverStructedAnswer.isFree
    }

    @Serializable
    data class EmailAuthorizationAnswerJson (
        val status : Int,
        val msg : String,
    )

    fun checkEmailAuthorization(email : String, password : String) : Boolean {
        if  (email.isEmpty() || password.isEmpty())
            return false

        val url = URL("http://185.178.47.135:8082/authByEmail?email=$email&password=$password")
        val serverAnswer = sendUrlRequest(url)
        val serverStructuredAnswer = Json.decodeFromString<EmailAuthorizationAnswerJson>(serverAnswer)
        if (serverStructuredAnswer.status != 1)
            LOGGER.logger.info("Email Authorization: ${serverStructuredAnswer.msg}")
        return serverStructuredAnswer.status == 1
    }
}