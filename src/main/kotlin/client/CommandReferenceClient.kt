package app.common.client

import persistence.console.CPT

data class Argument(
    val name: String,
    val type: CPT,
    val description: String
)

data class CommandReferenceClient(
    val description: String? = "Command is not implemented yet",
    val arguments: List<Argument>? = null, // argument, pattern
    val preCompile: (List<String>) -> List<String> = { it }
)
