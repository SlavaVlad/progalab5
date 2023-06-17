package app.common.persistence.utils

import app.common.handleCommand
import app.common.repo
import com.fasterxml.jackson.databind.ObjectMapper
import app.common.server.Command
import persistence.data.ExecutionResult
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread
import app.common.client.Command as ClientCommand

interface Connector {
    val port: Int
//    val output: PrintWriter
//    val input: BufferedReader

    val objectMapper: ObjectMapper
        get() = ObjectMapper()

}

class Client(
    override val port: Int,
    hostIp: String
) : Connector {
    private var client: Socket? = null
    var output: PrintWriter? = null
    var input: BufferedReader? = null

    init {
        connect(hostIp) // Вызов функции подключения при создании объекта Client
    }

    private fun connect(hostIp: String) {
        while (true) {
            try {
                client = Socket(hostIp, 8899)
                output = client?.getOutputStream()?.let { PrintWriter(it, true) }!!
                input = client?.getInputStream()?.let { InputStreamReader(it) }?.let { BufferedReader(it) }!!
                break // Выход из цикла при успешном подключении
            } catch (e: Exception) {
                // В случае исключения ждем 2000 миллисекунд перед повторной попыткой подключения
                Thread.sleep(2000)
                println("waiting for server")
            }
        }
    }

    fun sendCommand(command: ClientCommand) {
        val json = objectMapper.writeValueAsString(command)
        output?.println(json)
    }

    fun awaitMessageOrResult(onStringMessage: (String) -> Unit, onExecutionResult: (ExecutionResult) -> Unit) {
        while (true) {
            try {
                val receivedMessage = input?.readLine()
                if (receivedMessage != null) {
                    val executionResult = try {
                        objectMapper.readValue(receivedMessage, ExecutionResult::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    if (executionResult != null) {
                        // Если получен объект ExecutionResult
                        onExecutionResult(executionResult)
                    } else {
                        // Если получена простая строка
                        onStringMessage(receivedMessage)
                    }
                }
            } catch (e: SocketException) {
                Thread.sleep(2000)
                // wait for reconnect
            }
        }
    }

}

class Server(
    override val port: Int,
) : Connector {
    private val server = ServerSocket(8899)
    private val client = server.accept()

    val output = PrintWriter(client.getOutputStream(), true)
    val input = BufferedReader(InputStreamReader(client.inputStream))

    init {
        println("server launched at: ${server.inetAddress.hostAddress}")
    }

    fun awaitCommand() {
        while (true) {
            val client = server.accept()
            thread {
                val output = PrintWriter(client.getOutputStream(), true)
                val input = BufferedReader(InputStreamReader(client.inputStream))

                fun sendMessage(message: Any) {
                    val json = objectMapper.writeValueAsString(message)
                    output.println(json)
                }

                while (true) {
                    val receivedMessage = input.readLine()
                    if (receivedMessage != null) {
                        try {
                            val command = objectMapper.readValue(receivedMessage, Command::class.java)
                            handleCommand(command, repo) { result ->
                                sendMessage(result)
                            }
                        } catch (e: Exception) {
                            sendMessage("Error, ${e.message}; ${e.printStackTrace()}")
                        }
                    }
                }
            }
        }
    }
}
