package da.repositories.factory

import bl.repositories.factory.IRepositoryFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import da.repositories.PgCommentRepository
import da.repositories.PgIngredientRepository
import da.repositories.PgRecipeRepository
import da.repositories.PgUserRepository
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

class PgRepositoryFactory(private val schemaRep: String) : IRepositoryFactory {

    val logger = LoggerFactory.getLogger("mainLogger")

    init {
        val dotenv = dotenv()
        val DB_CONNECT = dotenv["DB_CONNECT"] ?: throw Exception("no DB_CONNECT in env")
        val DB_HOST = dotenv["DB_HOST"] ?: throw Exception("no DB_HOST in env")
        val DB_PORT = dotenv["DB_PORT"] ?: throw Exception("no DB_PORT in env")
        val DB_NAME = dotenv["DB_NAME"] ?: throw Exception("no DB_NAME in env")

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:$DB_CONNECT://$DB_HOST:$DB_PORT/$DB_NAME?currentSchema=$schemaRep"
            driverClassName = "org.postgresql.Driver"
            username = dotenv["DB_USER"] ?: throw Exception("no DB_USER in env")
            password = dotenv["DB_PASSWORD"] ?: throw Exception("no DB_PASSWORD in env")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        Database.connect(HikariDataSource(hikariConfig))

        logger.info("DB with config connected {}", hikariConfig)
    }

    override fun createCommentRepository(): PgCommentRepository = PgCommentRepository()

    override fun createIngredientRepository(): PgIngredientRepository = PgIngredientRepository()

    override fun createRecipeRepository(): PgRecipeRepository = PgRecipeRepository()

    override fun createUserRepository(): PgUserRepository = PgUserRepository()
}