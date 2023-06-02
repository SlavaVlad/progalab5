package app.server

import app.server.persistence.utils.Message
import app.server.persistence.utils.Client
import app.server.persistence.utils.Server
import persistence.checkerComponent.Command
import persistence.checkerComponent.Log
import persistence.checkerComponent.printToConsole
import persistence.console.CPT
import persistence.data.CommandInfo
import persistence.data.ExecutionResult
import persistence.database.ProductRepository
import persistence.utils.ConsoleColors
import persistence.utils.cond
import persistence.utils.makeInput
import persistence.utils.printlnc
import kotlin.concurrent.thread
import kotlin.test.assertTrue


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

/**
 * Эксперты утверждают: в недрах этой функции заложен смысл, который заключается в том,
* чтобы проверять, содержит ли команда в списке аргументов аргумент с типом JSON и что позиция,
* где он должен был быть при вводе команды пуста (null)
 * * P.S.> Это не шутка, это действительно так работает
 * * P. P. S.> Не используется, но оставлю это в моём гитхабе на память о том, что я творил на 1 курсе
 * */
fun checkForPayload(formattedArgs: Array<String>): Boolean {
    return ((CommandInfo().findReferenceOrNull(formattedArgs[0])?.arguments?.any { it.type == CPT.JSON })?.and(
        (CommandInfo().findReferenceOrNull(
            formattedArgs[0]
        )?.arguments?.indexOfFirst { it.type == CPT.JSON }
            ?.let { formattedArgs.getOrNull(it) } == null)) == true)
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
    callback: (ExecutionResult) -> Unit = { _ -> },
) {
    val ref = CommandInfo(repo)
        .findReferenceOrNull(command.name!!)
    ref?.function
        ?.invoke(command.args!!.toList(), callback)
}
