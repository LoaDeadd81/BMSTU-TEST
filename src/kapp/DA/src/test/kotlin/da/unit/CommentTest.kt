package da.unit

import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.dao.CommentTable
import da.dao.RecipeTable
import da.dao.toEntity
import da.exeption.NotFoundException
import da.unit.data.mother.CommentDataMother
import da.unit.db.PGDataSourceProvider
import da.unit.db.factory
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class CommentTest {

    private val repository = factory.createCommentRepository()

    @Test
    @DisplayName("Create comment")
    fun createComment() {
        val expected = CommentDataMother.newComment()
        val rId = 1

        val cId = repository.create(expected.autor.id, expected.text, rId)

        val actual1 = transaction {
            CommentTable.findById(cId)?.toEntity()
        }
        val actual2 = transaction {
            RecipeTable.findById(rId)?.toEntity()?.comments?.last()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        expected.date = actual2.date
        assertAll("Add comment asserts", { assertEquals(expected, actual1) }, { assertEquals(expected, actual2) })
    }

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentDataMother.updatedComment()

        repository.update(expected)

        val actual = transaction {
            CommentTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Comment with id = $id not found")
        }
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Read Comment")
    fun readComment() {
        val expected = CommentDataMother.readedComment()

        val actual = repository.read(expected.id)

        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { CommentTable.findById(id) })
    }

    @Test
    @DisplayName("Update text")
    fun updateText() {
        val expected = CommentDataMother.updatedTextComment()

        val actual = repository.updateText(expected.id, expected.text)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get owner id")
    fun getOwnerID() {
        val expected = 1
        val id = 1

        val actual = repository.getOwnerID(id)

        assertEquals(expected, actual)
    }
}