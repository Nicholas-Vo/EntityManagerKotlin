package entityManager.commands;

import entityManager.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

public class LagReport extends SubCommand {

    public LagReport(EntityManager plugin) {
        super(plugin, "report");
    }

    @Override
    public void execute(@Nullable CommandSender sender, @NotNull String[] args) {

		if (!(sender instanceof Player player)) {
			sender.sendMessage(Chat.red + "Sorry, that only works as a player.");
			return;
		}

        List<Entity> world = player.getWorld().getEntities();

		EntitySelection selection = new EntitySelection(player, world);
        selection.sortMap();
        selection.setMinimumResult(10);

        sender.sendMessage("Entity report for the " + Utils.worldToString(player.getWorld()) + ":");
        sender.sendMessage(selection.toString());
    }

    @Override
    public String description() {
        return "Display top entities for the world you're in";
    }

    @Override
    public String permission() {
        return "entitymanager.lagreport";
    }

}
