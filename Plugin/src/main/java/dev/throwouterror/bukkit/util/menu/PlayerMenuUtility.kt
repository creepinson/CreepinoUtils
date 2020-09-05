package dev.throwouterror.bukkit.util.menu

import org.bukkit.entity.Player
import java.util.*

/**
 * Companion class to all menus. This is needed to pass information across the entire
 * menu system no matter how many inventories are opened or closed.
 *
 *
 * Each player has one of these objects, and only one.
 */
class PlayerMenuUtility(val owner: Player) {
    private var target: Player? = null
    private var previousMenu: Menu? = null

    /**
     * This can be used for menus that require a target player such as a kill player menu.
     */
    val targetPlayer: Optional<Player>
        get() = Optional.ofNullable(target)

    fun setTargetPlayer(newTarget: Player?) {
        target = newTarget
    }

    fun getPreviousMenu(): Optional<Menu> = Optional.ofNullable(previousMenu)
    fun setPreviousMenu(menu: Menu?) {
        previousMenu = menu
    }
}