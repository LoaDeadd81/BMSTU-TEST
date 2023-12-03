package bl.unit.data.builder

import bl.entities.*
import bl.unit.data.mother.CommentDataMother
import bl.unit.data.mother.IngredientInStageDataMother
import bl.unit.data.mother.StageDataMother
import bl.unit.data.mother.UserDataMother
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecipeDataBuilder {
    private var recipe = Recipe(
        1,
        "name1",
        "desc1",
        1,
        1,
        PFC(1, 1, 1),
        LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        RecipeState.PUBLISHED,
        listOf(StageDataMother.regularStage()),
        listOf(CommentDataMother.regularComment()),
        UserDataMother.admin(),
        listOf(IngredientInStageDataMother.regularIngredient())
    )

    fun withMeta(
        id: Int,
        name: String,
        desc: String,
        time: Int,
        servNum: Int,
        pfc: PFC,
        date: LocalDateTime,
        state: RecipeState
    ): RecipeDataBuilder {
        this.recipe.id = id
        this.recipe.name = name
        this.recipe.description = desc
        this.recipe.time = time
        this.recipe.servingsNum = servNum
        this.recipe.pfc = pfc
        this.recipe.date = date
        this.recipe.state = state
        return this
    }

    fun withId(id: Int): RecipeDataBuilder {
        this.recipe.id = id
        return this
    }

    fun withName(name: String): RecipeDataBuilder {
        this.recipe.name = name
        return this
    }

    fun withState(state: RecipeState): RecipeDataBuilder {
        this.recipe.state = state
        return this
    }

    fun withStages(stages: List<Stage>): RecipeDataBuilder {
        this.recipe.stages = stages
        return this
    }

    fun withComments(comments: List<Comment>): RecipeDataBuilder {
        this.recipe.comments = comments
        return this
    }

    fun withOwner(user: User): RecipeDataBuilder {
        this.recipe.owner = user
        return this
    }

    fun withIngredients(ingredientsInStage: List<IngredientInStage>): RecipeDataBuilder {
        this.recipe.ingredients = ingredientsInStage
        return this
    }

    fun build(): Recipe {
        return recipe
    }

}