package dev.throwouterror.bukkit.data

import java.io.IOException
import java.io.UncheckedIOException
import java.net.URL
import java.util.*
import javax.persistence.SharedCacheMode
import javax.persistence.ValidationMode
import javax.persistence.spi.ClassTransformer
import javax.persistence.spi.PersistenceUnitInfo
import javax.persistence.spi.PersistenceUnitTransactionType
import javax.sql.DataSource


fun persistenceUnitInfo(classNames: List<String>): PersistenceUnitInfo? {
    return object : PersistenceUnitInfo {
        override fun getPersistenceUnitName(): String {
            return "CreepinoUtilsPersistenceUnit"
        }

        override fun getPersistenceProviderClassName(): String {
            return "org.hibernate.jpa.HibernatePersistenceProvider"
        }

        override fun getTransactionType(): PersistenceUnitTransactionType {
            return PersistenceUnitTransactionType.RESOURCE_LOCAL
        }

        override fun getJtaDataSource(): DataSource? {
            return null
        }

        override fun getNonJtaDataSource(): DataSource? {
            return null
        }

        override fun getMappingFileNames(): List<String> {
            return Collections.emptyList()
        }

        override fun getJarFileUrls(): List<URL> {
            return try {
                Collections.list(this.javaClass
                        .classLoader
                        .getResources(""))
            } catch (e: IOException) {
                throw UncheckedIOException(e)
            }
        }

        override fun getPersistenceUnitRootUrl(): URL? {
            return null
        }

        override fun getManagedClassNames(): List<String> {
            return classNames
        }

        override fun excludeUnlistedClasses(): Boolean {
            return false
        }

        override fun getSharedCacheMode(): SharedCacheMode? {
            return null
        }

        override fun getValidationMode(): ValidationMode? {
            return null
        }

        override fun getProperties(): Properties {
            return Properties()
        }

        override fun getPersistenceXMLSchemaVersion(): String? {
            return null
        }

        override fun getClassLoader(): ClassLoader? {
            return null
        }

        override fun addTransformer(transformer: ClassTransformer?) {}
        override fun getNewTempClassLoader(): ClassLoader? {
            return null
        }
    }
}