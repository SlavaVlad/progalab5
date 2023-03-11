package app.checkerComponent

import java.util.regex.Pattern

data class CommandReference(
    val commandName: String,
    val arguments: HashMap<String, Pattern>? // argument, pattern
)