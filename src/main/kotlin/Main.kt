package app.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.common.client.Command
import app.common.client.network.CommandDaoImpl
import app.common.client.ui.login.loginForm
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.*
import com.apu.data.ExecutionResult
import persistence.utils.ConsoleColors
import persistence.utils.makeInput
import persistence.utils.printlnc
import kotlin.system.exitProcess

// Обработка падения / недоступности секрвера

lateinit var arguments: Array<String>

enum class Page(val destination: String) {
    LOGIN("login"),
    TABLE("table"),
    GRAPHICS("graphics")
}

fun main(args: Array<String>) {
    arguments = args
    val compose = true
    if (compose) {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Compose UI lab 8",
                state = rememberWindowState(width = 1000.dp, height = 1200.dp)
            ) {
                val page by remember { mutableStateOf(Page.LOGIN.destination) }
                Column (modifier = Modifier.fillMaxSize().padding(32.dp)) {
                    Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "page: $page")
                    Column (Modifier.align(Alignment.CenterHorizontally)) {
                        when (page) {
                            Page.LOGIN.destination -> {
                                loginForm()
                            }
                        }
                    }
                }
            }
        }
    }
    runBlocking { launchClient() }
}

suspend fun launchClient() {
    val dao = CommandDaoImpl()
    while (true) {
        readlnOrNull().let { input ->
            if (input != null) {
                if (input == "exit") {
                    dao.close()
                    exitProcess(0)
                } else if (input == "login") {
                    dao.login()
                }
                val formattedArgs = makeInput(input)
                val command: Command? = Command.make(formattedArgs) { ref ->
                    return@make ref.preCompile.invoke(formattedArgs.toList())
                }
                command?.let { compiled ->
                    dao.sendCommand(compiled) { result ->
                        result?.println()
                    }
                }
            }
        }
    }
}

fun error(errorText: String = "Unknown error") {
    printlnc(errorText, ConsoleColors.ANSI_RED)
}

fun ExecutionResult.println() {
    message?.let { println(it) }
    error?.let { error(it) }
}
