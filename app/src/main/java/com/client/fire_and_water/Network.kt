package com.client.fire_and_water
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*
import java.net.*
import java.time.LocalDateTime

class Network {
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null
    private var connected : Boolean = false

    fun isConnected () : Boolean { return connected }

    fun startConnection(ip: String?, port: Int) {
        clientSocket = Socket(ip, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
        Log.i("CLIENT", "connection started")
        connected = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(msg: String?): String? {
        out!!.println(msg)
        Log.i("CLIENT ${LocalDateTime.now()}", "message: \'$msg\' sent");
        return `in`!!.readLine()
    }

    fun getMessage() : String? {
        val msg = `in`!!.readLine();
        Log.i("CLIENT", "got message\' $msg \' ")
        return msg
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
        connected = false;
        Log.i("CLIENT", "connection stopped")
    }

}
