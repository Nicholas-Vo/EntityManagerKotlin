package entityManager.teleporter

import entityManager.Chat
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.function.Consumer

class EntityList {
    private val list: MutableSet<Entity> = HashSet()

    /**
     * add an Entity into the set
     * @param entity - the non-player entity to add
     */
    fun add(p: Player, entity: Entity) {
        if (entity is Player) {
            return
        }
        if (list.add(entity)) {
            p.sendMessage("Added " + Chat.red + entity.name + Chat.reset + " to your list.")
            return
        }
        p.sendMessage(Chat.red + entity.name + " is already in your list.")
    }

    fun teleport(location: Location) {
        list.forEach(Consumer { entity: Entity ->
            entity.teleport(
                location
            )
        })
    }

    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    fun clear() {
        list.clear()
    }
}