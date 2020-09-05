package dev.throwouterror.bukkit.core

import dev.throwouterror.bukkit.core.command.AnnounceCommand
import dev.throwouterror.bukkit.util.SkinManager
import dev.throwouterror.bukkit.util.menu.MenuListener
import dev.throwouterror.bukkit.util.menu.PlayerMenuUtility
import dev.throwouterror.util.module.ModuleManager
import me.creepinson.api.util.TextUtils.color
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.jvm.Throws

class CreepinoUtils : JavaPlugin() {
    private val playerMenuUtilityMap: HashMap<UUID, PlayerMenuUtility> = HashMap<UUID, PlayerMenuUtility>()

    override fun onEnable() {
        super.onEnable()
        instance = this
        this.config.options().copyDefaults(true)
        saveDefaultConfig()
        getCommand("announce")!!.setExecutor(AnnounceCommand())
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        // Menu listener system
        server.pluginManager.registerEvents(MenuListener(), this)

        try {
            if (this.config.getBoolean("announcer-enabled")) {
                Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(this, {
                    Bukkit.getServer()
                            .broadcastMessage(color(
                                    config.getString("announcer-prefix")
                                            + " "
                                            + config.getString("announcer-message")))
                }, 0L, config.getInt("announcer-interval") * 20.toLong())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ModuleManager.enable()
    }

    override fun onDisable() {
        SkinManager.clearCache()
        ModuleManager.disable()
    }

    override fun reloadConfig() {
        super.reloadConfig()
        ModuleManager.reload()
    }


    /**
     * Provide a player and return a menu system for that player -
     * create one if they don't already have one
     */
    fun getPlayerMenuUtility(p: Player): PlayerMenuUtility? {
        return getPlayerMenuUtility(p.uniqueId)
    }

    @Throws(NullPointerException::class)
    fun getPlayerMenuUtility(id: UUID): PlayerMenuUtility? {
        val playerMenuUtility: PlayerMenuUtility
        // See if the player has a playermenuutility "saved" for them
        return if (!playerMenuUtilityMap.containsKey(id)) {

            // This player doesn't. Make one for them add add it to the hashmap
            playerMenuUtility = PlayerMenuUtility(server.getPlayer(id)!!)
            playerMenuUtilityMap[id] = playerMenuUtility
            playerMenuUtility
        } else {
            // Return the object by using the provided player
            playerMenuUtilityMap.get(id)
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: CreepinoUtils
            private set
    }
}