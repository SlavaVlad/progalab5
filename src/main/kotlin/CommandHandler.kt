package app

import app.app.database.ProductCollectionInfo
import app.checkerComponent.Command
import app.data.CommandInfo
import app.data.ExecutionResult
import app.database.ProductFabric
import app.database.ProductRepository
import app.database.product.Product
import utils.ConsoleColors
import utils.readlinesFile
import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

import java.io.OutputStream

class CommandHandler(val repo: ProductRepository) {

    fun groupByPrice(
        onCompleted: (ExecutionResult) -> Unit = {
            it.error?.let { println(it) }
            it.message?.let { println(it) }
        }
    ) {
        var result = ""
        val groups = repo.getProducts().groupBy { it.price }
        for ((price, products) in groups) {
            result += ("Продукты с ценой $price: ${products.joinToString { it.id.toString() }}")
        }

        onCompleted(ExecutionResult(message = result))
    }

    fun filterStartsWithPartNumber(
        substring: String, onCompleted: (ExecutionResult) -> Unit = {
            it.error?.let { println(it) }
            it.message?.let { println(it) }
        }
    ) {
        val list = repo.filter { it.partNumber?.startsWith(substring) ?: false }
        var result = ""
        list.forEach {
            result += it.toString()
        }

        onCompleted(ExecutionResult(message = result))
    }

    fun filterGreaterThanPrice(
        price: Double, onCompleted: (ExecutionResult) -> Unit = {
            it.error?.let { println(it) }
            it.message?.let { println(it) }
        }
    ) {
        val list = repo.filter { ((it.price ?: 0) > price) }
        var result = ""
        list.forEach {
            result += it.toString()
        }

        onCompleted(ExecutionResult(message = result))
    }

//    fun makeConfig(
//        filename: String, onCompleted: (ExecutionResult) -> Unit = {
//            it.error?.let { println(it) }
//            it.message?.let { println(it) }
//        }
//    ) {
//        val mapper = ObjectMapper().registerKotlinModule()
//        val file = File("E:\\Projects\\Projects\\progalab5\\src\\$filename").apply {
//            createNewFile()
//        }
//        val classString = mapper.writeValueAsString(CommandInfo.commands)
//        file.printWriter().apply {
//            println(classString)
//            this.close()
//        }
//        onCompleted(ExecutionResult(message = "result string = \"$classString\""))
//   }

}