package entityManager.commands.teleportCommands;

import entityManager.Chat;
import entityManager.EntityManager;
import entityManager.SubCommand;
import entityManager.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class WandToggleCommand extends SubCommand implements Listener {
    private final Map<UUID, Boolean> map;

    public WandToggleCommand(EntityManager p) {
        super(p, "togglewand");
        map = p.getWandMap();
    }

    @Override
    public void execute(@Nullable CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.red + "That's a player only command.");
            return;
        }

        if (!player.hasPermission("entitymanager.teleport")) {
            sender.sendMessage(EntityManager.NO_PERMISSION);
            return;
        }

        UUID pUUID = player.getUniqueId();

        map.putIfAbsent(pUUID, false);

        if (map.get(pUUID)) {
            map.put(pUUID, false);
            plugin.msg(player, "Toggled wand usage " + Chat.red + "OFF" + Chat.reset + ".");
            return;
        }

        map.put(pUUID, true);
        plugin.msg(player, "Toggled wand usage " + Chat.green + "ON" + Chat.reset + ".");
    }

    @Override
    public String description() {
        return "Toggle the wand";
    }

    @Override
    public String permission() {
        return "entitymanager.teleport";
    }
}
