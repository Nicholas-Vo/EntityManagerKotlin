package entityManager

import org.apache.commons.lang.StringUtils
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import java.util.*

object Utils {
    @JvmStatic
    fun locationToString(l: Location): String {
        return "x" + l.blockX + ", y" + l.blockY + ", z" + l.blockZ
    }

    @JvmStatic
    fun formatEntity(name: String): String {
        return StringUtils.capitalize(name.lowercase(Locale.getDefault()).replace("_", " "))
    }

    @JvmStatic
    fun worldToString(w: World): String {
        val name = w.name
        if (name == "world") return "overworld"
        if (name == "world_nether") return "nether"
        return if (name == "world_the_end") "end" else "null"
    }

    @JvmStatic
    fun parseNumber(sender: CommandSender?, number: String, error: String?): Int {
        var result = -1
        try {
            result = number.toInt()
        } catch (e: NumberFormatException) {
            EntityManager.instance.msg(sender!!, error!!)
        }
        return result
    }

    fun count(e: EntityType?, map: MutableMap<EntityType?, Int>) {
        map.putIfAbsent(e, 0)
        map[e] = map[e]!! + 1
    }
}