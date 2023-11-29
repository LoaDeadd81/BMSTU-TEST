package da.unit.data.mother

import bl.entities.Stage

object StageDataMother {
    fun regularStage(): Stage {
        return Stage(1, 1, "desc1", 1, listOf(IngredientInStageDataMother.regularIngredient()))
    }

    fun updatedStage(): Stage {
        return Stage(6, 1, "desc1", 1, listOf(IngredientInStageDataMother.regularIngredient()))
    }

    fun favoriteStage(): Stage {
        return Stage(5, 5, "desc5", 1, listOf(IngredientInStageDataMother.favoriteIngredient()))
    }

    fun readyToPublishStage(): Stage {
        return Stage(2, 2, "desc2", 1, listOf(IngredientInStageDataMother.readyToPublishIngredient()))
    }
}