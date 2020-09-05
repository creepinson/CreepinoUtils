package dev.throwouterror.bukkit.util.menu

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * A base class for creating inventory menus in Spigot.
 * This system was inspired by Kody Simpson, so keep in mind that most of the credit goes to him.
 */
abstract class Menu     // Constructor for Menu. Pass in a PlayerMenuUtility so that
// we have information on who's menu this is and
// what info is to be transfered
(protected var playerMenuUtility: PlayerMenuUtility) : InventoryHolder {
    /**
     * A random number generator instance that can be used for choosing a random slot or config option.
     */
    protected var random = Random()
    protected var inv: Inventory? = null
    protected var FILLER_GLASS = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ")

    // let each menu decide their name
    abstract val menuName: String?

    // let each menu decide their slot amount
    abstract val slots: Int

    // let each menu decide how the items in the menu will be handled when clicked
    abstract fun handleMenu(e: InventoryClickEvent)

    // let each menu decide what items are to be placed in the inventory menu
    abstract fun setMenuItems()
    abstract fun close()

    /**
     * When called, an inventory is created and opened for the player
     */
    fun open() {
        // The owner of the inventory created is the Menu itself,
        // so we are able to reverse engineer the Menu object from the
        // inventoryHolder in the MenuListener class when handling clicks
        inv = Bukkit.createInventory(this, slots, menuName!!)

        //grab all the items specified to be used for this menu and add to inventory
        setMenuItems()

        // open the inventory for the player
        playerMenuUtility.owner.openInventory(inventory)
    }

    // Overridden method from the InventoryHolder interface
    override fun getInventory(): Inventory {
        return inv!!
    }

    val previous: Optional<Menu>
        get() = playerMenuUtility.getPreviousMenu()

    /**
     * Helpful utility method to fill all remaining slots with "filler glass"
     */
    fun setFillerGlass() {
        for (i in 0 until slots) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS)
            }
        }
    }

    fun makeItem(material: Material?, displayName: String?, vararg lore: String?): ItemStack {
        val item = ItemStack(material!!)
        val itemMeta = item.itemMeta
        itemMeta!!.setDisplayName(displayName)
        itemMeta.lore = listOf(*lore)
        item.itemMeta = itemMeta
        return item
    }

    fun makeHeadItem(displayName: String?, targetPlayer: OfflinePlayer?, vararg lore: String?): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val itemMeta = item.itemMeta as SkullMeta?
        itemMeta!!.setDisplayName(displayName)
        itemMeta.owningPlayer = targetPlayer
        itemMeta.lore = listOf(*lore)
        item.itemMeta = itemMeta
        return item
    }
}