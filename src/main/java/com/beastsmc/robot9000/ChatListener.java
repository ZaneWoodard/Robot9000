package com.beastsmc.robot9000;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Robot9000 plugin;
    public ChatListener(Robot9000 plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        //TODO
    }
}
