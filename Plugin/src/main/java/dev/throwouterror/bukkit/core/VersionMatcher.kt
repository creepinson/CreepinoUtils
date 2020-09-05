package dev.throwouterror.bukkit.core

import dev.throwouterror.bukkit.v1_12_R1.utils.NMSWrapper1_12_R1
import dev.throwouterror.bukkit.v1_14_R1.utils.NMSWrapper1_14_R1
import me.creepinson.api.nms.VersionWrapper
import org.bukkit.Bukkit
import java.util.*

/**
 * Matches the server's NMS version to its [VersionWrapper]
 *
 * @author Wesley Smith
 * @since 1.2.1
 */
class VersionMatcher {
    /**
     * The server's version
     */
    private val serverVersion = Bukkit.getServer().javaClass.getPackage().name.split("\\.").toTypedArray()[3].substring(1)

    /**
     * All available [VersionWrapper]s
     */
    private val versions: List<Class<out VersionWrapper>> = listOf(
            NMSWrapper1_12_R1::class.java,
            NMSWrapper1_14_R1::class.java
    )

    /**
     * Matches the server version to it's [VersionWrapper]
     *
     * @return The [VersionWrapper] for this server
     * @throws RuntimeException If AnvilGUI doesn't support this server version
     */
    fun match(): VersionWrapper {
        return try {
            versions.stream()
                    .filter { version: Class<out VersionWrapper> -> version.simpleName.substring(10) == serverVersion }
                    .findFirst().orElseThrow { RuntimeException("Your server version isn't supported in AnvilGUI!") }
                    .newInstance()
        } catch (ex: IllegalAccessException) {
            throw RuntimeException(ex)
        } catch (ex: InstantiationException) {
            throw RuntimeException(ex)
        }
    }
}