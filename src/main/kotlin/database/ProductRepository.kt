package app.database//package app.database

import app.database.product.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import kotlin.math.roundToLong
import app.database.loader.CollectionLoader
import app.database.product.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*
import java.util.function.Predicate
import kotlin.time.measureTime

@Serializable
data class ProductRepository(private val products: @Contextual TreeSet<Product> = TreeSet<Product>()) {

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
        CollectionLoader.save(this.getProducts().toList(), filename)
        return true
    }

    fun loadCollectionFromFile(filename: String): List<Product>? {
        return CollectionLoader.load(filename).toList()
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
