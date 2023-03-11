package app.console

import java.util.regex.Pattern

object ConsolePrimitiveType {
    val boolean = Pattern.compile("[true|false]")
    val string = Pattern.compile("[A-z ]")
    val int = Pattern.compile("[+-]?[0-9]*")
    val double = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+")
    val element = "element input"
}