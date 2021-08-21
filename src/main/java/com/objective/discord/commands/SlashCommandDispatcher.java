package com.objective.discord.commands;

import com.objective.discord.engine.DiscordEngine;
import com.objective.discord.engine.SlashContextOperator;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SlashCommandDispatcher extends ListenerAdapter {
    private final Map<String, Class<? extends SlashCommand>> commandsByName = new HashMap<>();
    private final DiscordEngine discordOperator;

    public SlashCommandDispatcher(DiscordEngine discordOperator) {
        this.discordOperator = discordOperator;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        process(discordOperator.fromMessageEvent(event), event.getName());
    }

    public void register(Class<? extends SlashCommand> testCommand) {
        final Command commandAnnotation = testCommand.getAnnotation(Command.class);
        final String command = commandAnnotation.name();
        final String description  = commandAnnotation.description();
        discordOperator.registerSlashCommand(command, description);
        commandsByName.put(command, testCommand);
    }

    public void process(SlashContextOperator operator, String textCommand) {
        getCommand(textCommand).ifPresent(cmd -> {
            if (cmd.getClass().getAnnotation(Command.class).requiresGuild() && !operator.isFromGuild())
                operator.reply("You must run the command in a guild channel");
            else
                cmd.execute(operator).accept(operator::reply);
        });
    }

    private Optional<? extends SlashCommand> getCommand(String command) {
        return Optional.ofNullable(commandsByName.get(command)).map(this::createInstance);
    }

    private <T extends SlashCommand> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not instantiate " + clazz, e);
        }
    }

    public void initialize(String basePackageForCommands) {
        final CommandScanner commandScanner = new CommandScanner(basePackageForCommands);
        commandScanner.forEachCommand(this::register);
        discordOperator.flushCommands();
        discordOperator.addListener(this);
    }
}
