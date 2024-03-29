package app.common.client

import persistence.database.ProductRepository
import persistence.utils.addIncrement
import persistence.utils.makeInput

/** Нам нужно детектить (info, warn, error)
 * * i Очень длинные скрипты
 * * w запуск другого скрипта из этого скрипта
 * * e несуществующая команда или аргумент. Протестить, компилируется команда или нет. Выводить отчет в консоль
 * * e рекурсивный вызов скрипта
 * */

class Log(val tag: String) {
    var logString = ""

    enum class LogLevel {
        INFO, WARN, ERROR
    }

    fun log(level: LogLevel, message: String) {
        val string = "$level/$tag: $message"
        logString += "string\n"
        println(string)
    }

    fun e(message: String) {
        log(LogLevel.ERROR, message)
    }

    fun w(message: String) {
        log(LogLevel.WARN, message)
    }

    fun i(message: String) {
        log(LogLevel.INFO, message)
    }
}

class ScriptExecutorClient(private val lines: List<String>) {
    private val commands = mutableListOf<Command>()
    private val log = Log("ScriptExecutor")
    private val scriptsUsed = mutableMapOf<String, Int>()

    private val unsupportedCommandsList = listOf(
        "add",
        "update",
        "add_if_max",
        "remove_greater"
    )

    // e существование команды и проверка аргументов
    private fun checkCommands() {
        lines.forEachIndexed { index, line ->
            val input = makeInput(line)
            unsupportedCommandsList.forEach {
                if (input[0] == it) throw CommandCompilationException("Command $it is not supported in script mode")
            }
            val command = Command.make(input, onError = { errorString ->
                commands.clear()
                throw CommandCompilationException(errorString)
            }) { ref ->
                return@make ref.preCompile.invoke(input.toList())
            }
            commands.add(command!!)
        }
        log.i("All commands compiled successfully")
    }

    // e recursion - запуск скрипта из скрипта, проверка запуском в "виртуальной среде" не предсказательный тест, а прямой
    private fun checkRecursion() {
        commands.forEachIndexed { i, command ->
            if (command.name == "execute_script") scriptsUsed.addIncrement(command.args!![0])
        }
        checkScriptFromScript()
    }

    // w длина скрипта
    private fun checkScriptLength() {
        if (commands.count {
                it.name == "execute_script"
            } > 100) {
            log.w("script is longer than 100 lines (total ${commands.count()} commands))")
        }
    }

    private fun checkScriptFromScript() {
        log.i("This script has ${scriptsUsed.count()} scripts used")
        log.w("This script has ${scriptsUsed.count { it.value > 1 }} scripts used more than once")
        // log w list of used script from usedScripts in format "script1 - 9 times" and so on (use map.forEach)
        log.w("Used scripts: ${scriptsUsed.map { "${it.key} - ${it.value} times" }}")
    }

    fun check(onSuccess: (String, List<Command>) -> Unit, onError: (String) -> Unit) {
        try {
            checkCommands()
            checkRecursion()
            checkScriptLength()
            onSuccess(
                "Log:\n" +
                        log.logString, commands
            )
        } catch (e: Exception) {
            log.e("Script check failed (${e.message}), execution forbidden")
            onError(e.message ?: "Error while compiling command \nLog:\n${log.logString}")
        }
    }
}