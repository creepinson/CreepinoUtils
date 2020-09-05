package me.creepinson.api.nms

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * Wraps versions to be able to easily use different NMS server versions
 * @author Wesley Smith
 * Modified by Creepinson
 * @since 1.0
 */
interface VersionWrapper {
    /**
     * Gets the next available NMS container id for the player
     * @param player The player to get the next container id of
     * @return The next available NMS container id0
     */
    fun getNextContainerId(player: Player?): Int

    /**
     * Closes the current inventory for the player
     * @param player The player that needs their current inventory closed
     */
    fun handleInventoryCloseEvent(player: Player?)

    /**
     * Sends PacketPlayOutOpenWindow to the player with the container id
     * @param player The player to send the packet to
     * @param containerId The container id to open
     */
    fun sendPacketOpenWindow(player: Player?, containerId: Int)

    /**
     * Sends PacketPlayOutCloseWindow to the player with the contaienr id
     * @param player The player to send the packet to
     * @param containerId The container id to close
     */
    fun sendPacketCloseWindow(player: Player?, containerId: Int)

    /**
     * Sets the NMS player's active container to the default one
     * @param player The player to set the active container of
     */
    fun setActiveContainerDefault(player: Player?)

    /**
     * Sets the NMS player's active container to the one supplied
     * @param player The player to set the active container of
     * @param container The container to set as active
     */
    fun setActiveContainer(player: Player?, container: Any?)

    /**
     * Sets the supplied windowId of the supplied Container
     * @param container The container to set the windowId of
     * @param containerId The new windowId
     */
    fun setActiveContainerId(container: Any?, containerId: Int)

    /**
     * Adds a slot listener to the supplied container for the player
     * @param container The container to add the slot listener to
     * @param player The player to have as a listener
     */
    fun addActiveContainerSlotListener(container: Any?, player: Player?)

    /**
     * Gets the [Inventory] wrapper of the supplied NMS container
     * @param container The NMS container to get the [Inventory] of
     * @return The inventory of the NMS container
     */
    fun toBukkitInventory(container: Any?): Inventory?

    /**
     * Creates a new ContainerAnvil
     * @param player The player to get the container of
     * @return The Container instance
     */
    fun newContainerAnvil(player: Player?): Any?
}