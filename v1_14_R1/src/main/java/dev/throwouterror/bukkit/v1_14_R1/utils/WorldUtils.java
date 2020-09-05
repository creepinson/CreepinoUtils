package dev.throwouterror.bukkit.v1_14_R1.utils;

import net.minecraft.server.v1_14_R1.Block;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.PacketPlayOutBlockChange;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {
    /**
     * Created by ColonelHedgehog on 11/11/15. This method will send a block change
     * using packets, asynchronously, without actually changing the block. The block
     * will thus appear differently to the player, without actually changing
     * altogether.
     *
     * @param plugin   Bukkit Plugin Class
     * @param player   The player to whom the packet will be sent.
     * @param location The location where the block should be changed.
     * @param material The NEW material to give the block.
     * @param data     The NEW data to give the block.
     */
    public static void maskBlock(JavaPlugin plugin, Player player, Location location, Material material, byte data) {
        // As some have pointed out, it could be unwise to use this as a static.
        // Consider using it as an instance var.
        new BukkitRunnable() {
            @Override
            public void run() {

                BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(),
                        location.getBlockZ());
                PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(
                        ((CraftWorld) location.getWorld()).getHandle(), blockPosition);
                packet.block = Block.getByCombinedId(material.getId() + (data << 12));
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }.runTaskAsynchronously(plugin);
    }

    public static List<org.bukkit.block.Block> getNearbyBlocks(Location location, int radius) {
        List<org.bukkit.block.Block> blocks = new ArrayList<org.bukkit.block.Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
}
