package app.server

import persistence.utils.ConsoleColors
import java.sql.Timestamp

data class Message(
    val tag: String = "Undefined",
    val message: String,
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
) {
    override fun toString(): String {
        return "${ConsoleColors.ANSI_BLUE}$timestamp $tag${ConsoleColors.ANSI_CYAN}:${ConsoleColors.ANSI_RESET} $message"
    }
}
