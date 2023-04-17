package app

import app.checkerComponent.Command
import app.checkerComponent.printToConsole
import app.data.CommandInfo
import app.database.ProductRepository
import utils.ConsoleColors
import utils.printlnc

val repo = ProductRepository()
val ch = CommandHandler(repo)

fun main() {
    while (true) {
        readlnOrNull().let {
            if (it != null) {
                try {
                    handleCommand(Command
                        .make(makeInput(it))!!)
                } catch (e: Exception) {
                    error(e.message ?: "Error while compiling command")
                }
            }
        }
    }
}

fun makeInput(input: String): Array<String> {
    //val regex = """(?<=\s|^)"([^"]*)"(?=\s|$)|(\S+)""".toRegex()
    val regex = """(?<!\\)".*?(?<!\\)"|\S+""".toRegex()
    return regex.findAll(input)
        .map { it.groupValues[0].removeSurrounding("\"") }
        .toList()
        .toTypedArray()
}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun handleCommand(command: Command) {
    val ref = CommandInfo(repo).findReferenceOrNull(command.name!!)!!
    ref.function.invoke(command.args!!.toList(), ::printToConsole)
}
