package entityManager.teleporter

import entityManager.EntityManager
import entityManager.teleporter.TeleportUtils.teleport
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class Listeners(p: EntityManager) : Listener {
    private val plugin: EntityManager
    private val map: EntityTeleportMap
    private val selectionWand = ItemStack(Material.ARROW)
    private val teleportWand = ItemStack(Material.BLAZE_ROD)

    init {
        p.server.pluginManager.registerEvents(this, p)
        plugin = p
        map = plugin.entityListMap
    }

    /*
    Represents an event that is called when a player right clicks an entity that also contains the location where the entity was clicked.
     */
    @EventHandler
    fun onPlayerInteractAtEntity(e: PlayerInteractAtEntityEvent) {
        val p = e.player
        if (!p.hasPermission("entitymanager.teleport")) return
        if (e.hand == EquipmentSlot.OFF_HAND) return
        if (!plugin.wandEnabled(p)) {
            return
        }
        val inHand = p.inventory.itemInMainHand
        if (inHand == selectionWand) {
            val theList = map.getPlayerList(p)
            val theEntity = e.rightClicked
            theList?.add(p, theEntity)
        }
        e.isCancelled = true
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val p = e.player
        if (!plugin.wandEnabled(p)) {
            return
        }
        if (e.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        val inHand = p.inventory.itemInMainHand
        if (inHand == teleportWand) {
            val location = e.player.location
            teleport(p, location, map)
        }
    }
}