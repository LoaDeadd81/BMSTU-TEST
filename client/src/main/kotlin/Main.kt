import dto.requests.StoreComment
import dto.requests.UserCredentials
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
class Token(val token: String)

suspend fun main(args: Array<String>) {
    val apiPrefix = "api/v1"
    val host = "http://localhost:8080/$apiPrefix"

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val login = "login6"
    val password = "password6"

    var response = client.post("$host/auth/register"){
        contentType(ContentType.Application.Json)
        setBody(UserCredentials(login, password))
    }
    println("Register")
    println(response.toString())

    response = client.post("$host/auth/login") {
        contentType(ContentType.Application.Json)
        setBody(UserCredentials(login, password))
    }

    val token = response.body<Token>().token
    val authHeaderKey = "Authorization"
    val authHeaderValue = "Bearer $token"

    println("Log in")
    println(response.toString())

    response = client.get("$host/recipes") {
        parameter("state", "PUBLISHED")
    }

    println("See feed")
    println(response.toString())

    response = client.get("$host/recipes/1")

    println("See recipe")
    println(response.toString())
    val uId = 8

    response = client.post("$host/users/$uId/saved-recipes/1"){
        headers{
            append(authHeaderKey, authHeaderValue)
        }
    }

    println("Like recipe")
    println(response.toString())

    response = client.post("$host/recipes/1/comments"){
        headers{
            append(authHeaderKey, authHeaderValue)
        }
        contentType(ContentType.Application.Json)
        setBody(StoreComment("lalala"))
    }

    println("Add comment")
    println(response.toString())
}