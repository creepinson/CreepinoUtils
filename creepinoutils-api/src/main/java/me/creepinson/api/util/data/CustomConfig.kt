package me.creepinson.api.util.data

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Custom Config is a wrapper for custom yaml config files.
 */
class CustomConfig(val file: File) : YamlConfiguration() {

    /**
     * Creates a config file with the specified filename and appends ".yml" to it automatically.
     * @param parent The folder to create the config file in
     * @param fileName The config file name (without an extension)
     */
    constructor(parent: File?, fileName: String) : this(File(parent, "$fileName.yml")) {}
    constructor(plugin: Plugin, fileName: String) : this(plugin.dataFolder, fileName) {}

    /**
     * Creates a configuration file in the specified plugin's data folder, and names it "config.yml".
     *
     * @param plugin A bukkit plugin instance.
     */
    constructor(plugin: Plugin) : this(plugin.dataFolder, "config") {}

    fun save() {
        try {
            save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Helper method to load the config file without adding try statements in the plugin itself.
     * @return the config instance being loaded
     */
    fun load(): CustomConfig {
        try {
            load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
        return this
    }

    /**
     * Creates a new configuration file if it doesn't already exist,
     * and loads it into the configuration object.
     */
    fun createCustomConfig(): CustomConfig {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        load()
        return this
    }

    /**
     * Creates a new configuration file and loads it automatically.
     * @param file The file to use. See the other constructors for using it with a bukkit plugin.
     */
    init {
        load()
    }
}