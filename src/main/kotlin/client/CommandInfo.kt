package app.common.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import persistence.console.CPT
import persistence.database.ProductFabric
import persistence.database.ProductRepository
import persistence.database.product.Product

class CommandInfo(private val repo: ProductRepository = ProductRepository()) {
    private fun addProductToListAsJson(list: List<String>): MutableList<String> {
        val mapper = ObjectMapper()
        val newList = mutableListOf<String>().apply { addAll(list) }
        val jsonArg = mapper.writeValueAsString(ProductFabric.constructProductFromConsole())
        newList.add(jsonArg)
        return newList
    }

    private fun addProductWithIdToListAsJson(list: List<String>, id: Long): MutableList<String> {
        val mapper = ObjectMapper()
        val newList = mutableListOf<String>().apply { addAll(list) }
        val jsonArg = mapper.writeValueAsString(ProductFabric.constructProductFromConsoleWithId(id))
        newList.add(jsonArg)
        return newList
    }

    private fun String.toProduct(): Product {
        val mapper = ObjectMapper().apply {
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
        return mapper.readValue(this, Product::class.java)
    }

    private var commands = hashMapOf(
        "info" to CommandReferenceClient(description = "Command that shows information about collection"),
        "show" to CommandReferenceClient(description = "Command that shows all elements of collection"),
        "add" to CommandReferenceClient(description = "Command that adds new element to collection", arguments = listOf(
            Argument(
                "product", CPT.JSON, "Product to add"
            )
        ), preCompile = {
            addProductToListAsJson(it)
        }),
        "update" to CommandReferenceClient(
            description = "Command that updates element with specified id",
            arguments = listOf(
                Argument("id", CPT.INTEGER, "Id of element to update"),
                Argument("id", CPT.JSON, "element with updated parameters")
            ), preCompile = {
                addProductWithIdToListAsJson(it, it[0].toLong())
            }
        ),
        "remove_by_id" to CommandReferenceClient(
            description = "Command that removes element with specified id",
            listOf(
                Argument("id", CPT.INTEGER, "Id of element to delete")
            )
        ),
        "clear" to CommandReferenceClient(description = "Command that clears collection"),
//        "save" to CommandReferenceClient(
//            description = "Command that saves collection to file",
//            listOf(
//                Argument("filepath", CPT.STRING, "abs filepath to save collection to")
//            )
//        ) { args, onCompleted ->
//            val filename = args[0]
//            if (repo.saveToFile(filename)) {
//                onCompleted(ExecutionResult(message = "Collection saved to $filename"))
//            } else {
//                onCompleted(ExecutionResult(error = "Error while saving collection to $filename"))
//            }
//        },
//        "execute_script" to CommandReferenceClient(
//            description = "Command that executes script from file with command sequence. Include checks and recursion check.",
//            listOf(
//                Argument("filepath", CPT.STRING, "abs filepath to script")
//            ), preCompile = {
//                val filepath = it[0]
//                val lines = readlinesFile(filepath)
//                ScriptExecutorClient(lines).check(onError = { error ->
//                    println("Script is incorrect: $error")
//                }, onSuccess = { log, commands ->
//                    commands.forEach {
//                        handleCommand(it, repo)
//                    }
//                })
//
//                return@CommandReferenceClient emptyList()
//            }),
        "add_if_max" to CommandReferenceClient(
            description = "Command that adds new element to collection if it's price is greater than max price in collection",
            arguments = listOf(
                Argument("product", CPT.JSON, "Product to add if max")
            ),
            preCompile = {
                addProductToListAsJson(it)
            }
        ),
        "add_if_min" to CommandReferenceClient(
            description = "Command that adds new element to collection if it's price is less than max price in collection",
            arguments = listOf(
                Argument("product", CPT.JSON, "Product to add if max")
            ),
            preCompile = {
                addProductToListAsJson(it)
            }
        ),
        "remove_greater" to CommandReferenceClient(
            description = "Command that removes all elements that are greater than specified",
            arguments = listOf(
                Argument("product", CPT.JSON, "Product to add if max")
            ),
            preCompile = {
                addProductToListAsJson(it)
            }
        ),
        "group_counting_by_price" to CommandReferenceClient(
            description = "Command that groups elements by price and shows count of elements in each group",
            arguments = listOf(
                Argument("price", CPT.INTEGER, "Price to group by")
            )
        ),
        "filter_starts_with_part_number" to CommandReferenceClient(
            description = "Command that shows elements that have partNumber that starts with specified substring",
            arguments = listOf(
                Argument("substring", CPT.STRING, "Substring to check")
            )
        ),
        "filter_greater_than_price" to CommandReferenceClient(
            description = "Command that shows elements that have price greater than specified",
            arguments = listOf(
                Argument("price", CPT.INTEGER, "Price to check")
            )
        )
    )

    private val runtimeCommands = hashMapOf(
        "add_command" to CommandReferenceClient(
            description = "Command that adds new command", arguments = listOf(
                Argument("name", CPT.STRING, description = "Name of command"),
                Argument("description", CPT.STRING, description = "Description of command"),
            )
        ),
        "help" to CommandReferenceClient(
            description = "Manual for all commands"
        )
    )

    init {
        commands += runtimeCommands
    }

    fun findReferenceOrNull(commandName: String): CommandReferenceClient? {
        return commands[commandName]
    }

}