package integrate

import bl.entities.PFC
import bl.entities.RecipeState
import bl.managers.*
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.data.builder.RecipeDataBuilder
import da.data.mother.CommentDataMother
import da.data.mother.IngredientDataMother
import da.data.mother.StageDataMother
import da.data.mother.UserDataMother
import da.exeption.NotFoundException
import integrate.db.PGDataSourceProvider
import integrate.db.testFactory
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class CommentTest {
    private val repository = testFactory.createCommentRepository()

    @BeforeAll
    fun setUser() {
        CommentManager.registerRepository(repository)
        UserManager.registerRepository(testFactory.createUserRepository())

        AccountService.logIN("login1", "password1")
    }

    @Test
    @DisplayName("Create Comment")
    fun createComment() {
        val expected = CommentDataMother.newComment()

        val actual1 = CommentManager.create(expected.text, 1)

        val actual2 = repository.read(actual1)
        expected.date = actual2.date
        assertEquals(expected.id, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Read Comment")
    fun readComment() {
        val expected = CommentDataMother.readedComment()

        val actual = CommentManager.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentDataMother.updatedComment()

        CommentManager.update(expected)

        val actual = repository.read(expected.id)
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val cId = 1

        CommentManager.delete(cId)

        assertThrows<NotFoundException> { repository.read(cId) }
    }

    @Test
    @DisplayName("Update text Comment")
    fun updateTextComment() {
        val expected = CommentDataMother.updatedComment()

        val actual1 = CommentManager.updateText(expected.id, expected.text)

        val actual2 = repository.read(expected.id)
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Is owner")
    fun isOwner() {
        val expected = true
        val cId = 1
        val uId = 1

        val actual = CommentManager.isOwner(cId, uId)

        assertEquals(expected, actual)
    }

}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class IngredientTest {

    private val repository = testFactory.createIngredientRepository()

    @BeforeAll
    fun setUser() {
        IngredientManager.registerRepository(repository)
        UserManager.registerRepository(testFactory.createUserRepository())

        AccountService.logIN("login1", "password1")
    }

    @Test
    @DisplayName("Create Ingredient")
    fun createIngredient() {
        val expected = IngredientDataMother.newIngredient()

        val actual1 = IngredientManager.create(expected)

        val actual2 = repository.read(actual1)
        assertEquals(expected.id, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Read Ingredient")
    fun readIngredient() {
        val expected = IngredientDataMother.regularIngredient()

        val actual = IngredientManager.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Ingredient")
    fun updateIngredient() {
        val expected = IngredientDataMother.updatedIngredient()

        IngredientManager.update(expected)

        val actual = repository.read(expected.id)
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val cId = 1

        IngredientManager.delete(cId)

        assertThrows<NotFoundException> { repository.read(cId) }
    }

    @Test
    @DisplayName("Get all Ingredient")
    fun getAllIngredient() {
        val expected = IngredientDataMother.allIngredients()

        val actual = IngredientManager.getAll()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Find by name Ingredient")
    fun findByNameIngredient() {
        val expected = IngredientDataMother.allIngredients()
        val name = "name"

        val actual = IngredientManager.findByName(name)

        assertEquals(expected, actual)
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class RecipeTest {
    private val repository = testFactory.createRecipeRepository()

    @BeforeAll
    fun setUser() {
        RecipeManager.registerRepository(repository)
        UserManager.registerRepository(testFactory.createUserRepository())

        AccountService.logIN("login1", "password1")
    }

    @Test
    @DisplayName("Create Recipe")
    fun createRecipe() {
        val expected = RecipeDataBuilder()
            .withId(6)
            .withState(RecipeState.CLOSE)
            .withComments(listOf())
            .withStages(listOf(StageDataMother.newStage()))
            .build()

        val actual1 = RecipeManager.create(expected)

        val actual2 = repository.read(actual1)
        expected.date = actual2.date
        assertEquals(expected.id, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Read Recipe")
    fun readRecipe() {
        val expected = RecipeDataBuilder().withState(RecipeState.PUBLISHED).build()

        val actual = RecipeManager.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Recipe")
    fun updateRecipe() {
        val expected = RecipeDataBuilder().withName("new").withState(RecipeState.CLOSE)
            .withStages(listOf(StageDataMother.updatedStage())).build()

        RecipeManager.update(expected)

        val actual = repository.read(expected.id)
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val cId = 1

        RecipeManager.delete(cId)

        assertThrows<NotFoundException> { repository.read(cId) }
    }

    @Test
    @DisplayName("Get all Recipe")
    fun getAllRecipe() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())

        val actual = RecipeManager.getAll(null, null, "PUBLISHED")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update info Recipe")
    fun updateInfoRecipe() {
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

        val actual1 = RecipeManager.updateInfo(expected)

        val actual2 = repository.read(expected.id)
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Update stages Recipe")
    fun updateStagesRecipe() {
        val expected = RecipeDataBuilder().withStages(listOf(StageDataMother.updatedStage())).build()

        val actual1 = RecipeManager.updateStages(expected.id, expected.stages)

        val actual2 = repository.read(expected.id)
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class UserTest {
    private val repository = testFactory.createUserRepository()

    @BeforeAll
    fun setUser() {
        UserManager.registerRepository(repository)

        AccountService.logIN("login1", "password1")
    }

    @Test
    @DisplayName("Create User")
    fun createUser() {
        val expected = UserDataMother.created()

        val actual1 = UserManager.create(expected.login, expected.password)

        val actual2 = repository.read(actual1)
        assertEquals(expected.id, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Read User")
    fun readUser() {
        val expected = UserDataMother.admin()

        val actual = UserManager.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update User")
    fun updateUser() {
        val expected = UserDataMother.updated()

        UserManager.update(expected)

        val actual = repository.read(expected.id)
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete User")
    fun deleteUser() {
        val uId = 5

        UserManager.delete(uId)

        assertThrows<NotFoundException> { repository.read(uId) }
    }

    @Test
    @DisplayName("Get all User")
    fun getAllUser() {
        val expected = UserDataMother.all()

        val actual = UserManager.getAll()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update credentials User")
    fun updateCredentialsUser() {
        val expected = UserDataMother.updated()

        val actual1 = UserManager.updateCredentials(expected.id, expected.login, expected.password)

        val actual2 = repository.read(expected.id)
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Add to favorite User")
    fun addToFavoriteUser() {
        val uId = 5
        val rId = 5

        UserManager.addToFavorite(uId, rId)

        val actual = repository.isInFavorite(uId, rId)
        assertTrue(actual)
    }

    @Test
    @DisplayName("Delete from favorite User")
    fun deleteFromFavoriteUser() {
        val uId = 5
        val rId = 5

        UserManager.deleteFromFavorite(uId, rId)

        val actual = repository.isInFavorite(uId, rId)
        assertFalse(actual)
    }

    @Test
    @DisplayName("Get published recipes User")
    fun getPublishedRecipesUser() {
        val uId = 1
        val expected = listOf(RecipeDataBuilder().build().toPreview())

        val actual = UserManager.getPublishedRecipes(uId)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Is admin User")
    fun isAdminUser() {
        val uId = 1
        val expected = true

        val actual = UserManager.isAdmin(uId)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get by login User")
    fun getByLoginUser() {
        val expected = UserDataMother.admin()

        val actual = UserManager.getByLogin(expected.login)

        assertEquals(expected, actual)
    }
}


