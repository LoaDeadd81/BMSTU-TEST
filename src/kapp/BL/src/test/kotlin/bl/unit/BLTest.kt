package bl.unit

import bl.entities.PFC
import bl.entities.RecipeState
import bl.managers.*
import bl.repositories.ICommentRepository
import bl.repositories.IIngredientRepository
import bl.repositories.IRecipeRepository
import bl.repositories.IUserRepository
import da.data.builder.RecipeDataBuilder
import da.data.mother.CommentDataMother
import da.data.mother.IngredientDataMother
import da.data.mother.StageDataMother
import da.data.mother.UserDataMother
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

        assertEquals(expected.id, actual1)
        verify(mockRep, times(1)).create(uId, expected.text, 1)
    }

    @Test
    @DisplayName("Read Comment")
    fun readComment() {
        val expected = CommentDataMother.readedComment()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = CommentManager.read(expected.id)

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
        verify(mockRep, times(1)).getOwnerID(cId)
    }
}

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

        assertEquals(expected.id, actual)
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

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
        verify(mockRep, times(1)).getAll()
    }

    @Test
    @DisplayName("Find by name Ingredient")
    fun findByNameIngredient() {
        val expected = IngredientDataMother.allIngredients()
        val name = "name"
        doReturn(expected).`when`(mockRep).findByName(name)

        val actual = IngredientManager.findByName(name)

        assertEquals(expected, actual)
        verify(mockRep, times(1)).findByName(name)
    }
}

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

        assertEquals(expected.id, actual)
        verify(mockRep, times(1)).create(expected)
    }

    @Test
    @DisplayName("Read Recipe")
    fun readRecipe() {
        val expected = RecipeDataBuilder().withState(RecipeState.PUBLISHED).build()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = RecipeManager.read(expected.id)

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
        verify(mockRep, times(1)).updateInfo(expected)
        verify(mockUserRep, times(2)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Update stages Recipe")
    fun updateStagesRecipe() {
        val expected = RecipeDataBuilder().withStages(listOf(StageDataMother.updatedStage())).build()
        doReturn(expected).`when`(mockRep).updateStages(expected.id, expected.stages)

        val actual = RecipeManager.updateStages(expected.id, expected.stages)

        assertEquals(expected, actual)
        verify(mockRep, times(1)).updateStages(expected.id, expected.stages)
        verify(mockUserRep, times(1)).isAdmin(adminId)
    }
}


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

        assertEquals(expected.id, actual)
        verify(mockRep, times(1)).create(expected.login, expected.password)
        verify(mockRep, times(1)).isLoginNotExist(expected.login)
    }

    @Test
    @DisplayName("Read User")
    fun readUser() {
        val expected = UserDataMother.admin()
        doReturn(expected).`when`(mockRep).read(expected.id)

        val actual = UserManager.read(expected.id)

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
        verify(mockRep, times(1)).getAll()
    }

    @Test
    @DisplayName("Update credentials User")
    fun updateCredentialsUser() {
        val expected = UserDataMother.updated()
        doReturn(expected).`when`(mockRep).updateCredentials(expected.id, expected.login, expected.password)

        val actual = UserManager.updateCredentials(expected.id, expected.login, expected.password)

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
        verify(mockRep, times(1)).getPublishedRecipes(uId)
    }

    @Test
    @DisplayName("Is admin User")
    fun isAdminUser() {
        val uId = 1
        val expected = true

        val actual = UserManager.isAdmin(uId)

        assertEquals(expected, actual)
        verify(mockRep, times(1)).isAdmin(adminId)
    }

    @Test
    @DisplayName("Get by login User")
    fun getByLoginUser() {
        val expected = UserDataMother.admin()
        doReturn(expected).`when`(mockRep).getByLogin(expected.login)

        val actual = UserManager.getByLogin(expected.login)

        assertEquals(expected, actual)
        verify(mockRep, times(1)).getByLogin(expected.login)
    }
}

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

