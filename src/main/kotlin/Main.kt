package app

import app.checkerComponent.Command
import app.checkerComponent.printToConsole
import app.data.CommandInfo
import app.database.ProductRepository
import app.database.loader.CollectionLoader
import app.database.product.Product
import kotlinx.serialization.Contextual
import utils.ConsoleColors
import utils.makeInput
import utils.printlnc
import java.util.*

var repo: ProductRepository? = null

fun main(args: Array<String>) {
    repo = ProductRepository((CollectionLoader.load(args[0])?: TreeSet<Product>()))
    while (true) {
        readlnOrNull().let {
            if (it != null) {
                try {
                    handleCommand(Command
                        .make(makeInput(it))!!, repo!!)
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
