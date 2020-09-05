package dev.throwouterror.bukkit.util.menu

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * A class extending the functionality of the regular Menu, but making it Paginated
 *
 *
 * This pagination system was made from Jer's code sample. <3
 */
abstract class PaginatedMenu(playerMenuUtility: PlayerMenuUtility) : Menu(playerMenuUtility) {
    /**
     * Keep track of what page the menu is on
     */
    var currentPage = 0
        protected set

    /**
     * 28 is max items because with the border set,
     * 28 empty slots are remaining.
     */
    var maxItemsPerPage = 28
        protected set

    /**
     * The index represents the index of the slot that the loop is on
     */
    protected var index = 0
    override fun handleMenu(e: InventoryClickEvent) {
        if (e.slot == 49) {
            close()
        }
    }

    override fun close() {
        // Default close behaviour, you can override this
        if (previous.isPresent) previous.get().open() else playerMenuUtility.owner.closeInventory()
    }

    // Set the border and menu buttons for the menu
    fun addMenuBorder() {
        inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN.toString() + "Previous Page"))
        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED.toString() + "Close"))
        inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN.toString() + "Next Page"))
        for (i in 0..9) if (inventory.getItem(i) == null) inventory.setItem(i, super.FILLER_GLASS)
        inventory.setItem(17, super.FILLER_GLASS)
        inventory.setItem(18, super.FILLER_GLASS)
        inventory.setItem(26, super.FILLER_GLASS)
        inventory.setItem(27, super.FILLER_GLASS)
        inventory.setItem(35, super.FILLER_GLASS)
        inventory.setItem(36, super.FILLER_GLASS)
        for (i in 44..53) if (inventory.getItem(i) == null) inventory.setItem(i, super.FILLER_GLASS)
    }
}