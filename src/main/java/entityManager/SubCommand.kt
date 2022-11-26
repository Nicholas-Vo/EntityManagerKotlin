package entityManager

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

abstract class SubCommand(protected var plugin: EntityManager, val name: String) : TabCompleter {

    abstract fun description(): String?
    abstract fun permission(): String?
    abstract fun execute(sender: CommandSender?, args: Array<String>)
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return emptyList()
    }
}