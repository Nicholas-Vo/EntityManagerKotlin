package entityManager.commands

import entityManager.EntityManager
import entityManager.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UndoRemovalCommand(plugin: EntityManager?) : SubCommand(plugin!!, "undo") {
    override fun description(): String? {
        return "undo a removal"
    }

    override fun permission(): String? {
        return "entitymanager.undo"
    }

    override fun execute(sender: CommandSender?, args: Array<String>) {
        if (sender !is Player) {
            EntityManager.instance.msg(sender!!, "That's a player only command.")
            return
        }
        val selection = plugin.selectionMap.getPlayerSelection(sender)
        if (selection == null) {
            plugin.msg(sender, "You don't have anything to undo.")
            return
        }
        plugin.selectionMap.getPlayerSelection(sender)?.undoRemoval()
    }
}