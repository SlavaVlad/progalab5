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
