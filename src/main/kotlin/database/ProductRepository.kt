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
import java.util.function.Predicate
import kotlin.time.measureTime

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

    fun saveToFile(filename: String): Boolean {
        CollectionLoader.save(getProducts().toList(), filename)
        return true
    }

    fun loadCollectionFromFile(filename: String): List<Product> {
        return CollectionLoader.load(filename)
    }

    fun removeProductById(id: Long): Boolean {
        val product = products.firstOrNull { it.id == id }
        return if (product != null) {
            products.remove(product)
            true
        } else {
            false
        }
    }

    fun getSize(): Int {
        return products.size
    }

    fun compareMax(product: Product): Boolean {
        return products.comparator().compare(product, products.max()) > 0
    }
    fun compareMin(product: Product): Boolean {
        return products.comparator().compare(product, products.max()) < 0
    }
    fun removeAllGreaterThan(product: Product): Int {
        var count = 0
        while (products.comparator().compare(product, products.max()) > 0) {
            products.remove(products.max())
            count++
        }
        return count
    }

    fun filter(predicate: (Product) -> Boolean) = products.filter { predicate(it) }

}

fun <T> Iterable<T>.averageBy(selector: (T) -> Number): Double {
    var sum = 0.0
    var count = 0
    for (element in this) {
        sum += selector(element).toDouble()
        count++
    }
    return if (count > 0) sum / count else 0.0
}
