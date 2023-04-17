package app.database

import app.database.product.*
import utils.requestUserInput

object ProductFabric {
    private fun constructCoordinatesFromConsole(): Coordinates {
        println("Enter product coordinates:")
        val x = requestUserInput("Enter x coordinate:") {
            it.toLongOrNull() != null
        }.toLong()

        val y = requestUserInput("Enter y coordinate:") {
            it.toIntOrNull() != null
        }.toInt()

        return Coordinates(x, y)
    }

    private fun constructUnitOfMeasureFromConsole(): UnitOfMeasure {
        println("Enter unit of measure (cm, l, g, kg):")
        val unitOfMeasure = readLine() ?: throw IllegalArgumentException("unit of measure cannot be null or empty")

        return when (unitOfMeasure) {
            "kg" -> UnitOfMeasure.KILOGRAMS
            "g" -> UnitOfMeasure.GRAMS
            "l" -> UnitOfMeasure.LITERS
            "cm" -> UnitOfMeasure.CENTIMETERS
            else -> { throw IllegalArgumentException("unexpected unit of measure") }
        }
    }

    private fun constructPersonFromConsole(): Person {
        println("Enter owner's name:")
        val name = readLine() ?: throw IllegalArgumentException("owner's name cannot be null or empty")

        println("Enter owner's height:")
        val height = readLine()?.toLongOrNull() ?: throw IllegalArgumentException("owner's height cannot be null or empty")

        println("Enter owner's weight:")
        val weight = readLine()?.toFloatOrNull() ?: throw IllegalArgumentException("owner's weight cannot be null or empty")

        println("Enter owner's location:")
        val location = constructLocationFromConsole()

        return Person(name = name, height = height, weight = weight, location = location)
    }

    private fun constructLocationFromConsole(): Location {
        println("Enter location x:")
        val x = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("location x cannot be null or empty")

        println("Enter location y:")
        val y = readLine()?.toLongOrNull() ?: throw IllegalArgumentException("location y cannot be null or empty")

        println("Enter location z:")
        val z = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("location z cannot be null or empty")

        return Location(x, y, z)
    }

    fun constructProductFromConsole(): Product {

        println("Enter product name:")
        val name = readLine() ?: throw IllegalArgumentException("product name cannot be null or empty")

        println("Enter product coordinates:")
        val coordinates = constructCoordinatesFromConsole()

        println("Enter product price:")
        val price = readLine()?.toLongOrNull() ?: throw IllegalArgumentException("product price cannot be null or empty")

        println("Enter product part number:")
        val partNumber = readLine() ?: throw IllegalArgumentException("product part number cannot be null or empty")

        println("Enter product unit of measure:")
        val unitOfMeasure = constructUnitOfMeasureFromConsole()

        println("Enter product owner:")
        val owner = constructPersonFromConsole()

        return Product(name = name, coordinates = coordinates, price = price, partNumber = partNumber, unitOfMeasure = unitOfMeasure, owner = owner)
    }

    fun constructProductFromConsoleWithId(id: Long): Product {
        val name = requestUserInput("Enter product name:")

        val coordinates = constructCoordinatesFromConsole()

        println("Enter product price:")
        val price = readLine()?.toLongOrNull() ?: throw IllegalArgumentException("product price cannot be null or empty")

        println("Enter product part number:")
        val partNumber = readLine() ?: throw IllegalArgumentException("product part number cannot be null or empty")

        println("Enter product unit of measure:")
        val unitOfMeasure = constructUnitOfMeasureFromConsole()

        println("Enter product owner:")
        val owner = constructPersonFromConsole()

        return Product(id = id, name = name, coordinates = coordinates, price = price, partNumber = partNumber, unitOfMeasure = unitOfMeasure, owner = owner)
    }

}