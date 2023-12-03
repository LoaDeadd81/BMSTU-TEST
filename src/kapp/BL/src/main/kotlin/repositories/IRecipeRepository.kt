package bl.repositories

import bl.entities.Recipe
import bl.entities.RecipePreview
import bl.entities.Stage

interface IRecipeRepository {
    fun create(obj: Recipe): Int
    fun read(id: Int): Recipe
    fun update(obj: Recipe)
    fun delete(id: Int)
    fun getPublished(): List<RecipePreview>
    fun updateInfo(obj: Recipe): Recipe
    fun updateStages(id: Int, list: List<Stage>): Recipe
    fun getOwnerID(id: Int): Int
    fun getPublishQueue(): List<RecipePreview>
    fun getSavedRecipes(userID: Int): List<RecipePreview>
    fun getOwnRecipes(userID: Int): List<RecipePreview>
    fun getPublishedRecipes(userID: Int): List<RecipePreview>

}