package persistence.checkerComponent

import persistence.console.CPT
import persistence.data.ExecutionResult
import java.util.FormattableFlags
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.Flags

fun printToConsole(result: ExecutionResult) {
    result.error?.let { println(it) }
    result.message?.let { println(it) }
}

fun sendToClient(result: ExecutionResult) {
    result.error?.let { println(it) }
    result.message?.let { println(it) }
}

data class Argument(
    val name: String,
    val type: CPT,
    val description: String
)

data class CommandReference(
    val description: String? = "Command is not implemented yet",
    val arguments: List<Argument>? = null, // argument, pattern
    val preProcess: () -> Any = {},
    val function: (
        additionalPayload: Any?,
        List<String>,
        (ExecutionResult) -> Unit
    ) -> Unit
)
