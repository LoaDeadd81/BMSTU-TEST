package da.data.mother

import bl.entities.Comment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CommentDataMother {
    fun newComment(): Comment {
        return Comment(6, LocalDateTime.now(), "text", UserDataMother.admin())
    }

    fun newCommente2e(): Comment {
        return Comment(6, LocalDateTime.now(), "text", UserDataMother.created())
    }

    fun updatedComment(): Comment {
        return Comment(
            2,
            LocalDateTime.parse("2023-02-02 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "text_new",
            UserDataMother.regular()
        )
    }

    fun readedComment(): Comment {
        return Comment(
            1,
            LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "text1",
            UserDataMother.admin()
        )
    }

    fun regularComment(): Comment {
        return Comment(
            1,
            LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "text1",
            UserDataMother.admin()
        )
    }

    fun favoriteComment(): Comment {
        return Comment(
            5,
            LocalDateTime.parse("2023-05-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "text5",
            UserDataMother.lastUser()
        )
    }

    fun readyToPublishComment(): Comment {
        return Comment(
            2,
            LocalDateTime.parse("2023-02-02 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "text2",
            UserDataMother.regular()
        )
    }
}
