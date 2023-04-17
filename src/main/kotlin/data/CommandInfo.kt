package app.data

import app.app.database.ProductCollectionInfo
import app.checkerComponent.Command
import app.checkerComponent.CommandReference
import app.console.CPT
import app.database.ProductFabric
import app.database.ProductRepository
import app.database.product.Product
import app.handleCommand
import utils.readlinesFile


val HELP_STRING = """
    help - Manual for all commands
    info - Command that shows information about collection
    show - Command that shows all elements of collection
    add - Command that adds new element to collection
    update - Command that updates element with specified id
    remove_by_id - Command that removes element with specified id
    clear - Command that removes all elements from collection
    save - Command that saves collection to file
    execute_script - Command that executes script from file
    exit - Command that exits program
""".trimIndent()

class CommandInfo(private val repo: ProductRepository) {
    val commands = hashMapOf(
        "help" to CommandReference(
            description = "Manual for all commands"
        ) { args, onCompleted ->

            onCompleted(ExecutionResult(message = HELP_STRING))
        },
        "info" to CommandReference(description = "Command that shows information about collection") { args, onCompleted ->
            onCompleted(ExecutionResult(message = ProductCollectionInfo(repo).toString()))
        },
        "show" to CommandReference(description = "Command that shows all elements of collection") { args, onCompleted ->
            var msg = ""
            repo.getProducts().forEach {
                msg += it.toString()
            }
            onCompleted(ExecutionResult(message = msg))
        },
        "add" to CommandReference(description = "Command that adds new element to collection") { args, onCompleted ->
            try {
                repo.addProduct(ProductFabric.constructProductFromConsole())
            } catch (e: Exception) {
                onCompleted(ExecutionResult(error = e.message))
            }
            onCompleted(ExecutionResult(message = "Element added"))
        },
        "update" to CommandReference(
            description = "Command that updates element with specified id",
            arguments = listOf(CPT.int)
        ) { args, onCompleted ->
            try {
                repo.removeProductById(args[0].toInt().toLong())
                repo.addProduct(ProductFabric.constructProductFromConsoleWithId(args[0].toInt().toLong()))
            } catch (e: Exception) {
                onCompleted(ExecutionResult(error = e.message))
            }
            onCompleted(ExecutionResult(message = "Element updated"))
        },
        "remove_by_id" to CommandReference(
            description = "Command that removes element with specified id",
            listOf(CPT.int)
        ) { args, onCompleted ->
            try {
                if (!repo.removeProductById(args[0].toLong())) throw Exception("Element with id ${args[0]} not found")
            } catch (e: Exception) {
                onCompleted(ExecutionResult(error = e.message))
            }
            onCompleted(ExecutionResult(message = "Element removed"))
        },
        "clear" to CommandReference(description = "Command that clears collection") { args, onCompleted ->
            repo.clear()
            onCompleted(ExecutionResult(message = "Collection cleared"))
        },
        "save" to CommandReference(
            description = "Command that saves collection to file",
            listOf(CPT.string)
        ) { args, onCompleted ->
            val filename = args[0]
            if (repo.saveToFile(filename)) {
                onCompleted(ExecutionResult(message = "Collection saved to $filename"))
            } else {
                onCompleted(ExecutionResult(error = "Error while saving collection to $filename"))
            }
        },
        "execute_script" to CommandReference(
            description = "Command that executes script from file with command sequence",
            listOf(CPT.string)
        ) { args, onCompleted ->
            val filepath = args[0]
            var line = 0
            try {
                readlinesFile(filepath)
                    .forEachIndexed { i, it ->
                        line = i
                        Command.make(it.split(" ").toTypedArray())?.let { handleCommand(it) }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                onCompleted(ExecutionResult(error = "Exception occurred, command execution interrupted at line $line"))
            }
            onCompleted(ExecutionResult(message = "Script executed"))
        },
        "add_if_max" to CommandReference(
            description = "Command that adds new element to collection if it's price is greater than max price in collection"
        ) { args, onCompleted ->
            val product1 = ProductFabric.constructProductFromConsole()
            if (repo.compareMax(product1)) {
                repo.addProduct(product1)
            }
            onCompleted(ExecutionResult())
        },
        "add_if_min" to CommandReference(
            description = "Command that adds new element to collection if it's price is less than max price in collection"
        ) { args, onCompleted ->
            val product1 = ProductFabric.constructProductFromConsole()
            if (repo.compareMin(product1)) {
                repo.addProduct(product1)
            }
            onCompleted(ExecutionResult())
        },
        "remove_greater" to CommandReference(
            description = "Command that removes all elements that are greater than specified"
        ) { args, onCompleted ->
            var prod: Product? = null
            try {
                prod = ProductFabric.constructProductFromConsole()
            } catch (e: Exception) {
                onCompleted(ExecutionResult(error = e.message))
            }

            onCompleted(ExecutionResult(message = "Removed ${prod?.let { repo.removeAllGreaterThan(it) }} elements"))
        },
        "group_counting_by_price" to CommandReference(
            description = "Command that groups elements by price and shows count of elements in each group",
            arguments = listOf(
                CPT.int
            )
        ) { args, onCompleted ->
            var msg = ""
            repo.getProducts().groupBy { args[0] }.forEach {
                msg += "Price: ${it.key}, count: ${it.value.size}\n"
            }
            onCompleted(ExecutionResult(message = msg))
        },
        "filter_starts_with_part_number" to CommandReference(
            description = "Command that shows elements that have partNumber that starts with specified substring",
            arguments = listOf(
                CPT.string
            )
        ) { args, onCompleted ->
            var msg = ""
            repo.getProducts().filter { it.partNumber?.startsWith(args[0]) == true }.forEach {
                msg += it.toString()
            }
            onCompleted(ExecutionResult(message = msg))
        },
        "filter_greater_than_price" to CommandReference(
            description = "Command that shows elements that have price greater than specified",
            arguments = listOf(
                CPT.int
            )
        ) { args, onCompleted ->
            var msg = ""
            repo.getProducts().filter { (it.price ?: -1) > args[0].toInt() }.forEach {
                msg += it.toString()
            }
            onCompleted(ExecutionResult(message = msg))
        }
    )

    fun findReferenceOrNull(name: String): CommandReference? {
        return commands[name]
    }
}