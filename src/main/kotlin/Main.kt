package app.common

import app.common.persistence.utils.Client
import app.common.persistence.utils.Server
import app.common.server.Command
import app.common.client.Log
import app.common.server.printToConsole
import persistence.console.CPT
import app.common.server.CommandInfoServer
import persistence.data.ExecutionResult
import persistence.database.ProductRepository
import persistence.utils.ConsoleColors
import persistence.utils.makeInput
import persistence.utils.printlnc
import kotlin.concurrent.thread


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
                    val formattedArgs = makeInput(input)
                    val command: Command? = Command.make(formattedArgs) { ref ->
                        return@make ref.preCompile.invoke(formattedArgs.toList())
                    }
                    command?.let { client.sendCommand(it) }
                }
            }
        }
    }
}


fun checkForPayload(formattedArgs: Array<String>): Boolean {
    return ((CommandInfoServer().findReferenceOrNull(formattedArgs[0])?.arguments?.any { it.type == CPT.JSON })?.and(
        (CommandInfoServer().findReferenceOrNull(
            formattedArgs[0]
        )?.arguments?.indexOfFirst { it.type == CPT.JSON }
            ?.let { formattedArgs.getOrNull(it) } == null)) == true)
}

fun launchServer() {
    val server = Server(
        arguments[1].toIntOrNull() ?: 8899
    )
    server.awaitCommand()
}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun handleCommand(
    command: Command,
    repo: ProductRepository,
    callback: (ExecutionResult) -> Unit = { _ -> },
) {
    val ref = CommandInfoServer(repo)
        .findReferenceOrNull(command.name!!)
    ref?.function
        ?.invoke(command.args!!.toList(), callback)
}
