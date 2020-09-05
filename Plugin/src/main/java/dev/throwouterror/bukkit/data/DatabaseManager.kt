package dev.throwouterror.bukkit.data

import dev.throwouterror.bukkit.core.CreepinoUtils
import dev.throwouterror.util.module.Module
import org.hibernate.cfg.AvailableSettings
import org.hibernate.jpa.HibernatePersistenceProvider
import javax.persistence.EntityManagerFactory

class DatabaseManager {
    lateinit var entityManagerFactory: EntityManagerFactory

    fun disable() {
        entityManagerFactory.close()
    }

    /**
     * Initializes the database manager with a database connection from the creepinoutils config.
     * @param classNames The classes (full package names required) to manage within the database.
     * These class names have to be annotated with @Entity (javax.persistence).
     */
    fun create(classNames: List<String>) {
        val config = CreepinoUtils.instance.config

        entityManagerFactory = HibernatePersistenceProvider().createContainerEntityManagerFactory(
                persistenceUnitInfo(classNames),
                mutableMapOf(
                        AvailableSettings.DRIVER to config.getString("database.driver"),
                        AvailableSettings.URL to "jdbc:mysql://" + config.getString("database.host") + "/" + config.getString("database.name") + "?autoReconnect=true",
                        AvailableSettings.USER to config.getString("database.user"),
                        AvailableSettings.PASS to config.getString("database.password"),
                        AvailableSettings.DIALECT to config.getString("database.dialect"),
                        AvailableSettings.HBM2DDL_AUTO to "create"
                )
        )
    }

}