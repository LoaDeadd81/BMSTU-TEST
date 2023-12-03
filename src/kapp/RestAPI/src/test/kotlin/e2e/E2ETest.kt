package e2e

import api.RestApi
import api.dto.requests.comment.StoreComment
import api.dto.requests.user.UserCredentials
import api.dto.responces.recipe.RecipePreviewResponse
import api.dto.responces.recipe.RecipeResponse
import api.plugins.configureLogging
import api.plugins.configureStatusCodes
import api.plugins.configureValidation
import bl.managers.*
import bl.repositories.ICommentRepository
import bl.repositories.IIngredientRepository
import bl.repositories.IRecipeRepository
import bl.repositories.IUserRepository
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import e2e.data.builder.RecipeDataBuilder
import e2e.data.mother.CommentDataMother
import e2e.data.mother.UserDataMother
import e2e.db.PGDataSourceProvider
import e2e.db.testFactory
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class E2ETest {
    private val apiPrefix = "api/v1"
    private val host = "http://${RestApi.host}:${RestApi.port}/$apiPrefix"

    private lateinit var commentRepository: ICommentRepository
    private lateinit var ingredientRepository: IIngredientRepository
    private lateinit var recipeRepository: IRecipeRepository
    private lateinit var userRepository: IUserRepository

    @BeforeAll
    fun initManagers() {
        commentRepository = testFactory.createCommentRepository()
        ingredientRepository = testFactory.createIngredientRepository()
        recipeRepository = testFactory.createRecipeRepository()
        userRepository = testFactory.createUserRepository()

        CommentManager.registerRepository(commentRepository)
        IngredientManager.registerRepository(ingredientRepository)
        RecipeManager.registerRepository(recipeRepository)
        UserManager.registerRepository(userRepository)
    }

    @Serializable
    class Token(val token: String)

    @Test
    @DisplayName("Register, login, see feed, see recipe, like recipe, add comment")
    fun e2eTest() = testApplication {
        application {
            configureSecurity()
            configureRouting()
            configureValidation()
            configureStatusCodes()
            configureLogging()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        //Register
        val expectedUser = UserDataMother.created()
        val expectedUrlRegister = "$host/users/${expectedUser.id}"
        val expectedCodeRegister = HttpStatusCode.Created

        var response = client.post("$apiPrefix/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(UserCredentials(expectedUser.login, expectedUser.password))
        }

        val actualUser = userRepository.read(expectedUser.id)
        assertEquals(expectedUser, actualUser)
        assertEquals(expectedUrlRegister, response.bodyAsText())
        assertEquals(expectedCodeRegister, response.status)

        //LogIn
        val expectedIdLogIn = expectedUser.id
        val expectedCodeLogIn = HttpStatusCode.OK

        response = client.post("$apiPrefix/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(UserCredentials(expectedUser.login, expectedUser.password))
        }

        val curId = AccountService.getCurrentUserId() ?: throw Exception("No id were set up")
        assertEquals(expectedIdLogIn, curId)
        assertEquals(expectedCodeLogIn, response.status)

        val token = response.body<Token>().token
        val authHeaderKey = "Authorization"
        val authHeaderValue = "Bearer $token"

        //See feed
        val expectedFeed = listOf(RecipeDataBuilder().build().toPreview()).map { RecipePreviewResponse(it) }
        val expectedCodeFeed = HttpStatusCode.OK

        response = client.get("$apiPrefix/recipes") {
            parameter("state", "PUBLISHED")
        }

        val actualFeed: List<RecipePreviewResponse> = response.body()
        assertEquals(expectedFeed, actualFeed)
        assertEquals(expectedCodeFeed, response.status)

        //See recipe
        val expectedRecipe = RecipeResponse(RecipeDataBuilder().build())
        val expectedCodeRecipe = HttpStatusCode.OK

        response = client.get("$apiPrefix/recipes/${expectedRecipe.id}")

        val actualRecipe: RecipeResponse = response.body()
        assertEquals(expectedRecipe, actualRecipe)
        assertEquals(expectedCodeRecipe, response.status)

        //Like recipe
        val uIdLike = actualUser.id
        val rIdLike = actualRecipe.id
        val expectedCodeLike = HttpStatusCode.NoContent

        response = client.post("$apiPrefix/users/$uIdLike/saved-recipes/$rIdLike"){
            headers{
                append(authHeaderKey, authHeaderValue)
            }
        }

        assertTrue(userRepository.isInFavorite(uIdLike, rIdLike))
        assertEquals(expectedCodeLike, response.status)

        //Add comment
        val rIdComment = actualRecipe.id
        val expectedComment = CommentDataMother.newCommente2e()
        val expectedUrlComment = "$host/comments/${expectedComment.id}"
        val expectedCodeComment = HttpStatusCode.Created

        response = client.post("$apiPrefix/recipes/$rIdComment/comments"){
            headers{
                append(authHeaderKey, authHeaderValue)
            }
            contentType(ContentType.Application.Json)
            setBody(StoreComment(expectedComment.text))
        }

        val actualComment = commentRepository.read(expectedComment.id)
        expectedComment.date = actualComment.date
        assertEquals(expectedComment, actualComment)
        assertEquals(expectedUrlComment, response.bodyAsText())
        assertEquals(expectedCodeComment, response.status)
    }
}