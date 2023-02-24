package app

class Command(
    var command: String? = null,
    var arguments: Array<String>? = null,
) {
    companion object {
        fun make(input: Array<String>): Command? {
            val command: String
            try {
                command = input[0]
                if (CommandInfo.findReference(command) == null) {
                    if (command.isBlank()) return null
                    error("command not exists")
                    return null
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                // ничего не ввели и нажали Enter
                return null
            }

            val args = input.takeAfter(0)
            val ref = CommandInfo.findReference(command)!!
            val argsCount = ref.arguments?.size
            if (argsCount != args.size) {
                if (args.size > (ref.arguments?.size ?: 0)) {
                    error(ConsoleErrors(command).too_many_arguments)
                    return null
                } else if (args.size < (ref.arguments?.size ?: 0)) {
                    error(ConsoleErrors(command).not_enough_arguments)
                    return null
                }
            }
            args.forEachIndexed { index, arg ->
                if (ref.arguments?.keys?.toTypedArray()?.get(index) != null) {
                    ref.arguments.keys.toTypedArray()[index].let { Regex(it).matchEntire(arg) }
                } else {
                    error("argument $index is incorrect")
                    return null
                }
            }

            return Command(command, args)
        }
    }
}
