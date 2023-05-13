package app.database.loader

import app.database.ProductRepository
import app.database.product.Product
import java.util.*

interface CollectionSaveLoader {
    fun load(filename: String): TreeSet<Product>
    fun save(input: List<Product>, filename: String)
}