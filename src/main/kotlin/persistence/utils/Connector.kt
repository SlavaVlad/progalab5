package app.server.persistence.utils

import persistence.checkerComponent.Command
import persistence.checkerComponent.Log
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteBuffer

interface Connector {
    val port: Int
    val hostIp: String
    val output: PrintWriter
    val input: BufferedReader

    fun close() {
        output.close()
        input.close()
    }
}

class Client(
    override val port: Int,
    override val hostIp: String
) : Connector {
    val client = Socket(hostIp, 8899)

    override val output = PrintWriter(client.getOutputStream(), true)
    override val input = BufferedReader(InputStreamReader(client.inputStream))

    init {
        output.println("{\"name\": \"info\",\"args\":[]}")
    }

}

class Server (
    override val port: Int,
    override val hostIp: String
) : Connector {
    val server = ServerSocket(8899)
    val client = server.accept()

    override val output = PrintWriter(client.getOutputStream(), true)
    override val input = BufferedReader(InputStreamReader(client.inputStream))

    init {
        println("server launched at: ${server.inetAddress.hostAddress}")
    }

}