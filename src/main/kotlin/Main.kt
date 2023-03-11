package app

import app.checkerComponent.Command
import utils.ConsoleColors
import utils.printlnc

fun main() {
    Database()
    while (true) {
        readLine().let {
            if (it != null) {
                Command.make(it.split(" ").toTypedArray())?.let { handleCommand(it) }
            }
        }
    }
}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun handleCommand(command: Command) {
    with(CommandHandler) {
        when (command.command) {
            "help" -> help()
        }
    }
}
