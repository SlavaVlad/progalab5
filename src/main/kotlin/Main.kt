package app.server

import app.server.persistence.utils.Client
import app.server.persistence.utils.Server
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import persistence.checkerComponent.Command
import persistence.checkerComponent.Log
import persistence.checkerComponent.printToConsole
import persistence.data.CommandInfo
import persistence.data.ExecutionResult
import persistence.database.ProductRepository
import persistence.utils.ConsoleColors
import persistence.utils.makeInput
import persistence.utils.printlnc
import kotlin.concurrent.thread
import kotlin.system.exitProcess


var repo: ProductRepository = ProductRepository()
lateinit var arguments: Array<String>


fun main(args: Array<String>) {
    arguments = args
    when (args[0]) {
        "-server" -> { // -server port
            launchServer()
        }

        "-client" -> {
            launchClient() // -client serverip port
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

    thread {
        client.awaitMessageOrResult(
            onStringMessage = { println("message: $it") },
            onExecutionResult = { printToConsole(it) }
        )
    }

    thread {
        while (true) {
            readlnOrNull().let { input ->
                if (input != null) {
                    val command: Command = (Command.make(makeInput(input)) ?: (error("Command not found"))) as Command
                    client.sendCommand(command)
                }
            }
        }
    }

}

fun launchServer() {
    val server = Server(
        arguments[1].toIntOrNull() ?: 5000
    )

    thread {
        server.awaitCommand()
        server.sendMessage(
            Message(
                "Server ${arguments[0]}:${arguments[1].toInt()}", "Server is ready"
            )
        )
    }

}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun handleCommand(
    command: Command,
    repo: ProductRepository,
    payload: Any? = null,
    callback: (ExecutionResult) -> Unit = { _ -> },
) {
    val ref = CommandInfo(repo)
        .findReferenceOrNull(command.name!!)!!
    ref.function
        .invoke(payload, command.args!!.toList(), callback)
}
