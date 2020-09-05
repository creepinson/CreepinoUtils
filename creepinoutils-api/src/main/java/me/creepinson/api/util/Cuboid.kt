package me.creepinson.api.util

import dev.throwouterror.util.math.Tensor
import dev.throwouterror.util.math.shape.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.*
import kotlin.math.max
import kotlin.math.min

class BukkitCuboid : ConfigurationSerializable, Iterable<Block?>, Cloneable {
    protected var worldName: String? = null
    val cuboid: Cuboid

    constructor(cuboid: BukkitCuboid) : this(cuboid.worldName, cuboid.cuboid.minPoint.x, cuboid.cuboid.minPoint.y, cuboid.cuboid.minPoint.z, cuboid.cuboid.maxPoint.x, cuboid.cuboid.maxPoint.y, cuboid.cuboid.maxPoint.z) {}
    constructor(loc: Location?) : this(loc, loc) {}
    constructor(loc1: Location?, loc2: Location?) {
        if (loc1 != null && loc2 != null) {
            if (loc1.world != null && loc2.world != null) {
                check(loc1.world!!.uid == loc2.world!!.uid) { "The 2 locations of the cuboid must be in the same world!" }
            } else {
                throw NullPointerException("One/both of the worlds is/are null!")
            }
            worldName = loc1.world!!.name
            val xPos1 = min(loc1.x, loc2.x)
            val yPos1 = min(loc1.y, loc2.y)
            val zPos1 = min(loc1.z, loc2.z)
            val xPos2 = max(loc1.x, loc2.x)
            val yPos2 = max(loc1.y, loc2.y)
            val zPos2 = max(loc1.z, loc2.z)
            this.cuboid = Cuboid(xPos1, xPos2, yPos1, yPos2, zPos1, zPos2)
        } else {
            throw NullPointerException("One/both of the locations is/are null!")
        }
    }

    constructor(worldName: String?, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        if (worldName == null || Bukkit.getServer().getWorld(worldName) == null) throw NullPointerException("One/both of the worlds is/are null!")
        this.worldName = worldName
        val xPos1 = min(x1, x2)
        val xPos2 = max(x1, x2)
        val yPos1 = min(y1, y2)
        val yPos2 = max(y1, y2)
        val zPos1 = min(z1, z2)
        val zPos2 = max(z1, z2)
        this.cuboid = Cuboid(xPos1, xPos2, yPos1, yPos2, zPos1, zPos2)
    }

    fun containsLocation(location: Location): Boolean {
        return location.world!!.name == worldName && Tensor(location.x, location.y, location.z).contains(cuboid.minPoint, cuboid.maxPoint)
    }

    fun containsVector(vector: Tensor): Boolean {
        return cuboid.contains(vector)
    }

    val blocks: MutableList<Block>
        get() {
            val blockList: MutableList<Block> = ArrayList()
            val world = world
            if (world != null) {
                for (x in cuboid.minPoint.x.toInt()..cuboid.maxPoint.x.toInt()) {
                    var y: Int = cuboid.minPoint.y.toInt()
                    while (y <= cuboid.maxPoint.y && y <= world.maxHeight) {
                        for (z in cuboid.minPoint.z.toInt()..cuboid.maxPoint.z.toInt()) {
                            blockList.add(world.getBlockAt(x, y, z))
                        }
                        y++
                    }
                }
            }
            return blockList
        }
    val blockLocations: List<Location>
        get() {
            val blockList: MutableList<Location> = ArrayList()
            val world = world
            if (world != null) {
                for (x in cuboid.minPoint.x.toInt()..cuboid.maxPoint.x.toInt()) {
                    var y: Int = cuboid.minPoint.y.toInt()
                    while (y <= cuboid.maxPoint.y && y <= world.maxHeight) {
                        for (z in cuboid.minPoint.z.toInt()..cuboid.maxPoint.z.toInt()) {
                            blockList.add(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
                        }
                        y++
                    }
                }
            }
            return blockList
        }
    val lowerLocation: Location
        get() = Location(world, cuboid.minPoint.x, cuboid.minPoint.y, cuboid.minPoint.z)
    val upperLocation: Location
        get() = Location(world, cuboid.maxPoint.x, cuboid.maxPoint.y, cuboid.maxPoint.z)
    val volume: Double
        get() = (cuboid.maxPoint.x - cuboid.minPoint.x + 1) * (cuboid.maxPoint.y - cuboid.minPoint.y + 1) * (cuboid.maxPoint.z - cuboid.minPoint.z + 1)
    var world: World?
        get() = Bukkit.getServer().getWorld(worldName!!)
                ?: throw NullPointerException("World '$worldName' is not loaded.")
        set(world) {
            if (world != null) worldName = world.name else throw NullPointerException("The world cannot be null.")
        }

    override fun clone(): BukkitCuboid {
        return BukkitCuboid(this)
    }

    override fun iterator(): MutableListIterator<Block> {
        return blocks.listIterator()
    }

    override fun serialize(): Map<String, Any> {
        val serializedCuboid: MutableMap<String, Any> = HashMap()
        serializedCuboid["worldName"] = worldName!!
        serializedCuboid["x1"] = cuboid.minPoint.x
        serializedCuboid["x2"] = cuboid.maxPoint.x
        serializedCuboid["y1"] = cuboid.minPoint.y
        serializedCuboid["y2"] = cuboid.maxPoint.y
        serializedCuboid["z1"] = cuboid.minPoint.z
        serializedCuboid["z2"] = cuboid.maxPoint.z
        return serializedCuboid
    }

    companion object {
        fun deserialize(serializedCuboid: Map<String?, Any?>): BukkitCuboid? {
            return try {
                val worldName = serializedCuboid["worldName"] as String?
                val xPos1 = serializedCuboid["x1"] as Double
                val xPos2 = serializedCuboid["x2"] as Double
                val yPos1 = serializedCuboid["y1"] as Double
                val yPos2 = serializedCuboid["y2"] as Double
                val zPos1 = serializedCuboid["z1"] as Double
                val zPos2 = serializedCuboid["z2"] as Double
                BukkitCuboid(worldName, xPos1, yPos1, zPos1, xPos2, yPos2, zPos2)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
    }
}