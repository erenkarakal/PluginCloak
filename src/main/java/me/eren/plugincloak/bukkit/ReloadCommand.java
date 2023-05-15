package me.eren.plugincloak.bukkit;

import me.eren.plugincloak.bukkit.PluginCloak;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            Plugin pluginCloak = Bukkit.getPluginManager().getPlugin("PluginCloak");
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow><bold>PluginGuard </bold>" + pluginCloak.getDescription().getVersion()));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("by: <yellow>" + String.join("<white>, <yellow>", pluginCloak.getDescription().getAuthors())));
        }

        else if (args[0].equals("reload")) {
            PluginCloak.getInstance().reloadConfig();
            PluginCloak.loadConfig();
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Reloaded!"));
        }

        return true;
    }
}
