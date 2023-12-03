package bl.unit

import bl.managers.AccountService
import bl.managers.CommentManager
import bl.managers.UserManager
import bl.repositories.ICommentRepository
import bl.repositories.IUserRepository
import da.data.mother.CommentDataMother
import org.junit.jupiter.api.*
import org.mockito.kotlin.*

@TestMethodOrder(MethodOrderer.Random::class)
class CommentTest {

    private lateinit var mockRep: ICommentRepository
    private lateinit var mockUserRep: IUserRepository
    val adminId = 1

    @BeforeEach
    fun setMocks() {
        mockRep = mock<ICommentRepository>()
        CommentManager.registerRepository(mockRep)

        mockUserRep = mock<IUserRepository> {
            on { isAdmin(adminId) } doReturn true
        }
        UserManager.registerRepository(mockUserRep)

        AccountService.setId(adminId)
    }

    @Test
    @DisplayName("Create Comment")
    fun createComment() {
        val expected = CommentDataMother.newComment()
        val uId = AccountService.getCurrentUserId() ?: throw Exception("no id for test")
        doReturn(expected.id).`when`(mockRep).create(uId, expected.text, 1)

        val actual1 = CommentManager.create(expected.text, 1)

        Assertions.assertEquals(expected.id, actual1)
        verify(mockRep, times(1)).create(uId, expected.text, 1)
    }

    @Test
    @DisplayName("Read Comment")
    fun readComment() {
        val expected = CommentDataMother.readedComment()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = CommentManager.read(expected.id)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).read(expected.id)
    }

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentDataMother.updatedComment()
        doNothing().`when`(mockRep).update(expected)

        CommentManager.update(expected)

        verify(mockRep, times(1)).update(expected)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }


    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val cId = 1
        doNothing().`when`(mockRep).delete(cId)

        CommentManager.delete(cId)

        verify(mockRep, times(1)).delete(cId)
    }

    @Test
    @DisplayName("Update text Comment")
    fun updateTextComment() {
        val expected = CommentDataMother.updatedComment()
        doReturn(expected).`when`(mockRep).updateText(expected.id, expected.text)


        val actual = CommentManager.updateText(expected.id, expected.text)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).updateText(expected.id, expected.text)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Is owner")
    fun isOwner() {
        val expected = true
        val cId = 1
        doReturn(adminId).`when`(mockRep).getOwnerID(cId)

        val actual = CommentManager.isOwner(cId, adminId)

        Assertions.assertEquals(expected, actual)
        verify(mockRep, times(1)).getOwnerID(cId)
    }
}