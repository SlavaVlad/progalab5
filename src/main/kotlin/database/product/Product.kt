package app.database.product

import kotlinx.serialization.Contextual
import java.time.Instant
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Product(
    private val id //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    : Long? = null,
    private val name //Поле не может быть null, Строка не может быть пустой
    : String? = null,
    private val coordinates //Поле не может быть null
    : Coordinates? = null,
    private val price //Поле не может быть null, Значение поля должно быть больше 0
    : Long? = null,
    @Contextual
    private val creationDate //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    : Date = Date.from(Instant.now()),
    private val partNumber //Длина строки должна быть не меньше 13, Значение этого поля должно быть уникальным, Поле не может быть null
    : String? = null,
    private val unitOfMeasure //Поле не может быть null
    : UnitOfMeasure? = null,
    private val owner //Поле может быть null
    : Person? = null,
) : Comparable<Product> {

    init {
        require(id == null || id > 0) { "id must be greater than 0" }
        require(!name.isNullOrBlank()) { "name must not be null or blank" }
        require(coordinates != null) { "coordinates must not be null" }
        require(price != null && price > 0) { "price must be greater than 0" }
        require(partNumber != null && partNumber.length >= 13) { "partNumber must have length at least 13" }
        require(unitOfMeasure != null) { "unitOfMeasure must not be null" }
    }

    override fun compareTo(other: Product): Int {
        return this.id!!.compareTo(other.id!!)
    }

    fun getId(): Long? {
        return id
    }

}
