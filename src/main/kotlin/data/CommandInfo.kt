package app.data

import app.checkerComponent.CommandReference
import app.console.ConsolePrimitiveType

object CommandInfo {
    val commands = listOf(
        CommandReference("help", null),
        CommandReference("add_by_id", hashMapOf(
            "id" to ConsolePrimitiveType.int
        ))
    )

    fun findReference(name: String): CommandReference? {
        return commands.find { it.commandName == name }
    }
}