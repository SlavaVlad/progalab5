package app.database//package app.database
//
//import app.database.product.*
//import java.io.File
//import java.io.FileInputStream
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.net.URL
//import java.util.*
//import kotlin.math.roundToLong
//
//class CollectionLoader {
//    fun load(filename: String): TreeSet<Product> {
//        val reader = FileInputStream(filename).bufferedReader()
//        val header = reader.readLine()
//        return TreeSet(reader.lineSequence()
//            .filter { it.isNotBlank() }
//            .map {
//                val a = it.split(
//                    ',',
//                    ignoreCase = false
//                )
//                Product(
//                    id = a[0].toLong(),
//                    name = a[1],
//                    coordinates = Coordinates(a[2].toLong(), a[3].toInt()),
//                    price = a[4].toDouble().roundToLong(),
//                    partNumber = a[5],
//                    unitOfMeasure = UnitOfMeasure.valueOf(a[6]),
//                    owner = Person(
//                        name = a[7],
//                        height = a[8].toDouble().roundToLong(),
//                        weight = a[9].toFloat(),
//                        location = Location(
//                            a[10].toInt(),
//                            a[11].toLong(),
//                            a[12].toInt()
//                        )
//                    )
//                )
//            }.toList())
//    }
//}
import app.database.loader.CollectionLoader
import app.database.product.*
import java.util.*

class ProductRepository() {
    private val products = TreeSet<Product>()

    fun getProducts(): Set<Product> {
        return products
    }

    fun addProduct(product: Product) {
        products.add(product)
    }

    fun clear() {
        products.clear()
    }

    fun saveToFile(filename: String) {
        CollectionLoader().save(getProducts().toList(), filename)
    }

    fun loadCollectionFromFile(filename: String): List<Product> {
        return CollectionLoader().load(filename)
    }

    fun removeProductById(id: Long): Boolean {
        val product = products.firstOrNull { it.getId() == id }
        return if (product != null) {
            products.remove(product)
            true
        } else {
            false
        }
    }

    private fun parseCoordinates(xStr: String, yStr: String): Coordinates {
        val x = xStr.toLongOrNull()
        val y = yStr.toIntOrNull()
        return if (x != null && y != null) {
            Coordinates(x, y)
        } else {
            throw IllegalArgumentException("Invalid coordinates: ($xStr, $yStr)")
        }
    }

    private fun parseUnitOfMeasure(unitOfMeasureStr: String): UnitOfMeasure {
        return try {
            UnitOfMeasure.valueOf(unitOfMeasureStr)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid unit of measure: $unitOfMeasureStr")
        }
    }
}