package app.common.client

import app.common.error
import kotlinx.serialization.Serializable
import persistence.console.CPT
import persistence.console.ConsoleError
import persistence.database.ProductRepository
import persistence.utils.takeAfter

class CommandCompilationException(message: String) : Exception(message)

@Serializable
class Command(
    var name: String? = null,
    var args: Array<String>? = null,
) {
    companion object {
        fun make(input: Array<String>, onError: (String) -> Unit = { error(it) }, callPrecompile: (CommandReferenceClient) -> List<String>): Command? {
            val command: String
            try {
                command = input[0]
                if (CommandInfoClient(ProductRepository()).findReferenceOrNull(command) == null) {
                    if (command.isBlank()) return null
                    onError("command not exists")
                    return null
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                // ничего не ввели и нажали Enter
                return null
            }
            var args = input.takeAfter(0)
            val ref = CommandInfoClient(ProductRepository()).findReferenceOrNull(command)!!
            val refArgsCount = ref.arguments?.size?: 0

            if (ref.arguments?.any { it.type == CPT.JSON }?.and((args.size != ref.arguments.size)) == true) {
                args = callPrecompile(ref).toTypedArray().takeAfter(0)
            }

            if (refArgsCount != args.size) {
                if (args.size > (refArgsCount)) {
                    onError(ConsoleError.too_many_arguments.format(command))
                    return null
                } else if (args.size < (refArgsCount)) {
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
