package persistence.data

import persistence.database.utils.ProductCollectionInfo
import persistence.checkerComponent.Argument
import persistence.checkerComponent.CommandReference
import persistence.checkerComponent.ScriptExecutor
import persistence.console.CPT
import persistence.database.ProductFabric
import persistence.database.ProductRepository
import persistence.database.product.Product
import app.server.handleCommand
import persistence.utils.ConsoleColors
import persistence.utils.readlinesFile

class CommandInfo(private val repo: ProductRepository) {
    var commands = hashMapOf(
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
                repo.addProduct(ProductFabric.constructProductFromConsole()!!)
            } catch (e: Exception) {
                onCompleted(ExecutionResult(error = e.message))
            }
            onCompleted(ExecutionResult(message = "Element added"))
        },
        "update" to CommandReference(
            description = "Command that updates element with specified id",
            arguments = listOf(
                Argument("id", CPT.INTEGER, "Id of element to update")
            )
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
            listOf(
                Argument("id", CPT.INTEGER, "Id of element to delete")
            )
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
            listOf(
                Argument("filepath", CPT.STRING, "abs filepath to save collection to")
            )
        ) { args, onCompleted ->
            val filename = args[0]
            if (repo.saveToFile(filename)) {
                onCompleted(ExecutionResult(message = "Collection saved to $filename"))
            } else {
                onCompleted(ExecutionResult(error = "Error while saving collection to $filename"))
            }
        },
        "execute_script" to CommandReference(
            description = "Command that executes script from file with command sequence. Include checks and recursion check.",
            listOf(
                Argument("filepath", CPT.STRING, "abs filepath to script"),
                Argument("skip_recursion_check", CPT.BOOL, "Skips bruteforce recursion check"),
            )
        ) { args, onCompleted ->
            val filepath = args[0]
            val lines = readlinesFile(filepath)
            ScriptExecutor(lines, repo.copy()).check(onError = { error ->
                onCompleted(ExecutionResult(error = error))
            }, onSuccess = { log, commands ->
                commands.forEach {
                    handleCommand(it, repo)
                }
                onCompleted(ExecutionResult(message = "Script executed"))
            }, skipResursion = args[1].toBoolean())
        },
        "add_if_max" to CommandReference(
            description = "Command that adds new element to collection if it's price is greater than max price in collection"
        ) { args, onCompleted ->
            val product1 = ProductFabric.constructProductFromConsole()?.let {
                if (repo.compareMax(it)) {
                    repo.addProduct(it)
                }
            }.run {
                if (this == null) {
                    onCompleted(ExecutionResult(error = "Error while adding element"))
                } else {
                    onCompleted(ExecutionResult(message = "Element added"))
                }
            }
            onCompleted(ExecutionResult())
        },
        "add_if_min" to CommandReference(
            description = "Command that adds new element to collection if it's price is less than max price in collection"
        ) { args, onCompleted ->
            val product1 = ProductFabric.constructProductFromConsole()?.let {
                if (repo.compareMin(it)) {
                    repo.addProduct(it)
                }
            }.run {
                if (this == null) {
                    onCompleted(ExecutionResult(error = "Error while adding element"))
                } else {
                    onCompleted(ExecutionResult(message = "Element added"))
                }
            }
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
                Argument("price", CPT.INTEGER, "Price to group by")
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
                Argument("substring", CPT.STRING, "Substring to check")
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
                Argument("price", CPT.INTEGER, "Price to check")
            )
        ) { args, onCompleted ->
            var msg = ""
            repo.getProducts().filter { (it.price ?: -1) > args[0].toInt() }.forEach {
                msg += it.toString()
            }
            onCompleted(ExecutionResult(message = msg))
        }
    )

    val runtimeCommands = hashMapOf<String, CommandReference>(
        "add_command" to CommandReference(
            description = "Command that adds new command", arguments = listOf(
                Argument("name", CPT.STRING, description = "Name of command"),
                Argument("description", CPT.STRING, description = "Description of command"),
            )
        ) { args, onCompleted ->
            try {
                commands[args[0]] = CommandReference(description = "Command ad") { args, onCompleted ->
                    onCompleted(ExecutionResult(message = "Command added at runtime"))
                }
            } catch (e: Exception) {
                onCompleted(ExecutionResult(error = e.message))
            }
            onCompleted(ExecutionResult(message = "Command added"))
        },
        "help" to CommandReference(
            description = "Manual for all commands"
        ) { args, onCompleted ->
            var msg = "Available commands:\n"
            commands.forEach { (name, command) ->
                var argumentsString = "\n"
                command.arguments?.forEach {
                    argumentsString += "    ${ConsoleColors.ANSI_CYAN}(${it.name}: ${it.type.name})${ConsoleColors.ANSI_RESET} - ${ConsoleColors.ANSI_GREEN}${it.description}${ConsoleColors.ANSI_RESET}\n"
                }
                // list of all commands in format: commandName - description and list of arguments from argumentsString
                msg += if ((command.arguments?.size ?: 0) == 0) {
                    "${ConsoleColors.ANSI_BLUE}$name${ConsoleColors.ANSI_RESET}() - ${ConsoleColors.ANSI_LIGHT_GREEN}${command.description}${ConsoleColors.ANSI_RESET}\n"
                } else {
                    "${ConsoleColors.ANSI_BLUE}$name${ConsoleColors.ANSI_RESET}($argumentsString) - ${ConsoleColors.ANSI_LIGHT_GREEN}${command.description}${ConsoleColors.ANSI_RESET}\n"
                }
            }

            onCompleted(ExecutionResult(message = msg))
        }
    )

    init {
        commands += runtimeCommands
    }

    fun findReferenceOrNull(commandName: String): CommandReference? {
        return commands[commandName]
    }

}