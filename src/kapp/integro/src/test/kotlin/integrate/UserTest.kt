package integrate

import bl.managers.AccountService
import bl.managers.UserManager
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.exeption.NotFoundException
import integrate.data.builder.RecipeDataBuilder
import integrate.data.mother.UserDataMother
import integrate.db.PGDataSourceProvider
import integrate.db.testFactory
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        Assertions.assertFalse(actual)
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