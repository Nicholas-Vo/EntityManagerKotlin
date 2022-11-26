package entityManager

import entityManager.Utils.formatEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*
import java.util.function.BiFunction
import java.util.function.Consumer

class EntitySelection(player: Player, selected: MutableList<Entity?>) {
    private val em = EntityManager.instance
    private val log = em.logger
    private var countMap: MutableMap<EntityType, Int> =
        EnumMap(org.bukkit.entity.EntityType::class.java) // Switched to EnumMap
    private val entities: MutableList<EntityManagerEntity> = ArrayList()
    val player: Player

    /**
     * Get the total number of entities in this selection
     *
     * @return total number of entities
     */
    val total: Int

    init {
        selected.removeIf { e: Entity? -> e is Player }
        em.selectionMap.setSelection(player, this)
        this.player = player
        selected.forEach(Consumer { entity: Entity? -> entities.add(EntityManagerEntity(entity!!)) })

        /*
        calculate number of each entity type
         */
        entities.forEach(Consumer { e: EntityManagerEntity ->
            countMap.putIfAbsent(e.type(), 0)
            countMap.compute(e.type()) // This lambda is interesting... hmm
            { _: EntityType?, value: Int? -> value?.plus(1) ?: 1 }
        })
        total = total()
    }

    private fun total(): Int {
        var sum = 0
        for (`val` in countMap.values) {
            sum += `val`
        }
        return sum
    }

    /**
     * re-spawn entities that were deleted (doesn't account for any NBT data at the moment)
     */
    fun undoRemoval() {
        log.info(player.name + " re-spawned the following entitie(s)")
        entities.forEach(Consumer { obj: EntityManagerEntity -> obj.respawn() })
        em.msg(player, "Successfully undone previous removal of " + Chat.red + total() + Chat.reset + " entities.")
        em.msg(player, Chat.gray + "Note: /em undo does not bring back NBT data. Coming soon!")
        em.selectionMap.destroySelection(player) // destroy this selection Object
    }

    /**
     * Delete the group of entities
     */
    fun removeEntitites() {
        log.info(player.name + " cleared the following entitie(s):")
        entities.forEach(Consumer { obj: EntityManagerEntity -> obj.remove() })
        em.msg(player, "Removed " + Chat.red + total + Chat.reset + " entities.")
    }

    override fun toString(): String {
        val b = StringBuilder()
        countMap.forEach { (key: EntityType, value: Int?) ->
            b.append(Chat.darkGray).append("- ").append(Chat.gray)
                .append(formatEntity(key.name)).append(": ")
                .append(Chat.reset).append(value).append("\n")
        }
        return b.toString()
    }

    fun sortMap() {
        val list: List<Map.Entry<EntityType, Int>> = LinkedList<Map.Entry<EntityType, Int>>(countMap.entries)
        list.stream().sorted { (_, value): Map.Entry<EntityType?, Int>, (_, value1): Map.Entry<EntityType?, Int> ->
            value1.compareTo(value)
        }
        val sorted: HashMap<EntityType, Int> = LinkedHashMap()
        list.forEach(Consumer { (key, value): Map.Entry<EntityType, Int> -> sorted[key] = value })
        countMap = sorted
    }

    // todo - do this better
    fun setMinimumResult(minimum: Int) {
        val toRemove = ArrayList<Any>()
        countMap.forEach { (aKey: EntityType, anIntValue: Int) ->
            if (anIntValue < minimum) {
                toRemove.add(aKey)
            }
        }
        toRemove.forEach(Consumer { entity: Any? -> countMap.remove(entity) })
    }
}