package entityManager.commands;

import entityManager.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RemoveEntities extends SubCommand {

    public RemoveEntities(EntityManager plugin) {
        super(plugin, "remove");
    }

    @Override
    public void execute(@Nullable CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.red + "Sorry, that only works as a player.");
            return;
        }

        if (!player.hasPermission("entitymanager.remove")) {
            sender.sendMessage(EntityManager.NO_PERMISSION);
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: /entity remove <radius> <excluded>");
            return;
        }

        int radius = Utils.parseNumber(sender, args[1], "Usage: /entity remove <radius> <excluded>");
        if (radius == -1) return;

        int max = 200;
        if (radius > max || radius < 1) {
            plugin.msg(player, "You can only remove entities within a radius of " + max + ".");
            return;
        }

        List<Entity> nearby = player.getNearbyEntities(radius, 50, radius);

        EntityType toInclude = null;
        if (args.length  >= 3) {
            toInclude = EntityType.valueOf(args[2].toUpperCase()); //todo handle null
        }

        EntityType finalToInclude = toInclude;
        nearby.removeIf(entity -> plugin.getProtected().contains(entity.getType()) && entity.getType() != finalToInclude);

        if (nearby.size() == 0) {
            plugin.msg(player, "No entities found to remove.");
            return;
        }

        EntitySelection selected = new EntitySelection(player, nearby);

        String youAreAboutTo = selected.getTotal() < 2 ? "You are about to remove " + Chat.red + "1" + Chat.reset
                + " entity:" : "You are about to remove " + Chat.red + selected.getTotal() + Chat.reset + " entities.";

        sender.sendMessage(youAreAboutTo);
        sender.sendMessage(selected.toString());
        sender.sendMessage("Type" + Chat.red + " /em confirm" + Chat.reset + " to confirm this removal.");
    }

    @Override
    public String description() {
        return "Remove nearby entities";
    }

    @Override
    public String permission() {
        return "entitymanager.remove";
    }

}



















