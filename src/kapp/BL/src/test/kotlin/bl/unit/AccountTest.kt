package bl.unit

import bl.managers.AccountService
import bl.managers.UserManager
import bl.repositories.IUserRepository
import bl.unit.data.mother.UserDataMother
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.*

@TestMethodOrder(MethodOrderer.Random::class)
class AccountTest {
    private lateinit var mockUserRep: IUserRepository
    val adminId = 1

    @BeforeEach
    fun setMocks() {
        mockUserRep = mock<IUserRepository>()
        UserManager.registerRepository(mockUserRep)
    }

    @Test
    @DisplayName("Log in")
    fun logIn() {
        val expected = UserDataMother.admin()
        doReturn(expected).`when`(mockUserRep).getByLogin(expected.login)

        val actual1 = AccountService.logIN(expected.login, expected.password)
        val actual2 = AccountService.getCurrentUserId()

        assertEquals(expected, actual1)
        assertEquals(expected.id, actual2)
        verify(mockUserRep, times(1)).getByLogin(expected.login)
    }

    @Test
    @DisplayName("Set/get id")
    fun setGetId() {
        val expected = UserDataMother.admin().id

        AccountService.setId(expected)
        val actual = AccountService.getCurrentUserId()

        assertEquals(expected, actual)
    }
}