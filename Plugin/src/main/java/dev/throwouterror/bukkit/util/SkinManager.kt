package dev.throwouterror.bukkit.util

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.entity.Player
import sun.net.www.protocol.https.HttpsURLConnectionImpl
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit


object SkinManager {
    /**
     * @return cached skins for disguises. Cleared on server reload/restart.
     * The reason skins get cached is because of Mojang's (code 429) skin request limit.
     * We store the skins based on the unique id of the player the skin corresponds to.
     * This way we can just retrieve the skin multiple times per minute without getting a response code of 429 from mojang.
     * Developers can reset the skin cache using SkinManager.updateCachedSkin.
     * I recommend that any plugin developer who uses this class should include a command that lets admins update the skin cache based on a username.
     * @see Property
     */
    private val skins: Cache<UUID, Property> = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build()

    fun clearCache() {
        skins.invalidateAll()
    }

    /**
     * @param uuid the corresponding unique id of the player that owns the skin.
     * @return [SkinResponse] - a response object containg the error (if there is one) and the success value.
     */
    fun updateCachedSkin(uuid: UUID, skin: Property, insert: Boolean): SkinResponse {
        return if (!skins.asMap().containsKey(uuid)) {
            if (!insert) {
                SkinResponse("The cached skin does not exist!")
            } else {
                skins.put(uuid, skin)
                SkinResponse("The cached skin does not exist... Inserting anyways.")
            }
        } else {
            skins.put(uuid, skin)
            SkinResponse()
        }
    }

    fun setSkin(profile: GameProfile, uuid: UUID): Boolean {
        return try {
            if (skins.asMap().containsKey(uuid)) {
                profile.properties.removeAll("textures")
                profile.properties.put("textures", skins.getIfPresent(uuid))
                true
            } else {
                val connection = URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", com.mojang.util.UUIDTypeAdapter.fromUUID(uuid))).openConnection() as HttpsURLConnectionImpl
                if (connection.responseCode == HttpsURLConnectionImpl.HTTP_OK) {
                    val reply = BufferedReader(InputStreamReader(connection.inputStream)).readLine()
                    val skin = reply.split("\"value\":\"").toTypedArray()[1].split("\"").toTypedArray()[0]
                    val signature = reply.split("\"signature\":\"").toTypedArray()[1].split("\"").toTypedArray()[0]
                    val skinProp: Property = Property("textures", skin, signature)
                    profile.properties.removeAll("textures")
                    profile.properties.put("textures", skinProp)
                    skins.put(uuid, skinProp)
                    true
                } else {
                    println("Connection could not be opened (Response code " + connection.responseCode + ", " + connection.responseMessage + ")")
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun setSkin(player: Player, uuid: UUID): Boolean {
        return try {
            val ent: Any = ReflectionUtils.invokeMethod(player,
                    "getHandle", null)
            val profile: GameProfile = ReflectionUtils.invokeMethod(ent, "getProfile") as GameProfile
            if (skins.asMap().containsKey(uuid)) {
                profile.properties.removeAll("textures")
                profile.properties.put("textures", skins.getIfPresent(uuid))
                true
            } else {
                val connection = URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", com.mojang.util.UUIDTypeAdapter.fromUUID(uuid))).openConnection() as HttpsURLConnectionImpl
                if (connection.responseCode == HttpsURLConnectionImpl.HTTP_OK) {
                    val reply = BufferedReader(InputStreamReader(connection.inputStream)).readLine()
                    val skin = reply.split("\"value\":\"").toTypedArray()[1].split("\"").toTypedArray()[0]
                    val signature = reply.split("\"signature\":\"").toTypedArray()[1].split("\"").toTypedArray()[0]
                    val skinProp: Property = Property("textures", skin, signature)
                    profile.properties.removeAll("textures")
                    profile.properties.put("textures", skinProp)
                    skins.put(uuid, skinProp)
                    true
                } else {
                    println("Connection could not be opened (Response code " + connection.responseCode + ", " + connection.responseMessage + ")")
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * A response object containg the error (if there is one) and the success value.
     */
    class SkinResponse {
        var isSuccessful: Boolean
            private set
        var errorMessage: String
            private set

        constructor(errorMessage: String) {
            isSuccessful = false
            this.errorMessage = errorMessage
        }

        constructor() {
            isSuccessful = true
            errorMessage = ""
        }

        constructor(successful: Boolean, errorMessage: String) {
            isSuccessful = successful
            this.errorMessage = errorMessage
        }
    }
}