package bl.unit.data.mother

import bl.entities.IngredientInStage
import bl.entities.IngredientType
import bl.entities.ProcessingType

object IngredientInStageDataMother {
    fun regularIngredient(): IngredientInStage {
        return IngredientInStage(1, "name1", IngredientType.MEAT, 1, 1, ProcessingType.WASH)
    }

    fun favoriteIngredient(): IngredientInStage {
        return IngredientInStage(5, "name5", IngredientType.MEAT, 5, 5, ProcessingType.WASH)
    }

    fun readyToPublishIngredient(): IngredientInStage {
        return IngredientInStage(2, "name2", IngredientType.MEAT, 2, 2, ProcessingType.WASH)
    }
}