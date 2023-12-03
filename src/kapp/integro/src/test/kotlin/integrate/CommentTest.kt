package integrate

import bl.managers.AccountService
import bl.managers.CommentManager
import bl.managers.UserManager
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.data.mother.CommentDataMother
import da.exeption.NotFoundException
import integrate.db.PGDataSourceProvider
import integrate.db.testFactory
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class CommentTest {
    private val repository = testFactory.createCommentRepository()

    @BeforeAll
    fun setUser() {
        CommentManager.registerRepository(repository)
        UserManager.registerRepository(testFactory.createUserRepository())

        AccountService.logIN("login1", "password1")
    }

    @Test
    @DisplayName("Create Comment")
    fun createComment() {
        val expected = CommentDataMother.newComment()

        val actual1 = CommentManager.create(expected.text, 1)

        val actual2 = repository.read(actual1)
        expected.date = actual2.date
        assertEquals(expected.id, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Read Comment")
    fun readComment() {
        val expected = CommentDataMother.readedComment()

        val actual = CommentManager.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentDataMother.updatedComment()

        CommentManager.update(expected)

        val actual = repository.read(expected.id)
        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val cId = 1

        CommentManager.delete(cId)

        assertThrows<NotFoundException> { repository.read(cId) }
    }

    @Test
    @DisplayName("Update text Comment")
    fun updateTextComment() {
        val expected = CommentDataMother.updatedComment()

        val actual1 = CommentManager.updateText(expected.id, expected.text)

        val actual2 = repository.read(expected.id)
        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Is owner")
    fun isOwner() {
        val expected = true
        val cId = 1
        val uId = 1

        val actual = CommentManager.isOwner(cId, uId)

        assertEquals(expected, actual)
    }

}