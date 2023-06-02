package persistence.database

import persistence.database.product.*
import persistence.utils.InputInterruptException
import persistence.utils.requestUserInput

object ProductFabric {
    private fun constructCoordinatesFromConsole(): Coordinates {

        val x = requestUserInput("Enter x coordinate:") {
            it.toLongOrNull() != null
        }.toLong()

        val y = requestUserInput("Enter y coordinate:") {
            it.toIntOrNull() != null
        }.toInt()

        return Coordinates(x, y)
    }

    private fun constructUnitOfMeasureFromConsole(): UnitOfMeasure {
        val unitOfMeasure = requestUserInput("Enter unit of measure (cm, l, g, kg):") {
            it == "cm" || it == "l" || it == "g" || it == "kg"
        }

        return when (unitOfMeasure) {
            "kg" -> UnitOfMeasure.KILOGRAMS
            "g" -> UnitOfMeasure.GRAMS
            "l" -> UnitOfMeasure.LITERS
            "cm" -> UnitOfMeasure.CENTIMETERS
            else -> {
                throw IllegalArgumentException("unexpected unit of measure") // should never happen, but compiler doesn't know that
            }
        }
    }

    private fun constructPersonFromConsole(): Person {
        val name = requestUserInput("Enter owner's name:")

        val height = requestUserInput("Enter owner's height:") { it.toLongOrNull() != null }.toLong()

        val weight = requestUserInput("Enter owner's weight:") { it.toFloatOrNull() != null }.toFloat()


        val location = constructLocationFromConsole()

        return Person(name = name, height = height, weight = weight, location = location)
    }

    private fun constructLocationFromConsole(): Location {
        val x = requestUserInput("Enter location x:") { it.toIntOrNull() != null }.toInt()

        val y = requestUserInput("Enter location y:") { it.toLongOrNull() != null }.toLong()

        val z = requestUserInput("Enter location z:") { it.toIntOrNull() != null }.toInt()

        return Location(x, y, z)
    }


    fun constructProductFromConsole(): Product? {
        try {

            val name = requestUserInput("Enter product name:")

            val coordinates = constructCoordinatesFromConsole()

            val price = requestUserInput("Enter product price:") { it.toLongOrNull() != null }.toLong()

            val partNumber = requestUserInput("Enter product part number:") { it.length >= 13 }

            val unitOfMeasure = constructUnitOfMeasureFromConsole()

            val owner = constructPersonFromConsole()

            return Product(
                id = System.currentTimeMillis(),
                name0 = name,
                coordinates = coordinates,
                price = price,
                partNumber = partNumber,
                unitOfMeasure = unitOfMeasure,
                owner = owner
            )
        } catch (e: InputInterruptException) {
            return null
        }
    }

    fun constructProductFromConsoleWithId(id: Long): Product {
        val name = requestUserInput("Enter product name:")

        val coordinates = constructCoordinatesFromConsole()


        val price = requestUserInput("Enter product price:") { it.toLongOrNull() != null }.toLong()


        val partNumber = requestUserInput("Enter product part number (len >= 13):") { it.length >= 13 }


        val unitOfMeasure = constructUnitOfMeasureFromConsole()


        val owner = constructPersonFromConsole()

        return Product(
            id = id,
            name0 = name,
            coordinates = coordinates,
            price = price,
            partNumber = partNumber,
            unitOfMeasure = unitOfMeasure,
            owner = owner
        )
    }

}