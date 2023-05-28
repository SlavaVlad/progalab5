package persistence.database.product

import kotlinx.serialization.Serializable

@Serializable
class Person(
    val name //Поле не может быть null, Строка не может быть пустой
    : String? = null,
    val height //Значение поля должно быть больше 0
    : Long = 0,
    val weight: Float //Значение поля должно быть больше 0
    = 0f,
    val location //Поле может быть null
    : Location? = null,
) {
    override fun toString(): String {
        return "$name,$height,$weight,${location.toString()}"
    }
}