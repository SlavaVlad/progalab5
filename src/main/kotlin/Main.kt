package app

import java.io.Console

fun main() {
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
