package app.common.client.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.common.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController

@Composable
fun loginForm() {
    Column {
        // login form with username and password text fields
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf(false) }
        val nav = LocalRootController.current

        Text("Login", style = textHeader)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = username, onValueChange = { username = it }, enabled = !loading, isError = error)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = password, onValueChange = { password = it }, enabled = !loading, isError = error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                globalNetworkScope.launch {
                    loading = true
                    val success = dao.login(username, password)
                    if (success) {
                        loading = false
                        error = false
                        delay(400)
                        globalUsername.value = username
                        nav.push(Page.HOME.destination)
                    } else {
                        loading = false
                        error = true
                    }
                }
            },
            enabled = !loading
        ) {
            if (loading) {
                Text("Loading...")
            } else {
                Text("Login")
            }
        }
    }
}