package app.common.client.network

import app.common.client.Command
import com.apu.data.ExecutionResult
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import persistence.database.product.Product

class CommandDaoImpl : CommandDAO {
    var token = ""

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
            header("Content-Type", "application/json")
            header("Authorization", "Bearer $token")
        }
        install(HttpCookies)
    }

    data class Auth(
        val name: String,
        val pass: String
    )
    data class Token(
        val token: String
    )
    suspend fun login(username: String, password: String): Boolean {
        val call = client.post("/login"){
            setBody(Auth(
                username,
                password
            ))
        }
        if (call.status.isSuccess()) {
            token = call.body<Token>().token
            println("token: $token")
            println("logged in")
            return true
        } else {
            println("failed")
            return false
        }
    }

    override suspend fun sendCommand(
        command: Command,
        onResult: (ExecutionResult?) -> Unit
    ) {
        val response = client.post("/command") {
            if (token.isNotBlank()) {
                headers.append("Authorization", "Bearer $token")
            }
            setBody(command)
        }
        onResult(Gson().fromJson(response.bodyAsText(), ExecutionResult::class.java))
    }

    suspend fun getProducts(): List<Product> {
        return client.get("/products/all").body<List<Product>>()
    }
    suspend fun editProduct(p: Product, id: Long) {
        client.post("/products/edit/$id") {
            setBody(p)
        }
    }
    suspend fun deleteProduct(id: Long) {
        client.delete("/products/delete/$id")
    }

    fun close() {
        client.close()
    }
}