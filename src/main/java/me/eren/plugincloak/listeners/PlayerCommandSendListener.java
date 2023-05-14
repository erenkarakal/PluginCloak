package me.eren.plugincloak.listeners;

import me.eren.plugincloak.PluginCloak;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerCommandSendListener implements Listener {
    @EventHandler
    public void onPlayerCommandSendEvent(PlayerCommandSendEvent event) {
        if (event.getPlayer().hasPermission("plugincloak.bypass")) return;

        String group = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider().getPrimaryGroup(event.getPlayer());
        event.getCommands().clear();
        event.getCommands().addAll(PluginCloak.getAvailableCommands(group));
    }
}
