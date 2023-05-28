package persistence.database//package app.database

import java.util.*
import persistence.database.loader.CollectionLoader
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import persistence.database.product.Product

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
