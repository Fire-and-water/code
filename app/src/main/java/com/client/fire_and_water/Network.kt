package com.client.fire_and_water
import android.os.Build
import androidx.annotation.RequiresApi
import com.client.fire_and_water.game.Game
import com.client.fire_and_water.game.PlayerType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import mu.KLogger
import mu.KotlinLogging
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import java.util.*
import kotlin.concurrent.thread


class Network {
    private var logger : KLogger = KotlinLogging.logger("Network")
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null
    private var connected : Boolean = false
    private lateinit var user : User
    fun isConnected () : Boolean { return connected }

    @Synchronized
    fun startConnection(ip: String?, port: Int) : Boolean {
        //val secretKeyClass = getSecretKey() ?: return false

        clientSocket = Socket(ip, port)
        connected = true
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
        val serverAns = sendMessageAndGetMessage("auth ${user.id} ${user.secretKey}")
        val serverStructuredAns = Json.decodeFromString<StartConnectionJSON>(serverAns)
        if (serverStructuredAns.status != 1) {
            logger.info("login failed with message\n${serverStructuredAns.status}")
            return false
        }
        logger.info( "connection started")
        return true
    }

    @Serializable
    data class StartConnectionJSON (
        val status : Int,
        val `player-id`: Int,
        val `current-game-id`: Int,
    )


    @Synchronized
    private fun sendMessage(msg: String?) {
        if (!connected)
            throw Exception("not connected")
        logger.info( "sending message: \'$msg\' sent")
        out!!.println(msg)
    }

    @Synchronized
    private fun getMessage() : String {
        var msg = `in`!!.readLine()
        logger.info("got message\' $msg \' ")
        while (msg == "/field-status") {
            getFieldStatus()
            msg = `in`!!.readLine()
        }
        return msg
    }

    @Synchronized
    fun cancelGame() {
       sendMessageAndGetMessage("cancel-game")
    }

    fun waitGameStart() {
        do {
            val s = getMessage()
            val obj = JSONObject(s)
        } while (!(obj.has("isTwoConnected") && obj.get("isTwoConnected") == true))
        }
    @Serializable
    data class SecretKeyClass (
        val status : Int,
        val msg    : String,
        val id     : Int,
        val token  : String
    )


    @Synchronized
    fun sendMessageAndGetMessage(msg : String): String {
        sendMessage(msg)
        return getMessage()
    }

