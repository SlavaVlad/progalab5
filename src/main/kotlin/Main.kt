package app

import app.checkerComponent.Command
import app.checkerComponent.printToConsole
import app.data.CommandInfo
import app.database.ProductRepository
import utils.ConsoleColors
import utils.makeInput
import utils.printlnc

val repo = ProductRepository()
val ch = CommandHandler(repo)

fun main() {
    while (true) {
        readlnOrNull().let {
            if (it != null) {
                try {
                    handleCommand(Command
                        .make(makeInput(it))!!, repo)
                } catch (e: Exception) {
                    error(e.message ?: "Error while compiling command")
                }
            }
        }
    }
}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun handleCommand(command: Command, repo: ProductRepository) {
    val ref = CommandInfo(repo)
        .findReferenceOrNull(command.name!!)!!
    ref.function
        .invoke(command.args!!.toList(), ::printToConsole)
}
