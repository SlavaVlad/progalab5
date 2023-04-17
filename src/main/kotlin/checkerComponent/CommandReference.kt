package app.checkerComponent

import app.data.ExecutionResult
import java.util.regex.Pattern

fun printToConsole(result: ExecutionResult) {
    result.error?.let { println(it) }
    result.message?.let { println(it) }
}

data class CommandReference(
    val description: String? = "Command is not implemented yet",
    val arguments: List<Pattern>? = null, // argument, pattern
    val argumentsDescription: List<String>? = null, // null fo no args commands
    val function: (
        List<String>,
        (ExecutionResult) -> Unit
    ) -> Unit
)