    @Synchronized
    fun sendUrlRequest(url : URL) : String {
        logger.info("sending URL: $url")

        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"


        val serverAnswer = BufferedReader(InputStreamReader(conn.inputStream)).readLine()
        logger.info("URL got $serverAnswer")
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

    fun createGame(level : Int, role : UserClient.Role) : Int? {
        val serverAnswer = sendMessageAndGetMessage("create-game $level $role")
        val serverStructAnswer = Json.decodeFromString<CreateGameJson>(serverAnswer)
        if (serverStructAnswer.status == 1) {
            logger.info("Game created with game-id ${serverStructAnswer.`game-id`}")
        }
        else {
            logger.info("Server can't create game")
        }
        return serverStructAnswer.`game-id`
    }

    @Serializable
    data class StatusAndMessage (
        val status : Int,
        val msg: String
    )


    fun connectToGame(gamecode: String?) : Int {
        if (gamecode == null || gamecode.isEmpty())
            return 2
        val serverAnswer = sendMessageAndGetMessage("connect-to-game $gamecode")
//        val serverStructAnswer = Json.decodeFromString<StatusAndMessage>(serverAnswer)
//        if (serverStructAnswer.status == 1) {
//            logger.debug("CLIENT", "Connected to game with message ${serverStructAnswer.msg}")
//        }
//        else {
//            logger.debug("CLIENT", "Can't connect to game, got message ${serverStructAnswer.msg}")
//        }
//        return serverStructAnswer.status
        return 1
    }

    fun sendStep(step : UserClient.UserStep) {
        sendMessage("send-step $step")
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
        connected = false
        logger.info("connection stopped")
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
    data class GetTopAnswer(
        val top : Array<UserFromTop>,
    )

    @Serializable
    data class UserFromTop(
        val id : Int,
        val nickname: String,
        val place : Int,
        val rating : Int,
    )

    fun getTop(): Array<UserFromTop> {
        val url = URL("http://185.178.47.135:8082/getTop?id=${user.id}")
        val serverAnswer = sendUrlRequest(url)
        val serverStructuredAnswer = Json.decodeFromString<GetTopAnswer>(serverAnswer)
        return serverStructuredAnswer.top
    }

    @Serializable
    data class RegisterByEmailAnswer (
        val status : Int,
        val msg : String,
        val id : Int
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerByEmail(email : String, nickname : String, password : String) : Boolean {
        assert(email.isNotEmpty() && password.isNotEmpty())
//        val encodedString = Base64.getEncoder().encodeToString(password.toByteArray())
//        val url = URL("http://185.178.47.135:8082/registerByEmail?nickname=$nickname&email=$email&password=$encodedString")
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
        val serverAnswer = sendUrlRequest(url)
        val serverStructuredAnswer = Json.decodeFromString<CheckNickNameAnswer>(serverAnswer)
        if (serverStructuredAnswer.nickName != nickname)
            throw Exception("not the same nickName from server")
        return serverStructuredAnswer.isFree
    }

    @Serializable
    data class EmailAuthorizationAnswerTypeOne(val status: Int, val user : User)

    @Serializable
    data class EmailAuthorizationAnswerTypeTwo(val status: Int, val msg : String)

    @Serializable
    data class User (
        val id: Int,
        val secretKey : String,
        val nickName : String,
        val photo : String,
        val status : String,
        val eMail : String,
        val password : String,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkEmailAuthorization(email : String, password : String) : Boolean {
        if  (email.isEmpty() || password.isEmpty())
            return false
//        val encodedString = Base64.getEncoder().encodeToString(password.toByteArray())

//        val url = URL("http://185.178.47.135:8082/authByEmail?email=$email&password=$encodedString")
        val url = URL("http://185.178.47.135:8082/authByEmail?email=$email&password=$password")
        val serverAnswer = sendUrlRequest(url)
        return if (JSONObject(serverAnswer).get("status") == 1) {
            val serverStructuredAnswer =
                Json.decodeFromString<EmailAuthorizationAnswerTypeOne>(serverAnswer)
            user = serverStructuredAnswer.user
            true
        } else {
            val serverStructuredAnswer =
                Json.decodeFromString<EmailAuthorizationAnswerTypeTwo>(serverAnswer)
            logger.info("Email Authorization: ${serverStructuredAnswer.msg}")
            false
        }
    }

    @Serializable
    data class PlayerCoordinates (
        val posX : Double,
        val posY : Double
    )

    fun startListen(game: Game, playerType: PlayerType) {
        thread(start=true) {
            while(true) {
                val serverAns = `in`!!.readLine()

                if (JSONObject(serverAns).get("what").equals("coordinates")) {
                    if (playerType == PlayerType.FIRE) {
                        game.updateOtherPlayer(
                            Json.decodeFromString<PlayerCoordinates>(
                                JSONObject(serverAns).get("water").toString()
                            )
                        )
                    } else {
                        game.updateOtherPlayer(
                            Json.decodeFromString<PlayerCoordinates>(
                                JSONObject(serverAns).get("fire").toString()
                            )
                        )
                    }
                }
            }
        }
    }

    fun sendMove(posX: Float, posY: Float) {
        sendMessage("send-move $posX $posY")
        /*return if (JSONObject(serverAns).get("what").equals("coordinates")) {
            if (playerType == PlayerType.FIRE) {
                Json.decodeFromString<PlayerCoordinates>(
                    JSONObject(serverAns).get("water").toString()
                )
            } else {
                Json.decodeFromString<PlayerCoordinates>(
                    JSONObject(serverAns).get("fire").toString()
                )
            }
        } else null*/

    }

    fun sendWinningMessage() {
        sendMessage("game-end")
    }
}