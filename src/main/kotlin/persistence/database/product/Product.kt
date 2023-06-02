package persistence.database.product

import kotlinx.serialization.Contextual
import java.time.Instant
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Product(
    val id //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    : Long? = System.currentTimeMillis(),
    val name0 //Поле не может быть null, Строка не может быть пустой
    : String? = null,
    val coordinates //Поле не может быть null
    : Coordinates? = null,
    val price //Поле не может быть null, Значение поля должно быть больше 0
    : Long? = null,
    @Contextual
    val creationDate //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    : Date = Date.from(Instant.now()),
    val partNumber //Длина строки должна быть не меньше 13, Значение этого поля должно быть уникальным, Поле не может быть null
    : String? = null,
    val unitOfMeasure //Поле не может быть null
    : UnitOfMeasure? = null,
    val owner //Поле может быть null
    : Person? = null,
) : Comparable<Product> {

    override fun compareTo(other: Product): Int {
        return this.price!!.compareTo(other.price!!)
    }

    override fun toString(): String {
        return "Product(id=$id, name=$name0, coordinates=$coordinates, price=$price, creationDate=$creationDate, partNumber=$partNumber, unitOfMeasure=$unitOfMeasure, owner=$owner)"
    }
    
    fun getWriterString(): String {
        return ("\n${id},${name0},${coordinates?.x},${coordinates?.y},${price},${partNumber},${unitOfMeasure},${owner?.name},${owner?.height},${owner?.weight},${owner?.location?.x},${owner?.location?.y},${owner?.location?.z}")
    }

}
