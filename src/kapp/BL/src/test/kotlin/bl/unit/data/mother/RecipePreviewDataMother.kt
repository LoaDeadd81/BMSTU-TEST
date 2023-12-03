package bl.unit.data.mother

import bl.entities.PFC
import bl.entities.RecipePreview
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RecipePreviewDataMother {
    fun favorite(): RecipePreview {
        return RecipePreview(
            5,
            "name5",
            "desc5",
            5,
            5,
            LocalDateTime.parse("2023-05-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            PFC(5, 5, 5)
        )
    }
}