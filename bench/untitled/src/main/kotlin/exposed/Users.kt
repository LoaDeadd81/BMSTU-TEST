package exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val login = text("login")
    val password = text("password")
    val isAdmin = bool("is_admin").default(false)
}

class UserTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserTable>(Users)

    var login by Users.login
    var password by Users.password
    var isAdmin by Users.isAdmin
}

