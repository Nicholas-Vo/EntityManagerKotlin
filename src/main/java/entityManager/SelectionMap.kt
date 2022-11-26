package entityManager

import org.bukkit.entity.Player
import java.util.*

class SelectionMap {
    private val selections: MutableMap<UUID, EntitySelection?> = HashMap()
    fun getPlayerSelection(player: Player): EntitySelection? {
        return selections[player.uniqueId]
    }

    fun setSelection(player: Player, theSelection: EntitySelection?) {
        selections[player.uniqueId] = theSelection
    }

    fun destroySelection(player: Player) {
        selections[player.uniqueId] = null
    }
}