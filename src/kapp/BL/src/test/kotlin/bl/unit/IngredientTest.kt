package bl.unit

import bl.managers.AccountService
import bl.managers.IngredientManager
import bl.managers.UserManager
import bl.repositories.IIngredientRepository
import bl.repositories.IUserRepository
import da.data.mother.IngredientDataMother
import org.junit.jupiter.api.*
import org.mockito.kotlin.*

@TestMethodOrder(MethodOrderer.Random::class)
class IngredientTest {
    private lateinit var mockRep: IIngredientRepository
    private lateinit var mockUserRep: IUserRepository
    val adminId = 1

    @BeforeEach
    fun setMocks() {
        mockRep = mock<IIngredientRepository>()
        IngredientManager.registerRepository(mockRep)

        mockUserRep = mock<IUserRepository> {
            on { isAdmin(adminId) } doReturn true
        }
        UserManager.registerRepository(mockUserRep)

        AccountService.setId(adminId)
    }

    @Test
    @DisplayName("Create Ingredient")
    fun createIngredient() {
        val expected = IngredientDataMother.newIngredient()
        doReturn(expected.id).`when`(mockRep).create(expected)
        doReturn(true).`when`(mockRep).isNameNotExist(expected.name)

        val actual = IngredientManager.create(expected)

        Assertions.assertEquals(expected.id, actual)
        verify(mockRep, times(1)).create(expected)
        verify(mockRep, times(1)).isNameNotExist(expected.name)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Read Ingredient")
    fun readIngredient() {
        val expected = IngredientDataMother.regularIngredient()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = IngredientManager.read(expected.id)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).read(expected.id)
    }

    @Test
    @DisplayName("Update Ingredient")
    fun updateIngredient() {
        val expected = IngredientDataMother.updatedIngredient()
        doNothing().`when`(mockRep).update(expected)

        IngredientManager.update(expected)

        verify(mockRep, times(1)).update(expected)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }


    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val cId = 1
        doNothing().`when`(mockRep).delete(cId)

        IngredientManager.delete(cId)

        verify(mockRep, times(1)).delete(cId)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Get all Ingredient")
    fun getAllIngredient() {
        val expected = IngredientDataMother.allIngredients()
        doReturn(expected).`when`(mockRep).getAll()

        val actual = IngredientManager.getAll()

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).getAll()
    }

    @Test
    @DisplayName("Find by name Ingredient")
    fun findByNameIngredient() {
        val expected = IngredientDataMother.allIngredients()
        val name = "name"
        doReturn(expected).`when`(mockRep).findByName(name)

        val actual = IngredientManager.findByName(name)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).findByName(name)
    }
}