package utils

import java.io.InputStream

/**
 * @param index take elements after index
 */
inline fun <reified T> Array<T>.takeAfter(index: Int): Array<T> {
    val newArray = ArrayList<T>()
    this.forEachIndexed { i, element ->
        if (i > index) {
            newArray.add(element)
        }
    }
    return newArray.toTypedArray()
}

fun printc(string: String, color: String) {
    System.out.print("$color$string${ConsoleColors.ANSI_RESET}")
}

fun printlnc(string: String, color: String) {
    System.out.println("$color$string${ConsoleColors.ANSI_RESET}")
}

object ConsoleColors {
    val ANSI_RESET = "\u001B[0m"
    val ANSI_BLACK = "\u001B[30m"
    val ANSI_RED = "\u001B[31m"
    val ANSI_GREEN = "\u001B[32m"
    val ANSI_YELLOW = "\u001B[33m"
    val ANSI_BLUE = "\u001B[34m"
    val ANSI_PURPLE = "\u001B[35m"
    val ANSI_CYAN = "\u001B[36m"
    val ANSI_WHITE = "\u001B[37m"
    val ANSI_BLACK_BACKGROUND = "\u001B[40m"
    val ANSI_RED_BACKGROUND = "\u001B[41m"
    val ANSI_GREEN_BACKGROUND = "\u001B[42m"
    val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
    val ANSI_BLUE_BACKGROUND = "\u001B[44m"
    val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
    val ANSI_CYAN_BACKGROUND = "\u001B[46m"
    val ANSI_WHITE_BACKGROUND = "\u001B[47m"
}
