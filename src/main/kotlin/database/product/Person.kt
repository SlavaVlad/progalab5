package app.database.product

import kotlinx.serialization.Serializable

@Serializable
class Person(
    private val name //Поле не может быть null, Строка не может быть пустой
    : String? = null,
    private val height //Значение поля должно быть больше 0
    : Long = 0,
    private val weight: Float //Значение поля должно быть больше 0
    = 0f,
    private val location //Поле может быть null
    : Location? = null,
) {
    override fun toString(): String {
        return "$name,$height,$weight,${location.toString()}"
    }
}