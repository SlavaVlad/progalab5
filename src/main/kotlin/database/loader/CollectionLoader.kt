package app.database.loader

import app.database.product.*
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.PrintWriter
import java.util.*

object CollectionLoader: CollectionSaveLoader {
    override fun load(filename: String): List<Product> {
        val yaml = Yaml.default
        val text = File(filename).readText()
        return yaml.decodeFromString(text)
    }

    override fun save(input: List<*>, filename: String) {
        val yaml = Yaml.default
        val text = yaml.encodeToString(input)
        File(filename).writeText(text)
    }

}