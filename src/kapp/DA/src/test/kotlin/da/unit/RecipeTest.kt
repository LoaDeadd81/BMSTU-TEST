package da.unit

import bl.entities.PFC
import bl.entities.RecipeState
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.dao.RecipeTable
import da.dao.toEntity
import da.exeption.NotFoundException
import da.unit.data.builder.RecipeDataBuilder
import da.unit.data.mother.CommentDataMother
import da.unit.data.mother.IngredientInStageDataMother
import da.unit.data.mother.StageDataMother
import da.unit.data.mother.UserDataMother
import da.unit.db.PGDataSourceProvider
import da.unit.db.factory
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        Assertions.assertEquals(expected, actual)
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
        Assertions.assertEquals(expected, actual)
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
        Assertions.assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val id = 5

        repository.delete(id)

        Assertions.assertNull(transaction { RecipeTable.findById(id) })
    }


    @Test
    @DisplayName("Get published")
    fun getPublished() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())

        val actual = repository.getPublished()

        Assertions.assertEquals(expected, actual)
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
        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
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
        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Get owner id")
    fun getOwnerID() {
        val expected = 1
        val rId = 1

        val actual = repository.getOwnerID(rId)

        Assertions.assertEquals(expected, actual)
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

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get saved recipe")
    fun getSavedRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getSavedRecipes(uId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get own recipe")
    fun getOwnRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getOwnRecipes(uId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get published recipe")
    fun getPublishedRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getPublishedRecipes(uId)

        Assertions.assertEquals(expected, actual)
    }

}