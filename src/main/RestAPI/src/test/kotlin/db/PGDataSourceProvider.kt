package db

import com.radcortez.flyway.test.junit.DataSourceInfo
import com.radcortez.flyway.test.junit.DataSourceProvider
import da.repositories.factory.PgRepositoryFactory
import io.github.cdimascio.dotenv.dotenv
import org.junit.jupiter.api.extension.ExtensionContext

val dotenv = dotenv()

val testFactory = PgRepositoryFactory(dotenv["TEST_SCHEMA"] ?: throw Exception("no TEST_SCHEMA in env"))

class PGDataSourceProvider : DataSourceProvider {
    override fun getDatasourceInfo(extensionContext: ExtensionContext?): DataSourceInfo {
        val DB_CONNECT = dotenv["DB_CONNECT"] ?: throw Exception("no DB_CONNECT in env")
        val DB_HOST = dotenv["DB_HOST"] ?: throw Exception("no DB_HOST in env")
        val DB_PORT = dotenv["DB_PORT"] ?: throw Exception("no DB_PORT in env")
        val DB_NAME = dotenv["DB_NAME"] ?: throw Exception("no DB_NAME in env")
        val DB_SHEMA = dotenv["TEST_SCHEMA"] ?: throw Exception("no TEST_SCHEMA in env")
        val DB_USER = dotenv["DB_USER"] ?: throw Exception("no DB_USER in env")
        val DB_PASSWORD = dotenv["DB_PASSWORD"] ?: throw Exception("no DB_PASSWORD in env")

        val url = "jdbc:$DB_CONNECT://$DB_HOST:$DB_PORT/$DB_NAME?currentSchema=$DB_SHEMA"
        val user = DB_USER
        val password = DB_PASSWORD
        return DataSourceInfo.config(url, user, password)
    }

}