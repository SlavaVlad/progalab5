package persistence.database.loader

import persistence.database.product.*
import java.io.*
import java.util.*
import kotlin.math.roundToLong

object CollectionLoader : CollectionSaveLoader {
    override fun load(filepath: String): TreeSet<Product> {
        val reader = FileInputStream(filepath).bufferedReader()
        val header = reader.readLine()
        return TreeSet(reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val a = it.split(
                    ',',
                    ignoreCase = false
                ).toMutableList()
                a.forEachIndexed { i, s ->
                    a[i] = s.filter { it != " ".toCharArray()[0] }
                }
                Product(
                    id = a[0].toLong(),
                    name = a[1],
                    coordinates = Coordinates(a[2].toLong(), a[3].toInt()),
                    price = a[4].toDouble().roundToLong(),
                    partNumber = a[5],
                    unitOfMeasure = UnitOfMeasure.valueOf(a[6]),
                    owner = Person(
                        name = a[7],
                        height = a[8].toDouble().roundToLong(),
                        weight = a[9].toFloat(),
                        location = Location(
                            a[10].toInt(),
                            a[11].toLong(),
                            a[12].toInt()
                        )
                    )
                )
            }.toList()
        )
    }

    override fun save(input: List<Product>, filepath: String) {
        val writer = FileWriter(filepath)
        writer.write("id,name,coordinates_x,coordinates_y,price,partNumber,unitOfMeasure,owner_name,owner_height,owner_weight,owner_location_x,owner_location_y,owner_location_z")
        input.forEach{
                writer.write(it.getWriterString())
            }
        writer.close()
    }

}