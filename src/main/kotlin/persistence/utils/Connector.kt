package app.common.persistence.utils

import app.common.handleCommand
import app.common.repo
import com.fasterxml.jackson.databind.ObjectMapper
import app.common.server.Command
import persistence.data.ExecutionResult
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

interface Connector {
    val port: Int
    val output: PrintWriter
    val input: BufferedReader

    val objectMapper: ObjectMapper
        get() = ObjectMapper()

}

class Client(
    override val port: Int,
    hostIp: String
) : Connector {
    private val client = Socket(hostIp, 8899)

    override val output = PrintWriter(client.getOutputStream(), true)
    override val input = BufferedReader(InputStreamReader(client.inputStream))

    fun sendCommand(command: Command) {
        val json = objectMapper.writeValueAsString(command)
        output.println(json)
    }

    fun awaitMessageOrResult(onStringMessage: (String) -> Unit, onExecutionResult: (ExecutionResult) -> Unit) {
        while (true) {
            val receivedMessage = input.readLine()
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
        }
    }

}

class Server(
    override val port: Int,
) : Connector {
    private val server = ServerSocket(8899)
    private val client = server.accept()

    override val output = PrintWriter(client.getOutputStream(), true)
    override val input = BufferedReader(InputStreamReader(client.inputStream))

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
