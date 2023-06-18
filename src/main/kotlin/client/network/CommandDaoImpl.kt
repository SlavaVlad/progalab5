package app.common.client.network

import app.common.client.Command
import com.apu.data.ExecutionResult
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.date.*
import kotlinx.coroutines.*
import java.time.Instant

class CommandDaoImpl : CommandDAO {

    var user = "test"
    var pass = "test"
    var authorised = false

    private val client = HttpClient(CIO) {
        followRedirects = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            url("http:/127.0.0.1/")
            header("Content-Type", ContentType.Application.Json)
        }
        install(HttpCookies)
    }

//    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            keepLogin()
//        }
//    }

    override suspend fun keepLogin() {
        authorised = true
        return
        client.cookies("http://0.0.0.0:8080/").map {
            authorised =
                it.name == "user_session" && it.expires?.toJvmDate()?.time!! < Instant.now().epochSecond * 1000L
        }
        if (!authorised) {
            val response = client.submitForm("/login") {
                formData {
                    this.append("username", user)
                    this.append("password", pass)
                }
            }
            if (response.status.isSuccess()) {
                authorised = true
            }
        }
    }

    // recursion login warning
    override suspend fun sendCommand(
        command: Command,
        onResult: (ExecutionResult?) -> Unit
    ) {
        runBlocking {
            keepLogin()
        }
        if (authorised) {
            val response = client.post("/command") {
                setBody(command)
            }
            onResult(Gson().fromJson(response.bodyAsText(), ExecutionResult::class.java))
        } else {
            onResult(null)
        }
    }

    fun close() {
        client.close()
    }
}