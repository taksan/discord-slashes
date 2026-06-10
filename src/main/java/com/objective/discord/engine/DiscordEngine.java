package com.objective.discord.engine;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public interface DiscordEngine {
    SlashContextOperator fromMessageEvent(@NotNull SlashCommandEvent event);
    void registerSlashCommand(String name, String description);
    void flushCommands();
    void addListener(EventListener listener);
}
