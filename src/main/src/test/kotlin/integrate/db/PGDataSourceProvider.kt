package integrate.db

import com.radcortez.flyway.test.junit.DataSourceInfo
import com.radcortez.flyway.test.junit.DataSourceProvider
import da.repositories.factory.PgRepositoryFactory
import org.junit.jupiter.api.extension.ExtensionContext

val testFactory = PgRepositoryFactory(System.getenv("TEST_SCHEMA") ?: throw Exception("no TEST_SCHEMA in env"))

class PGDataSourceProvider : DataSourceProvider {
    override fun getDatasourceInfo(extensionContext: ExtensionContext?): DataSourceInfo {
        val DB_CONNECT = System.getenv("DB_CONNECT") ?: throw Exception("no DB_CONNECT in env")
        val DB_HOST = System.getenv("DB_HOST") ?: throw Exception("no DB_HOST in env")
        val DB_PORT = System.getenv("DB_PORT") ?: throw Exception("no DB_PORT in env")
        val DB_NAME = System.getenv("DB_NAME") ?: throw Exception("no DB_NAME in env")
        val DB_SHEMA = System.getenv("TEST_SCHEMA") ?: throw Exception("no TEST_SCHEMA in env")
        val DB_USER = System.getenv("DB_USER") ?: throw Exception("no DB_USER in env")
        val DB_PASSWORD = System.getenv("DB_PASSWORD") ?: throw Exception("no DB_PASSWORD in env")

        val url = "jdbc:$DB_CONNECT://$DB_HOST:$DB_PORT/$DB_NAME?currentSchema=$DB_SHEMA"
        val user = DB_USER
        val password = DB_PASSWORD
        return DataSourceInfo.config(url, user, password)
    }

}