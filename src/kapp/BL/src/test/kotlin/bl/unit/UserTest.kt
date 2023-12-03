package bl.unit

import bl.managers.AccountService
import bl.managers.UserManager
import bl.repositories.IUserRepository
import da.data.builder.RecipeDataBuilder
import da.data.mother.UserDataMother
import org.junit.jupiter.api.*
import org.mockito.kotlin.*

@TestMethodOrder(MethodOrderer.Random::class)
class UserTest {
    private lateinit var mockRep: IUserRepository
    val adminId = 1

    @BeforeEach
    fun setMocks() {
        mockRep = mock<IUserRepository>() {
            on { isAdmin(adminId) } doReturn true
        }
        UserManager.registerRepository(mockRep)

        AccountService.setId(adminId)
    }

    @Test
    @DisplayName("Create User")
    fun createUser() {
        val expected = UserDataMother.created()
        doReturn(expected.id).`when`(mockRep).create(expected.login, expected.password)
        doReturn(true).`when`(mockRep).isLoginNotExist(expected.login)

        val actual = UserManager.create(expected.login, expected.password)

        Assertions.assertEquals(expected.id, actual)
        verify(mockRep, times(1)).create(expected.login, expected.password)
        verify(mockRep, times(1)).isLoginNotExist(expected.login)
    }

    @Test
    @DisplayName("Read User")
    fun readUser() {
        val expected = UserDataMother.admin()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = UserManager.read(expected.id)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).read(expected.id)
    }

    @Test
    @DisplayName("Update User")
    fun updateUser() {
        val expected = UserDataMother.updated()
        doNothing().`when`(mockRep).update(expected)

        UserManager.update(expected)

        verify(mockRep, times(1)).update(expected)
        verify(mockRep, times(1)).isAdmin(adminId)
    }


    @Test
    @DisplayName("Delete User")
    fun deleteUser() {
        val uId = 5
        doNothing().`when`(mockRep).delete(uId)

        UserManager.delete(uId)

        verify(mockRep, times(1)).delete(uId)
        verify(mockRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Get all User")
    fun getAllUser() {
        val expected = UserDataMother.all()
        doReturn(expected).`when`(mockRep).getAll()

        val actual = UserManager.getAll()

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).getAll()
    }

    @Test
    @DisplayName("Update credentials User")
    fun updateCredentialsUser() {
        val expected = UserDataMother.updated()
        doReturn(expected).`when`(mockRep).updateCredentials(expected.id, expected.login, expected.password)

        val actual = UserManager.updateCredentials(expected.id, expected.login, expected.password)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).updateCredentials(expected.id, expected.login, expected.password)
        verify(mockRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Add to favorite User")
    fun addToFavoriteUser() {
        val uId = 5
        val rId = 5
        doNothing().`when`(mockRep).addToFavorite(uId, rId)

        UserManager.addToFavorite(uId, rId)

        verify(mockRep, times(1)).addToFavorite(uId, rId)
        verify(mockRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Delete from favorite User")
    fun deleteFromFavoriteUser() {
        val uId = 5
        val rId = 5
        doNothing().`when`(mockRep).deleteFromFavorite(uId, rId)

        UserManager.deleteFromFavorite(uId, rId)

        verify(mockRep, times(1)).deleteFromFavorite(uId, rId)
        verify(mockRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Get published recipes User")
    fun getPublishedRecipesUser() {
        val uId = 5
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        doReturn(expected).`when`(mockRep).getPublishedRecipes(uId)

        val actual = UserManager.getPublishedRecipes(uId)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).getPublishedRecipes(uId)
    }

    @Test
    @DisplayName("Is admin User")
    fun isAdminUser() {
        val uId = 1
        val expected = true

        val actual = UserManager.isAdmin(uId)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Get by login User")
    fun getByLoginUser() {
        val expected = UserDataMother.admin()
        doReturn(expected).`when`(mockRep).getByLogin(expected.login)

        val actual = UserManager.getByLogin(expected.login)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).getByLogin(expected.login)
    }
}