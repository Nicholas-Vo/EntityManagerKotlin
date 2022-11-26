package entityManager.teleporter

import org.bukkit.entity.Player
import java.util.*

class EntityTeleportMap {
    private val map: MutableMap<UUID, EntityList> = HashMap()
    fun getPlayerList(p: Player): EntityList? {
        map.putIfAbsent(p.uniqueId, EntityList())
        return map[p.uniqueId]
    }
}