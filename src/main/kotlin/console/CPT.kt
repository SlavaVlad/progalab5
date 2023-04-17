package app.console

import java.util.regex.Pattern

object CPT {
    val boolean = Pattern.compile("[true|false]")
    val string = Pattern.compile(".*")
    val int = Pattern.compile("[+-]?[0-9]*")
    val double = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+")
    val filepath = Pattern.compile("^([a-zA-Z]:)?(\\\\[^<>:\"/\\\\|?*]+)+\\\\?\n")
}