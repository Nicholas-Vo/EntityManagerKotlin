package entityManager.teleporter

import entityManager.Chat
import org.bukkit.Location
import org.bukkit.entity.Player

object TeleportUtils {
    @JvmStatic
    fun teleport(player: Player, location: Location, map: EntityTeleportMap) {
        val list = map.getPlayerList(player)
        if (list == null) {
            player.sendMessage(Chat.red + "Error: Your list doesn't exist.")
            return
        }
        if (list.isEmpty()) {
            player.sendMessage(
                (Chat.red + "You don't have any entities selected. "
                        + Chat.reset) + "To do so, right click with a stone shovel in hand."
            )
            return
        }
        list.teleport(location)
    }
}