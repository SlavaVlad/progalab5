package persistence.database.loader

import persistence.database.product.Product
import java.util.*

interface CollectionSaveLoader {
    fun load(filename: String): TreeSet<Product>
    fun save(input: List<Product>, filename: String)
}