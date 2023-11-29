package da.unit

import bl.entities.PFC
import bl.entities.RecipeState
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import com.radcortez.flyway.test.junit.DataSourceInfo
import com.radcortez.flyway.test.junit.DataSourceProvider
import da.dao.*
import da.unit.data.builder.RecipeDataBuilder
import da.exeption.NotFoundException
import da.repositories.factory.PgRepositoryFactory
import da.unit.data.mother.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val factory = PgRepositoryFactory(System.getenv("TEST_SCHEMA") ?: throw Exception("no TEST_SCHEMA in env"))

class PGDataSourceProvider : DataSourceProvider {
    override fun getDatasourceInfo(extensionContext: ExtensionContext?): DataSourceInfo {
        val DB_CONNECT = System.getenv("DB_CONNECT") ?: throw Exception("no DB_CONNECT in env")
        val DB_HOST = System.getenv("DB_HOST") ?: throw Exception("no DB_HOST in env")
        val DB_PORT = System.getenv("DB_PORT") ?: throw Exception("no DB_PORT in env")
        val DB_NAME = System.getenv("DB_NAME") ?: throw Exception("no DB_NAME in env")
        val DB_SHEMA = System.getenv("TEST_SCHEMA") ?: throw Exception("no TEST_SCHEMA in env")
        val DB_USER = System.getenv("DB_USER") ?: throw Exception("no DB_USER in env")
        val DB_PASSWORD = System.getenv("DB_PASSWORD") ?: throw Exception("no DB_PASSWORD in env")

        val url = "jdbc:$DB_CONNECT://$DB_HOST:$DB_PORT/$DB_NAME?currentSchema=$DB_SHEMA"
        val user = DB_USER
        val password = DB_PASSWORD
        return DataSourceInfo.config(url, user, password)
    }

}


