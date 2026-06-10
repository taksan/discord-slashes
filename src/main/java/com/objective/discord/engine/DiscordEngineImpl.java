package com.objective.discord.engine;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordEngineImpl implements DiscordEngine {
    private final JDA jda;
    private final CommandListUpdateAction commands;
    private final List<CommandData> pendingCommands = new LinkedList<>();

    public DiscordEngineImpl(JDA jda) {
        this.jda = jda;
        this.commands = jda.updateCommands();
    }

    @Override
    public SlashContextOperator fromMessageEvent(@NotNull SlashCommandEvent event) {
        return new SlashContextOperatorImpl(event);
    }

    @Override
    public void registerSlashCommand(String name, String description) {
        pendingCommands.add(new CommandData(name, description));
    }

    @Override
    public void flushCommands() {
        final List<String> newCommands = pendingCommands.stream()
                .map(command -> command.getName()+"/"+ command.getDescription())
                .collect(Collectors.toList());

        jda.retrieveCommands().queue(existingCommands -> {
            existingCommands.forEach(oldCommand -> newCommands.remove(oldCommand.getName()+"/"+oldCommand.getDescription()));

            if (!newCommands.isEmpty())
                commands.addCommands(pendingCommands).queue();
        });
    }

    @Override
    public void addListener(EventListener listener) {
        jda.addEventListener(listener);
    }

}
