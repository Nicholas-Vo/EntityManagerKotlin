package entityManager.commands;

import entityManager.Chat;
import entityManager.EntityManager;
import entityManager.SubCommand;
import entityManager.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

public class SearchEntity extends SubCommand {

    public SearchEntity(EntityManager entityManager) {
        super(entityManager, "search");
    }

    @Override
    public void execute(@Nullable CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Player only command.");
            return;
        }

        List<Player> playersInWorld = p.getWorld().getPlayers();

        if (args.length < 2) {
            sender.sendMessage("Usage: /entity search <entity>");
            return;
        }

        EntityType toSearch = EntityType.valueOf(args[1].toUpperCase());

        var entityList = new HashMap<Player, Integer>();
        var locMap = new HashMap<Player, Location>();

        for (Player player : playersInWorld) {
            List<Entity> nearby = player.getNearbyEntities(200, 150, 200);
            for (Entity e : nearby) {
                if (e.getType() == toSearch) {
                    count(player, entityList);
                    locMap.put(player, e.getLocation());
                }
            }
        }

        entityList = sort(entityList);
        String lookedUp = WordUtils.capitalize(toSearch.toString().toLowerCase().replace("_", " "));
        if (entityList.size() == 0) {
            sender.sendMessage("No entities of that type were found.");
        } else {
            sender.sendMessage("Players with the most " + lookedUp + "s:");
        }

        int limit = 0;
        for (Entry<Player, Integer> entry : entityList.entrySet()) {
            if (limit < 12) {
                sender.sendMessage(Chat.darkGray + "- " + Chat.gray + entry.getKey().getName() + ": " + Chat.red + entry.getValue()
                        + " " + Chat.reset + Utils.locationToString(locMap.get(entry.getKey())));
            }
            limit++;
        }
    }

    private HashMap<Player, Integer> sort(HashMap<Player, Integer> map) {
        List<Map.Entry<Player, Integer>> list = new LinkedList<>(map.entrySet());

        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        HashMap<Player, Integer> sorted = new LinkedHashMap<>();
        for (Map.Entry<Player, Integer> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }

    private void count(Player p, Map<Player, Integer> entityList) {
        entityList.merge(p, 1, Integer::sum);
    }

    @Override
    public String description() {
        return "Lookup top entity count per player for a specific entity";
    }

    @Override
    public String permission() {
        return "entitymanager.searchentity";
    }

}
