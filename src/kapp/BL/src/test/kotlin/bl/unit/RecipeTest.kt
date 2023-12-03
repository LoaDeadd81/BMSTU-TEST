package bl.unit

import bl.entities.PFC
import bl.entities.RecipeState
import bl.managers.AccountService
import bl.managers.RecipeManager
import bl.managers.UserManager
import bl.repositories.IRecipeRepository
import bl.repositories.IUserRepository
import da.data.builder.RecipeDataBuilder
import da.data.mother.StageDataMother
import org.junit.jupiter.api.*
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@TestMethodOrder(MethodOrderer.Random::class)
class RecipeTest {
    private lateinit var mockRep: IRecipeRepository
    private lateinit var mockUserRep: IUserRepository
    val adminId = 1

    @BeforeEach
    fun setMocks() {
        mockRep = mock<IRecipeRepository>()
        RecipeManager.registerRepository(mockRep)

        mockUserRep = mock<IUserRepository> {
            on { isAdmin(adminId) } doReturn true
        }
        UserManager.registerRepository(mockUserRep)

        AccountService.setId(adminId)
    }

    @Test
    @DisplayName("Create Recipe")
    fun createRecipe() {
        val expected = RecipeDataBuilder().withId(6).build()
        doReturn(expected.id).`when`(mockRep).create(expected)

        val actual = RecipeManager.create(expected)

        Assertions.assertEquals(expected.id, actual)
        verify(mockRep, times(1)).create(expected)
    }

    @Test
    @DisplayName("Read Recipe")
    fun readRecipe() {
        val expected = RecipeDataBuilder().withState(RecipeState.PUBLISHED).build()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = RecipeManager.read(expected.id)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).read(expected.id)
    }

    @Test
    @DisplayName("Update Recipe")
    fun updateRecipe() {
        val expected = RecipeDataBuilder()
            .withName("new")
            .withState(RecipeState.CLOSE)
            .withStages(listOf(StageDataMother.updatedStage()))
            .build()
        doNothing().`when`(mockRep).update(expected)

        RecipeManager.update(expected)

        verify(mockRep, times(1)).update(expected)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }


    @Test
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val cId = 1
        doNothing().`when`(mockRep).delete(cId)

        RecipeManager.delete(cId)

        verify(mockRep, times(1)).delete(cId)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Get all Recipe")
    fun getAllRecipe() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        doReturn(expected).`when`(mockRep).getPublished()

        val actual = RecipeManager.getAll(null, null, "PUBLISHED")

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).getPublished()
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
        doReturn(expected).`when`(mockRep).updateInfo(expected)

        val actual = RecipeManager.updateInfo(expected)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).updateInfo(expected)
        verify(mockUserRep, times(2)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Update stages Recipe")
    fun updateStagesRecipe() {
        val expected = RecipeDataBuilder().withStages(listOf(StageDataMother.updatedStage())).build()
        doReturn(expected).`when`(mockRep).updateStages(expected.id, expected.stages)

        val actual = RecipeManager.updateStages(expected.id, expected.stages)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).updateStages(expected.id, expected.stages)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }
}