package app

object CommandInfo {
    val commands = listOf(
        CommandReference("help", null),
        CommandReference("add_by_id", hashMapOf(
            "id" to ConsolePrimitiveType.int.pattern()
        ))
    )

    fun findReference(name: String): CommandReference? {
        return commands.find { it.commandName == name }
    }
}