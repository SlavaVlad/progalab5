package app.checkerComponent

import app.data.CommandInfo
import app.console.ConsoleError
import app.database.ProductRepository
import utils.takeAfter

class CommandCompilationException(message: String) : Exception(message)

class Command(
    var name: String? = null,
    var args: Array<String>? = null,
) {
    companion object {
        fun make(input: Array<String>, onError: (String) -> Unit = { app.error(it) }): Command? {
            val command: String
            try {
                command = input[0]
                if (CommandInfo(ProductRepository()).findReferenceOrNull(command) == null) {
                    if (command.isBlank()) return null
                    onError("command not exists")
                    return null
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                // ничего не ввели и нажали Enter
                return null
            }

            val args = input.takeAfter(0)
            val ref = CommandInfo(ProductRepository()).findReferenceOrNull(command)!!
            val argsCount = ref.arguments?.size
            if (argsCount != args.size) {
                if (args.size > (ref.arguments?.size ?: 0)) {
                    onError(ConsoleError.too_many_arguments.format(command))
                    return null
                } else if (args.size < (ref.arguments?.size ?: 0)) {
                    onError(ConsoleError.not_enough_arguments.format(command))
                    return null
                }
            }
            args.forEachIndexed { index, arg ->
                if (ref.arguments?.toTypedArray()?.get(index) != null) {
                    if (!ref.arguments.toTypedArray()[index].type.pattern.matcher(arg).matches()) {
                        onError("argument $index has incorrect type")
                        return null
                    }
                } else {
                    onError(ConsoleError.too_many_arguments.format(command))
                    return null
                }
            }

            return Command(command, args)
        }
    }
}
