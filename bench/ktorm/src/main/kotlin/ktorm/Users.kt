package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Users : Table<Nothing>("user") {
    val id = int("id").primaryKey()
    val login = varchar("login")
    val password = varchar("password")
    val isAdmin = boolean("is_admin")
}