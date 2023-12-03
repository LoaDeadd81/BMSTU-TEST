package integrate

import bl.managers.AccountService
import bl.managers.IngredientManager
import bl.managers.UserManager
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.data.mother.IngredientDataMother
import da.exeption.NotFoundException
import integrate.db.PGDataSourceProvider
import integrate.db.testFactory
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

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