package utils

import java.io.File
import java.io.InputStream
import java.lang.Exception

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
    val ANSI_LIGHT_GREEN = "\u001B[92m"
    val ANSI_BLACK_BACKGROUND = "\u001B[40m"
    val ANSI_RED_BACKGROUND = "\u001B[41m"
    val ANSI_GREEN_BACKGROUND = "\u001B[42m"
    val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
    val ANSI_BLUE_BACKGROUND = "\u001B[44m"
    val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
    val ANSI_CYAN_BACKGROUND = "\u001B[46m"
    val ANSI_WHITE_BACKGROUND = "\u001B[47m"
}

fun readlinesFile(path: String): List<String> {
    val file = File(path)
    if (!file.isFile) {
        throw IllegalArgumentException("Specified path does not point to a file: $path")
    }

    val lines = mutableListOf<String>()
    file.forEachLine {
        lines.add(it)
    }

    return lines
}

class InputInterruptException(message: String) : Exception(message)

/**
 * По умолчанию валидатор проверяет строку на пустоту
 * */
fun requestUserInput(
    prompt: String, validator: (String) -> Boolean = {
        it.isNotBlank()
    }
): String {
    var input: String
    do {
        print(prompt)
        input = readLine()!!
        if (input == "exit") {
            throw InputInterruptException("User interrupted input")
        }
    } while (!validator(input))
    return input
}

fun makeInput(input: String): Array<String> {
    //val regex = """(?<=\s|^)"([^"]*)"(?=\s|$)|(\S+)""".toRegex()
    val regex = """(?<!\\)".*?(?<!\\)"|\S+""".toRegex()
    return regex.findAll(input)
        .map { it.groupValues[0].removeSurrounding("\"") }
        .toList()
        .toTypedArray()
}

fun <T> MutableMap<T, Int>.addIncrement(key: T) {
    if (this.containsKey(key)) {
        this[key] = this[key]!! + 1
    } else {
        this[key] = 1
    }
}

fun <T> MutableMap<T, Int>.addDecrement(key: T) {
    if (this.containsKey(key)) {
        this[key] = this[key]!! - 1
    } else {
        this[key] = 0
    }
}

fun <V, T> HashMap<V, T>.plusAssign(pair: Pair<V, T>) {
    this[pair.first] = pair.second
}