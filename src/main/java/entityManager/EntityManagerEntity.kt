package entityManager

import entityManager.Utils.locationToString
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

/**
 * represents each entity within an EntitySelection
 */
class EntityManagerEntity(private val entity: Entity) {
    fun remove() {
        entity.remove()
        EntityManager.instance.logger.info(entity.name + " at " + location())
    }

    private fun location(): String {
        return locationToString(entity.location)
    }

    fun type(): EntityType {
        return entity.type
    }

    fun respawn() {
        entity.world.spawnEntity(entity.location, entity.type)
    }
}