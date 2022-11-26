package entityManager.commands.teleportCommands;

import entityManager.Chat;
import entityManager.EntityManager;
import entityManager.SubCommand;
import entityManager.teleporter.EntityTeleportMap;
import entityManager.teleporter.TeleportUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityTeleport extends SubCommand implements Listener {
    private final EntityTeleportMap theMap;

    public EntityTeleport(EntityManager p) {
        super(p, "teleport");
        theMap = p.getEntityListMap();
    }

    @Override
    public void execute(@Nullable CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.red + "That's a player only command.");
            return;
        }

        if (theMap.getPlayerList(player).isEmpty()) {
            sender.sendMessage(Chat.red + "Your entity teleport list is empty. " + Chat.reset
                    + "Use an arrow to select new entities to teleport.");
            return;
        }

        TeleportUtils.teleport(player, player.getLocation(), theMap);
    }

    @Override
    public String description() {
        return "Teleport an entity";
    }

    @Override
    public String permission() {
        return "entitymanager.teleport";
    }
}
