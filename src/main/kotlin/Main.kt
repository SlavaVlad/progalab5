package app.server

import app.server.persistence.utils.Client
import app.server.persistence.utils.Server
import com.fasterxml.jackson.databind.ObjectMapper
import persistence.checkerComponent.Command
import persistence.checkerComponent.Log
import persistence.checkerComponent.printToConsole
import persistence.data.CommandInfo
import persistence.data.ExecutionResult
import persistence.database.ProductRepository
import persistence.utils.ConsoleColors
import persistence.utils.makeInput
import persistence.utils.printlnc
import java.util.*
import kotlin.concurrent.thread


var repo: ProductRepository = ProductRepository()
lateinit var arguments: Array<String>


fun main(args: Array<String>) {
    arguments = args
    when (args[0]) {
        "-server" -> { // -server -host -port (mode; port)
            launchServer()
        }

        "-client" -> {
            launchClient() // -client -1337
        }

        else -> {
            Log("Main launcher").e("no launch mode specified")
        }
    }
}

fun launchClient() {
    val client = Client(
        arguments[2].toInt(),
        arguments[1]
    )

    fun sendCommand(command: Command, repo: ProductRepository): String? { // input handler
        CommandInfo(repo)
            .findReferenceOrNull(command.name!!)?.let {
                val mapper = ObjectMapper()
                mapper.writeValue(client.output, command)
            }
        return null
    }

    thread {
        while (true) {
            if (client.input
                .readLine()
                .isNotBlank()) {
                println("hasNext")
                val value = client.input.readText()
                val mapper = ObjectMapper()
                val result = mapper.readValue(value, ExecutionResult::class.java)
                println("client: received result: ${result.message}")
                printToConsole(result)
            }
        }
    }

    thread {
        while (true) {
            readlnOrNull().let {
                if (it != null) {
                    try {
                        sendCommand(
                            Command
                                .make(makeInput(it))!!, repo
                        )
                    } catch (e: Exception) {
                        error(e.message ?: "Error while making command; ${e.printStackTrace()}")
                    }
                }
            }
        }
    }

}

fun launchServer() {

    val server = Server(
        arguments[1].toInt(),
        arguments[0]
    )


    thread {
        val scanner = Scanner(server.input)
        while (true) {
            if (scanner.hasNextLine()) {
                val value = scanner.nextLine()
                val mapper = ObjectMapper()
                val command = mapper.readValue(value, Command::class.java)
                handleCommand(command, repo) {
                    val outMapper = ObjectMapper()
                    outMapper.writeValue(server.output, it)
                    println("server: write to PW")
                }
            }
        }
    }

}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun handleCommand(
    command: Command,
    repo: ProductRepository,
    callback: (ExecutionResult) -> Unit = { _ -> }
) { // input handler
    val ref = CommandInfo(repo)
        .findReferenceOrNull(command.name!!)!!
    ref.function
        .invoke(command.args!!.toList(), callback)
}
