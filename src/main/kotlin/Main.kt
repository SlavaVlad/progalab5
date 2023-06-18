@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package app.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.common.client.Command
import app.common.client.network.CommandDaoImpl
import app.common.client.ui.graphix.graphix
import app.common.client.ui.home.home
import app.common.client.ui.login.loginForm
import kotlinx.coroutines.*
import com.apu.data.ExecutionResult
import persistence.utils.ConsoleColors
import persistence.utils.makeInput
import persistence.utils.printlnc
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import kotlin.system.exitProcess

// Обработка падения / недоступности секрвера

enum class Page(val destination: String) {
    LOGIN("login"),
    HOME("table"),
    GRAPHICS("graphics")
}

val textHeader = TextStyle(fontSize = TextUnit(24F, TextUnitType.Sp), color = Color.DarkGray)
var page: MutableState<Page> = mutableStateOf(Page.LOGIN)
lateinit var arguments: MutableList<String>
val dao = CommandDaoImpl()
val globalNetworkScope = CoroutineScope(Dispatchers.IO)

fun main(args: Array<String>) {
    arguments = args.toMutableList()
    if (args.isEmpty()) {
        println("Using: -ui parameter")
        arguments.add("-ui")
    }
    when (arguments[0]) {
        "-ui" -> {
            runUI()
        }

        "-console" -> {
            runBlocking {
                launchConsoleClient()
            }
        }
    }
}

@Composable
fun appBar() {
    val nav = LocalRootController.current
    TopAppBar {
        IconButton(
            onClick = {
                nav.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.padding(8.dp).size(24.dp)
            )
        }
        Text(
            text = "Lab 8 using Compose UI for desktop",
            modifier = Modifier.padding(8.dp),
            style = TextStyle(color = Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Page: ${page.value}",
            modifier = Modifier.padding(8.dp),
            style = TextStyle(color = Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "username: $globalUsername",
            modifier = Modifier.padding(8.dp),
            style = TextStyle(color = Color.White)
        )
    }
}

fun runUI() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Lab 8 using Compose UI for desktop",
            state = rememberWindowState(width = 600.dp, height = 800.dp)
        ) {
            setNavigationContent(configuration = OdysseyConfiguration()) {
                navigationGraph()
            }
        }
    }
}

var globalUsername = mutableStateOf("")

@ExperimentalFoundationApi
fun RootComposeBuilder.navigationGraph() {
    screen(Page.LOGIN.destination) {
        loginForm()
    }
    screen(Page.HOME.destination) {
        home()
    }
    screen(Page.GRAPHICS.destination) {
        graphix()
    }
}

suspend fun launchConsoleClient() {
    val dao = CommandDaoImpl()
    while (true) {
        readlnOrNull().let { input ->
            if (input != null) {
                if (input == "exit") {
                    dao.close()
                    exitProcess(0)
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