@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class CommentTest {

    private val repository = factory.createCommentRepository()

    @Test
    @DisplayName("Create comment")
    fun createComment() {
        val expected = CommentDataMother.newComment()
        val rId = 1

        val cId = repository.create(expected.autor.id, expected.text, rId)

        val actual1 = transaction {
            CommentTable.findById(cId)?.toEntity()
        }
        val actual2 = transaction {
            RecipeTable.findById(rId)?.toEntity()?.comments?.last()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        expected.date = actual2.date
        assertAll("Add comment asserts", { assertEquals(expected, actual1) }, { assertEquals(expected, actual2) })
    }

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentDataMother.updatedComment()

        repository.update(expected)

        val actual = transaction {
            CommentTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Comment with id = $id not found")
        }
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Read Comment")
    fun readComment() {
        val expected = CommentDataMother.readedComment()

        val actual = repository.read(expected.id)

        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { CommentTable.findById(id) })
    }

    @Test
    @DisplayName("Update text")
    fun updateText() {
        val expected = CommentDataMother.updatedTextComment()

        val actual = repository.updateText(expected.id, expected.text)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get owner id")
    fun getOwnerID() {
        val expected = 1
        val id = 1

        val actual = repository.getOwnerID(id)

        assertEquals(expected, actual)
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class IngredientTest {

    private val repository = factory.createIngredientRepository()

    @Test
    @DisplayName("Create Ingredient")
    fun createIngredient() {
        val expected = IngredientDataMother.newIngredient()

        val iId = repository.create(expected)

        val actual = transaction {
            IngredientTable.findById(iId)?.toEntity()
        }
        expected.id = iId
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Ingredient")
    fun updateIngredient() {
        val expected = IngredientDataMother.updatedIngredient()

        repository.update(expected)

        val actual = transaction {
            IngredientTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Ingredient with id = $id not found")
        }
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { IngredientTable.findById(id) })
    }

    @Test
    @DisplayName("Get all Ingredients")
    fun getAllIngredients() {
        val expected = IngredientDataMother.allIngredients()

        val actual = repository.getAll()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Is name not exist, exist")
    fun isNameNotExistExist() {
        val name = IngredientDataMother.regularIngredient().name

        val actual = repository.isNameNotExist(name)

        assertFalse(actual)

    }

    @Test
    @DisplayName("Is name not exist, not exist")
    fun isNameNotExistNotExist() {
        val name = "no name"

        val actual = repository.isNameNotExist(name)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Find by name Ingredient")
    fun findByNameIngredient() {
        val expected = IngredientDataMother.allIngredients()

        val actual = repository.findByName("name")

        assertEquals(expected, actual)
    }
}


@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class RecipeTest {

    private val repository = factory.createRecipeRepository()

    @Test
    @DisplayName("Create Recipe")
    fun createRecipe() {
        val expected = RecipeDataBuilder().withMeta(
            6,
            "name6",
            "desc6",
            5,
            5,
            PFC(5, 5, 5),
            LocalDateTime.parse("2023-05-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            RecipeState.CLOSE
        )
            .withOwner(UserDataMother.lastUser())
            .withStages(listOf(StageDataMother.favoriteStage()))
            .withComments(listOf())
            .withIngredients(listOf(IngredientInStageDataMother.favoriteIngredient()))
            .build()

        val rId = repository.create(expected)

        val actual = transaction {
            RecipeTable.findById(rId)?.toEntity() ?: throw NotFoundException("Recipe with id = $id not found")
        }
        expected.id = rId
        expected.date = actual.date
        for (i in actual.stages.indices) {
            expected.stages[i].id = actual.stages[i].id
        }
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get Recipe")
    fun getRecipe() {
        val expected = RecipeDataBuilder().build()

        repository.read(expected.id)

        val actual = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Update Recipe")
    fun updateRecipe() {
        val expected = RecipeDataBuilder().withName("new").withState(RecipeState.PUBLISHED).build()

        repository.update(expected)

        val actual = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        for (i in actual.stages.indices) {
            expected.stages[i].id = actual.stages[i].id
        }
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { RecipeTable.findById(id) })
    }


    @Test
    @DisplayName("Get published")
    fun getPublished() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())

        val actual = repository.getPublished()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update info")
    fun updateInfo() {
        val expected = RecipeDataBuilder().withMeta(
            1,
            "new",
            "new",
            10,
            10,
            PFC(10, 10, 10),
            LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            RecipeState.PUBLISHED
        ).build()

        val actual1 = repository.updateInfo(expected)

        val actual2 = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }


    @Test
    @DisplayName("Update stages")
    fun updateStages() {
        val expected = RecipeDataBuilder().withStages(listOf(StageDataMother.updatedStage())).build()

        val actual1 = repository.updateStages(expected.id, expected.stages)

        val actual2 = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Get owner id")
    fun getOwnerID() {
        val expected = 1
        val rId = 1

        val actual = repository.getOwnerID(rId)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get publish queue")
    fun getPublishQueue() {
        val expected = listOf(
            RecipeDataBuilder().withMeta(
                2,
                "name2",
                "desc2",
                2,
                2,
                PFC(2, 2, 2),
                LocalDateTime.parse("2023-02-02 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                RecipeState.READY_TO_PUBLISH
            )
                .withOwner(UserDataMother.regular())
                .withStages(listOf(StageDataMother.readyToPublishStage()))
                .withComments(listOf(CommentDataMother.readyToPublishComment()))
                .withIngredients(listOf(IngredientInStageDataMother.readyToPublishIngredient()))
                .build().toPreview()
        )

        val actual = repository.getPublishQueue()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get saved recipe")
    fun getSavedRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getSavedRecipes(uId)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get own recipe")
    fun getOwnRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getOwnRecipes(uId)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get published recipe")
    fun getPublishedRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getPublishedRecipes(uId)

        assertEquals(expected, actual)
    }

}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class UserTest {

    private val repository = factory.createUserRepository()

    @Test
    @DisplayName("Create user")
    fun createUser() {
        val expected = UserDataMother.created()

        val uid = repository.create(expected.login, expected.password)

        val actual = transaction {
            UserTable.findById(uid)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get User")
    fun getUser() {
        val expected = UserDataMother.admin()

        val actual = repository.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update User")
    fun updateUser() {
        val expected = UserDataMother.updated()

        repository.update(expected)

        val actual = transaction {
            UserTable.findById(expected.id)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete User")
    fun deleteUser() {
        val id = 5

        repository.delete(id)

        val ent = EntityID(id, Users)
        assertTrue(transaction { CommentTable.find { Comments.autor eq ent }.empty() })
        assertTrue(transaction { RecipeTable.find { Recipes.owner eq ent }.empty() })
        assertTrue(transaction { SavedRecipeTable.find { SavedRecipes.user eq ent }.empty() })
    }

    @Test
    @DisplayName("Get all Users")
    fun getAllUsers() {
        val expected = UserDataMother.all()

        val actual = repository.getAll()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update credentials")
    fun updateCredentials() {
        val expected = UserDataMother.updated()

        val actual1 = repository.updateCredentials(expected.id, expected.login, expected.password)

        val actual2 = transaction {
            UserTable.findById(expected.id)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Add to favorite")
    fun addToFavorite() {
        val expected = RecipePreviewDataMother.favorite()
        val uId = 5
        val rId = 5

        repository.addToFavorite(uId, rId)

        val ent = EntityID(uId, Users)
        val actual2 = transaction {
            SavedRecipeTable.find { SavedRecipes.user eq ent }.map { it.recipe.toPreview() }
        }.first()
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Del from favorite")
    fun delFromFavorite() {
        val uId = 4
        val rId = 4

        repository.deleteFromFavorite(uId, rId)

        val uEnt = EntityID(uId, Users)
        val rEnt = EntityID(rId, Recipes)
        assertTrue(
            transaction {
                SavedRecipeTable.find { SavedRecipes.user eq uEnt and (SavedRecipes.recipe eq rEnt) }.empty()
            }
        )
    }

    @Test
    @DisplayName("Is in favorite")
    fun isInFavorite() {
        val uId = 1
        val rId = 1

        val actual = repository.isInFavorite(uId, rId)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Is login exist")
    fun isLoginNotExist() {
        val login = "lalala"

        val actual = repository.isLoginNotExist(login)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Is admin")
    fun isAdmin() {
        val uId = 1

        val actual = repository.isAdmin(uId)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Get by login")
    fun getByLogin() {
        val expected = UserDataMother.admin()
        val login = expected.login

        val actual = repository.getByLogin(login)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get published recipes")
    fun getPublishedRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getPublishedRecipes(uId)

        assertEquals(expected, actual)
    }

}
