package app.database.loader

interface CollectionSaveLoader {
    fun load(filename: String): List<*>
    fun save(input: List<*>, filename: String)
}