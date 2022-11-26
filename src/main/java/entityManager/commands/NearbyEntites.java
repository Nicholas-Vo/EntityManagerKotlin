package entityManager.commands;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entityManager.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NearbyEntites extends SubCommand {
	private int max = 200;

	public NearbyEntites(EntityManager entityManager) {
		super(entityManager, "near");
	}

	@Override
	public void execute(@Nullable CommandSender sender, @NotNull String[] args) {
		if (!(sender instanceof Player player)) { return; }

		int radius = 10;

		if (args.length == 2) radius = Utils.parseNumber(sender, args[1], "Usage: /em near <radius>");
		if (radius < 1) return;

		if (radius > max) {
			sender.sendMessage("You can only search for entities within a radius of " + max + ".");
			return;
		}

		List<Entity> nearby = player.getNearbyEntities(radius, 100, radius);

		EntitySelection selection = new EntitySelection(player, nearby);

		selection.sortMap();

		String msg = selection.getTotal() == 1 ? "There is" + Chat.red + " 1 " + Chat.reset + "entity"
				: "There are " + Chat.red + selection.getTotal() + Chat.reset + " entities";

		sender.sendMessage(msg + Chat.reset + " nearby within a radius of " + radius + ".");
		sender.sendMessage(selection.toString());
	}
	
	@Override
	public String description() {
		return "Display nearby entities";
	}

	@Override
	public String permission() {
		return "entitymanager.nearby";
	}

}
