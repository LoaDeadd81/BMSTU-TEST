package da.unit

import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.dao.IngredientTable
import da.dao.toEntity
import da.exeption.NotFoundException
import da.unit.data.mother.IngredientDataMother
import da.unit.db.PGDataSourceProvider
import da.unit.db.factory
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

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
        Assertions.assertEquals(expected, actual)
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
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val id = 5

        repository.delete(id)

        Assertions.assertNull(transaction { IngredientTable.findById(id) })
    }

    @Test
    @DisplayName("Get all Ingredients")
    fun getAllIngredients() {
        val expected = IngredientDataMother.allIngredients()

        val actual = repository.getAll()

        Assertions.assertEquals(expected, actual)
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

        Assertions.assertEquals(expected, actual)
    }
}