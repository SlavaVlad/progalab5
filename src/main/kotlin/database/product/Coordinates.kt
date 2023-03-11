package app.database.product

import kotlinx.serialization.Serializable

@Serializable
class Coordinates (
    private val x //Поле не может быть null
            : Long? = null,
    private val y //Значение поля должно быть больше -143, Поле не может быть null
            : Int? = null,
) {
    override fun toString(): String {
        return "$x, $y"
    }
}