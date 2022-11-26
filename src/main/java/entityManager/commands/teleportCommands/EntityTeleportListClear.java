package entityManager.commands.teleportCommands;

import entityManager.Chat;
import entityManager.EntityManager;
import entityManager.SubCommand;
import entityManager.teleporter.EntityList;
import entityManager.teleporter.EntityTeleportMap;
import entityManager.teleporter.TeleportUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityTeleportListClear extends SubCommand implements Listener {
    private final EntityTeleportMap theMap;

    public EntityTeleportListClear(EntityManager p) {
        super(p, "clearteleportlist");
        theMap = p.getEntityListMap();
    }

    @Override
    public void execute(@Nullable CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.red + "That's a player only command.");
            return;
        }

        EntityList list = theMap.getPlayerList(player);

        if (list.isEmpty()) {
            sender.sendMessage(Chat.red + "Your entity teleport list is already empty.");
            return;
        }

        list.clear();
        sender.sendMessage(Chat.red + "Entity teleport list cleared.");
    }

    @Override
    public String description() {
        return "Clear the teleport entity list";
    }

    @Override
    public String permission() {
        return "entitymanager.teleport";
    }
}
