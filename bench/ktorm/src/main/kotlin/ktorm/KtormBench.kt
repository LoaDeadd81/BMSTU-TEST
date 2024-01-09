package ktorm

import org.ktorm.database.Database
import org.ktorm.dsl.*
import kotlin.system.measureTimeMillis


object KtormBench {
    private lateinit var database: Database

    fun connect() {
        database = Database.connect(
            "jdbc:postgresql://localhost:7543/back?currentSchema=ktorm",
            user = "postgres",
            password = "postgres"
        )
    }

    fun insert(num: Int): Float {
        val time = measureTimeMillis {
            for (i in 0..num) {
                database.insert(Users) {
                    set(it.login, "login$i")
                    set(it.password, "password$i")
                }
            }
        }

        return time.toFloat() / num.toFloat()
    }

    fun searchById(num: Int): Float {

        val time = measureTimeMillis {
            for (i in 0..num) {
                database.from(Users).select().where { Users.id eq i + 1 }.rowSet
            }
        }

        return time.toFloat() / num.toFloat()
    }

    fun searchByName(num: Int): Float {
        val time = measureTimeMillis {
            for (i in 0..num) {
                database.from(Users).select().where { Users.login eq "login$i" }.rowSet
            }
        }

        return time.toFloat() / num.toFloat()
    }

    fun delete(num: Int): Float {
        val time = measureTimeMillis {
            for (i in 0..num) {
                database.delete(Users) {it.id eq i + 1}
            }
        }

        return time.toFloat() / num.toFloat()
    }
}