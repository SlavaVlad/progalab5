package app

import java.time.ZonedDateTime

class Dragon {
    private val id //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
            : Long? = null
    private val name //Поле не может быть null, Строка не может быть пустой
            : String? = null
    private val coordinates //Поле не может быть null
            : Coordinates? = null
    private val creationDate //Поле не может быть null, Значение этого поля должно генерироваться автоматически
            : ZonedDateTime? = null
    private val age //Значение поля должно быть больше 0
            : Long = 0
    private val description //Поле может быть null
            : String? = null
    private val speaking //Поле не может быть null
            : Boolean? = null
    private val type //Поле может быть null
            : DragonType? = null
    private val killer //Поле может быть null
            : Person? = null
}

class Coordinates {
    private val x = 0.0
    private val y: Int? = null //Значение поля должно быть больше -994, Поле не может быть null
}

class Person {
    private val name //Поле не может быть null, Строка не может быть пустой
            : String? = null
    private val birthday //Поле может быть null
            : ZonedDateTime? = null
    private val passportID //Поле может быть null
            : String? = null
    private val eyeColor //Поле не может быть null
            : Color? = null
}

enum class DragonType {
    UNDERGROUND, AIR, FIRE
}

enum class Color {
    RED, BLUE, YELLOW, ORANGE, WHITE
}