package persistence.database.product

import kotlinx.serialization.Serializable

@Serializable
class Coordinates (
    val x //Поле не может быть null
            : Long? = null,
    val y //Значение поля должно быть больше -143, Поле не может быть null
            : Int? = null,
) {
    override fun toString(): String {
        return "$x, $y"
    }
}