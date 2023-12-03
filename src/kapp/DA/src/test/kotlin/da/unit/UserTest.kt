package da.unit

import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import da.dao.*
import da.exeption.NotFoundException
import da.unit.data.builder.RecipeDataBuilder
import da.unit.data.mother.RecipePreviewDataMother
import da.unit.data.mother.UserDataMother
import da.unit.db.PGDataSourceProvider
import da.unit.db.factory
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@FlywayTest(DataSource(PGDataSourceProvider::class))
class UserTest {

    private val repository = factory.createUserRepository()

    @Test
    @DisplayName("Create user")
    fun createUser() {
        val expected = UserDataMother.created()

        val uid = repository.create(expected.login, expected.password)

        val actual = transaction {
            UserTable.findById(uid)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get User")
    fun getUser() {
        val expected = UserDataMother.admin()

        val actual = repository.read(expected.id)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update User")
    fun updateUser() {
        val expected = UserDataMother.updated()

        repository.update(expected)

        val actual = transaction {
            UserTable.findById(expected.id)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete User")
    fun deleteUser() {
        val id = 5

        repository.delete(id)

        val ent = EntityID(id, Users)
        Assertions.assertTrue(transaction { CommentTable.find { Comments.autor eq ent }.empty() })
        Assertions.assertTrue(transaction { RecipeTable.find { Recipes.owner eq ent }.empty() })
        Assertions.assertTrue(transaction { SavedRecipeTable.find { SavedRecipes.user eq ent }.empty() })
    }

    @Test
    @DisplayName("Get all Users")
    fun getAllUsers() {
        val expected = UserDataMother.all()

        val actual = repository.getAll()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update credentials")
    fun updateCredentials() {
        val expected = UserDataMother.updated()

        val actual1 = repository.updateCredentials(expected.id, expected.login, expected.password)

        val actual2 = transaction {
            UserTable.findById(expected.id)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Add to favorite")
    fun addToFavorite() {
        val expected = RecipePreviewDataMother.favorite()
        val uId = 5
        val rId = 5

        repository.addToFavorite(uId, rId)

        val ent = EntityID(uId, Users)
        val actual2 = transaction {
            SavedRecipeTable.find { SavedRecipes.user eq ent }.map { it.recipe.toPreview() }
        }.first()
        Assertions.assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Del from favorite")
    fun delFromFavorite() {
        val uId = 4
        val rId = 4

        repository.deleteFromFavorite(uId, rId)

        val uEnt = EntityID(uId, Users)
        val rEnt = EntityID(rId, Recipes)
        Assertions.assertTrue(
            transaction {
                SavedRecipeTable.find { SavedRecipes.user eq uEnt and (SavedRecipes.recipe eq rEnt) }.empty()
            }
        )
    }

    @Test
    @DisplayName("Is in favorite")
    fun isInFavorite() {
        val uId = 1
        val rId = 1

        val actual = repository.isInFavorite(uId, rId)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Is login exist")
    fun isLoginNotExist() {
        val login = "lalala"

        val actual = repository.isLoginNotExist(login)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Is admin")
    fun isAdmin() {
        val uId = 1

        val actual = repository.isAdmin(uId)

        assertTrue(actual)
    }

    @Test
    @DisplayName("Get by login")
    fun getByLogin() {
        val expected = UserDataMother.admin()
        val login = expected.login

        val actual = repository.getByLogin(login)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get published recipes")
    fun getPublishedRecipes() {
        val expected = listOf(RecipeDataBuilder().build().toPreview())
        val uId = 1

        val actual = repository.getPublishedRecipes(uId)

        Assertions.assertEquals(expected, actual)
    }

}
