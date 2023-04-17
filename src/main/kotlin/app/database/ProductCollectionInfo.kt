package app.app.database

import app.database.ProductRepository
import app.database.averageBy
import app.database.product.Person
import app.database.product.Product

class ProductCollectionInfo(collection: ProductRepository) {
    val products = collection.getProducts()

    private fun getMostExpensiveProduct(): Product? {
        return products.maxByOrNull { it.price?: -1 }
    }

    fun getAveragePrice(): Double {
        return products.averageBy { it.price?.toDouble() ?: 0.0 }
    }

    fun getOwners(): Set<Person?> {
        return products.map { it.owner }.toSet()
    }

    override fun toString(): String {
        return "Collection size = ${products.size}" +
                "\nMost expensive product is ${getMostExpensiveProduct()?.name} id=${getMostExpensiveProduct()?.id}" +
                "\nAverage products price = ${getAveragePrice()}" +
                "\n owners list: ${getOwners()}"
    }
}
