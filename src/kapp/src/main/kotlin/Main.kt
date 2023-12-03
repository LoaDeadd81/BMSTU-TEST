package main

import api.RestApi
import bl.managers.CommentManager
import bl.managers.IngredientManager
import bl.managers.RecipeManager
import bl.managers.UserManager
import com.zaxxer.hikari.pool.HikariPool
import da.repositories.factory.PgRepositoryFactory
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("mainLogger")

    try {
        val main_shema = System.getenv("MAIN_SCHEMA")
        val repositoryFactory = PgRepositoryFactory(main_shema)
        CommentManager.registerRepository(repositoryFactory.createCommentRepository())
        IngredientManager.registerRepository(repositoryFactory.createIngredientRepository())
        RecipeManager.registerRepository(repositoryFactory.createRecipeRepository())
        UserManager.registerRepository(repositoryFactory.createUserRepository())

        val api = RestApi()
        api.run()

    } catch (e: HikariPool.PoolInitializationException) {
        println("Не удалось подключиться к БД")
        println(e.message)
        logger.error("Failed to connect to the database", e)
    } catch (e: Exception) {
        println("Ошибка")
        println(e.message)
        logger.error("Exception", e)
    }
}