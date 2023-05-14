package me.eren.plugincloak.listeners;

import me.eren.plugincloak.PluginCloak;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreProcessListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("plugincloak.bypass")) return;

        String command = event.getMessage().replace("/", "");
        String group = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider().getPrimaryGroup(event.getPlayer());
        String message = PluginCloak.getInstance().getConfig().getString("unknown-message");

        if (!PluginCloak.getAvailableCommands(group).contains(command)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(message));
        }
    }
}
