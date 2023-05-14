package me.eren.plugincloak;

import me.eren.plugincloak.listeners.CommandPreProcessListener;
import me.eren.plugincloak.listeners.PlayerCommandSendListener;
import me.eren.plugincloak.listeners.TabCompleteListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class PluginCloak extends JavaPlugin {
    private static PluginCloak instance;
    private static List<String> plugins = new ArrayList<String>();
    private static Map<String, List<String>> showcommands = new HashMap<String, List<String>>();


    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabled PluginCloak v" + this.getDescription().getVersion());

        this.getCommand("plugincloak").setExecutor(new CommandReload());

        TabCompleteListener.registerListener();
        getServer().getPluginManager().registerEvents(new PlayerCommandSendListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreProcessListener(), this);

        this.saveDefaultConfig();
        loadConfig();

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            PluginCloak.plugins.add(plugin.getName());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled PluginCloak v" + this.getDescription().getVersion());
    }

    public static PluginCloak getInstance() {
        return instance;
    }

    public static void loadConfig() {
        PluginCloak.showcommands.clear();
        for (String group : instance.getConfig().getConfigurationSection("show-commands").getKeys(false)) {
            List<String> commands = instance.getConfig().getStringList("show-commands." + group + ".commands");

            for (String inherits : instance.getConfig().getStringList("show-commands." + group + ".inherits-from")) {
                commands.addAll(instance.getConfig().getStringList("show-commands." + inherits + ".commands"));
            }
            PluginCloak.showcommands.put(group, commands);
        }
    }

    public static List<String> getAvailableCommands(String group) {
        return PluginCloak.showcommands.get(group);
    }

    public static Collection<String> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }
}

