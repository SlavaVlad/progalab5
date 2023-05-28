package app.server.persistence.utils

import app.server.handleCommand
import app.server.repo
import com.fasterxml.jackson.databind.ObjectMapper
import persistence.checkerComponent.Command
import persistence.data.ExecutionResult
import java.io.*
import java.net.ServerSocket
import java.net.Socket

interface Connector {
    val port: Int
    val output: PrintWriter
    val input: BufferedReader

    val objectMapper: ObjectMapper
        get() = ObjectMapper()

    fun close() {
        output.close()
        input.close()
    }
}

class Client(
    override val port: Int,
    val hostIp: String
) : Connector {
    private val client = Socket(hostIp, 8899)

    override val output = PrintWriter(client.getOutputStream(), true)
    override val input = BufferedReader(InputStreamReader(client.inputStream))

    init {
        output.println("{\"name\": \"info\",\"args\":[]}")
    }

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
            val receivedMessage = input.readLine()
            if (receivedMessage != null) {
                val command = try {
                    objectMapper.readValue(receivedMessage, Command::class.java)
                } catch (e: Exception) {
                    null
                }
                if (command != null) {
                    handleCommand(command, repo) { result ->
                        sendMessage(result)
                    }
                }
            }
        }
    }

    fun sendMessage(message: Any) {
        val json = objectMapper.writeValueAsString(message)
        output.println(json)
    }

}