package com.client.fire_and_water
import android.util.Log
import java.io.*
import java.net.*
import java.text.SimpleDateFormat
import java.util.*

class Network {

    enum class StartGameStatus {
        WRONGREQUEST, WAITING, START
    }

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
        Log.i("CLIENT", "connection started")
        connected = true
    }

    @Synchronized
    fun sendMessage(msg: String?): String {
        val currentDate = sdf.format(Date())
        out!!.println(msg)
        Log.i("CLIENT $currentDate", "message: \'$msg\' sent")
        return getMessage()
    }

    @Synchronized
    private fun getMessage(waiting_field_status : Boolean = false) : String {
        var msg = `in`!!.readLine()
        Log.i("CLIENT", "got message\' $msg \' ")
        while (msg == "/field-status") {
            getFieldStatus()
            msg = `in`!!.readLine()
        }
        return msg
    }


    @Synchronized
    fun sendMessageAndGetMessage(msg : String, expectsAnswer : Boolean = false): String {
        sendMessage(msg)
        return getMessage()
    }

    private fun getFieldStatus() {
        // TODO handle field status
    }

    private fun checkGamecode(code : String) : Boolean {
        return (code.length == 6 || code.filter { a -> a.isDigit() }.length == 6)
    }

    fun createGame(level : Int, role : Player.Role) : String {
        val gamecode : String = sendMessageAndGetMessage("/create-game $level $role", true)
        while (!checkGamecode(gamecode)) {
            Log.w("CLIENT", "wrong code")
            // TODO  handle wrong gamecode
        }

        Log.w("CLIENT", "got gamecode")
        return gamecode
    }

    fun connectToGame(gamecode: String): StartGameStatus {
        sendMessage("/connect-to-game $gamecode")
        return StartGameStatus.valueOf(getMessage())
    }

    fun sendStep(step : Player.PlayerStep) {
        sendMessage("/send-step $step")
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
        connected = false
        Log.i("CLIENT", "connection stopped")
    }

}