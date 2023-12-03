package integrate

import bl.entities.PFC
import bl.entities.RecipeState
import bl.managers.AccountService
import bl.managers.RecipeManager
import bl.managers.UserManager
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.data.builder.RecipeDataBuilder
import da.data.mother.StageDataMother
import da.exeption.NotFoundException
import integrate.db.PGDataSourceProvider
import integrate.db.testFactory
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

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