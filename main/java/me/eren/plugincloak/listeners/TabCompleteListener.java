package me.eren.plugincloak.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import me.eren.plugincloak.PluginCloak;
import me.eren.plugincloak.events.CheatDetectEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener {
    public static void registerListener() {

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(PluginCloak.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE) {

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPlayer().hasPermission("plugincloak.bypass")) return;

                        Suggestions suggestions = event.getPacket().getSpecificModifier(Suggestions.class).read(0);

                        // Check 1
                        boolean hacking = suggestions.getRange().getEnd() == 1;

                        // Check 2
                        if (!hacking) {
                            for(Suggestion suggestion : event.getPacket().getSpecificModifier(Suggestions.class).read(0).getList()) {
                                if (!PluginCloak.getPlugins().contains(suggestion.getText())) {
                                    return;
                                }
                                hacking = true;
                            }
                        }
                        if (hacking) {
                            CheatDetectEvent cheatDetectEvent = new CheatDetectEvent(event.getPlayer());
                            Bukkit.getPluginManager().callEvent(cheatDetectEvent);

                            event.setCancelled(true);

                            String method = PluginCloak.getInstance().getConfig().getString("anticheat.method", "");
                            switch (method) {
                                case "fakelist":
                                    event.setCancelled(false);

                                    List<Suggestion> suggestionsList = new ArrayList<>();
                                    for (String fakePluginName: PluginCloak.getInstance().getConfig().getStringList("anticheat.fakelist")) {
                                        suggestionsList.add(new Suggestion(new StringRange(1, 1), ChatColor.translateAlternateColorCodes('&', fakePluginName) + ":" + fakePluginName));
                                    }

                                    event.getPacket().getSpecificModifier(Suggestions.class).write(0, new Suggestions(new StringRange(1, 1), suggestionsList));
                                    break;

                                case "crash":
                                    Location playerLoc = event.getPlayer().getLocation();
                                    PacketContainer fakeExplosion = new PacketContainer(PacketType.Play.Server.EXPLOSION);
                                    fakeExplosion.getDoubles()
                                            .write(0, playerLoc.getX())
                                            .write(1, playerLoc.getY())
                                            .write(2, playerLoc.getZ());
                                    fakeExplosion.getFloat()
                                            .write(0, 1.0F) // strength
                                            .write(1, 340282346638528860000000000000000000000.0F) // motion X
                                            .write(2, 340282346638528860000000000000000000000.0F) // motion y
                                            .write(3, 340282346638528860000000000000000000000.0F); // motion z
                                    try {
                                        ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), fakeExplosion);
                                    } catch (InvocationTargetException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;

                                case "console":
                                    String command = PluginCloak.getInstance().getConfig().getString("anticheat.command", "");
                                    command = command.replaceAll("\\{player}", event.getPlayer().getName());
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                            }
                        }
                    }
                });
    }
}