package app.data

class ConsoleErrors(command: String, type: String = "error") {
    val too_many_arguments = "$type: too many arguments for $command"
    val not_enough_arguments = "$type: not enough arguments for $command"
}