package exposed

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.system.measureTimeMillis

object ExposedBench {
    private lateinit var database: Database
    fun connect() {
        database = Database.connect(
            "jdbc:postgresql://localhost:7543/back?currentSchema=exposed",
            user = "postgres",
            password = "postgres"
        )
    }

    fun insert(num: Int): Float {
        val time = measureTimeMillis {
            for (i in 0..num) {
                transaction {
                    UserTable.new {
                        this.login = "login$i"
                        this.password = "password$i"
                    }
                }
            }
        }

        return time.toFloat() / num.toFloat()
    }

    fun searchById(num: Int): Float {

        val time = measureTimeMillis {
            for (i in 0..num) {
                transaction {
                    UserTable.findById(i + 1)
                }
            }
        }

        return time.toFloat() / num.toFloat()
    }

    fun searchByName(num: Int): Float {
        val time = measureTimeMillis {
            for (i in 0..num) {
                transaction {
                    UserTable.find { Users.login eq "login$i" }.firstOrNull()
                }
            }
        }

        return time.toFloat() / num.toFloat()
    }

    fun delete(num: Int): Float {
        val time = measureTimeMillis {
            for (i in 0..num) {
                transaction {
                    UserTable.findById(i + 1)?.delete()
                }
            }
        }

        return time.toFloat() / num.toFloat()
    }
}