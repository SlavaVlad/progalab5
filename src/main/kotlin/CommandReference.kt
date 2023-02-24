package app

data class CommandReference(
    val commandName: String,
    val arguments: HashMap<String, String>? // argument, pattern
)