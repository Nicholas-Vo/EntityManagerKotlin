package entityManager

import org.bukkit.ChatColor

interface Chat {
    companion object {
        val red = ChatColor.RED.toString() + ""
        val gold = ChatColor.GOLD.toString() + ""
        val gray = ChatColor.GRAY.toString() + ""
        val darkGray = ChatColor.DARK_GRAY.toString() + ""
        val reset = ChatColor.RESET.toString() + ""
        val PLUGIN_TAG = "$red[EntityManager] $reset"
    }
}