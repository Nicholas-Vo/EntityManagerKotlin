package entityManager

import entityManager.commands.*
import entityManager.commands.teleportCommands.EntityTeleport
import entityManager.commands.teleportCommands.EntityTeleportListClear
import entityManager.commands.teleportCommands.WandToggleCommand
import entityManager.teleporter.EntityTeleportMap
import entityManager.teleporter.Listeners
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EntityManager : JavaPlugin() {
    var commands = ArrayList<SubCommand>()
    val entityListMap = EntityTeleportMap()
    val selectionMap = SelectionMap()
    private val wandMap: MutableMap<UUID, Boolean> = HashMap()
    fun getWandMap(): Map<UUID, Boolean> {
        return wandMap
    }

    companion object {
        const val VERSION = "1.5"

        lateinit var instance: EntityManager

        @JvmField
        val NO_PERMISSION = Chat.red + "You do not have permission to do that."
        val entityTypes: List<String>
            get() {
                val results = ArrayList<String>()
                for (m in EntityType.values()) results.add(m.name)
                return results
            }
    }

    override fun onEnable() {
        instance = this
        Bukkit.getLogger().info("Enabling EntityManager v$VERSION...")
        commands.add(LagReport(this))
        commands.add(NearbyEntites(this))
        commands.add(RemoveEntities(this))
        commands.add(SearchEntity(this))
        commands.add(WandToggleCommand(this))
        commands.add(EntityTeleportListClear(this))
        commands.add(EntityTeleport(this))
        commands.add(ConfirmCommand(this))
        commands.add(UndoRemovalCommand(this))
        getPlugin(EntityManager::class.java).saveDefaultConfig()
        Listeners(this)
    }

    override fun onDisable() {
        Bukkit.getLogger().info("Disabling EntityManager v$VERSION...")
    }

    fun wandEnabled(p: Player): Boolean {
        wandMap.putIfAbsent(p.uniqueId, false)
        return wandMap[p.uniqueId]!!
    }

    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("entitymanager.use")) {
            sender.sendMessage(Chat.PLUGIN_TAG + "version " + VERSION)
            return false
        }
        if (args.isEmpty()) {
            sender.sendMessage(
                Chat.gray + " --------" + Chat.gold + " Entity Manager " + Chat.gray + "--------"
            )
            for (command in commands) sender.sendMessage(
                Chat.red + "/" + command.name + Chat.reset + " - " + command.description()
            )
            return true
        }
        for (command in commands) {
            if (command.name == args[0]) {
                command.execute(sender, args)
                break
            }
        }
        return false
    }

    fun msg(aSender: CommandSender, aMessage: String) {
        aSender.sendMessage(Chat.PLUGIN_TAG + aMessage)
    }

    val protected: List<EntityType>
        get() {
            val theList = ArrayList<EntityType>()
            config.getStringList("protected").forEach(Consumer { name: String? ->
                theList.add(
                    EntityType.valueOf(
                        name!!
                    )
                )
            })
            return theList
        }

    private fun getResults(args: Array<String>, toSearch: List<String>): List<String> {
        val results: MutableList<String> = ArrayList()
        for (s in toSearch) {
            if (s.lowercase(Locale.getDefault()).startsWith(args[1].lowercase(Locale.getDefault())))
                results.add(s)
        }
        return results
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (!commandSender.hasPermission("entitymanager.use")) return java.util.List.of()
        val results: MutableList<String> = ArrayList()
        val aList: MutableList<String> = ArrayList()
        commands.forEach(Consumer { cmd: SubCommand -> aList.add(cmd.name) })
        if (args.size == 1) {
            for (a in aList) {
                if (a.lowercase(Locale.getDefault()).startsWith(args[0].lowercase(Locale.getDefault())))
                    results.add(a)
            }
            return results
        }
        if (args.size == 2) {
            if (args[0].equals("search", ignoreCase = true)) {
                return getResults(args, entityTypes)
            }
        }
        if (args.size == 3) {
            if (args[0].equals("remove", ignoreCase = true)) {
                return getResults(args, config.getStringList("protected"))
            }
        }
        return listOf()
    }
}