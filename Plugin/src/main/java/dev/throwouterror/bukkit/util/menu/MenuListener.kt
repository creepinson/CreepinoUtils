package dev.throwouterror.bukkit.util.menu

import dev.throwouterror.bukkit.core.CreepinoUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MenuListener : Listener {
    @EventHandler
    fun onInvClose(e: InventoryCloseEvent) {
        if (e.player is Player) {
            val current: PlayerMenuUtility = CreepinoUtils.instance.getPlayerMenuUtility(e.player as Player) ?: return
            val holder = e.inventory.holder
            if (holder is Menu) {
                current.setPreviousMenu(holder as Menu?)
            }
        }
    }

    @EventHandler
    fun onMenuClick(e: InventoryClickEvent) {
        val holder = e.inventory.holder
        // If the inventoryholder of the inventory clicked on
        // is an instance of Menu, then gg. The reason that
        // an InventoryHolder can be a Menu is because our Menu
        // class implements InventoryHolder!!
        if (holder is Menu) {
            e.isCancelled = true // prevent them from messing with the inventory
            if (e.currentItem == null) // deal with null exceptions
                return
            // Since we know our inventoryholder is a menu, get the Menu Object representing
            // the menu we clicked on
            // Call the handleMenu object which takes the event and processes it
            holder.handleMenu(e)
        }
    }
}