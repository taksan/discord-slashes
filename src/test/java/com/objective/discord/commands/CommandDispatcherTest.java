package com.objective.discord.commands;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import com.objective.discord.engine.DiscordEngine;
import com.objective.discord.engine.SlashContextOperator;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class CommandDispatcherTest {
    final SlashContextOperator operator = mock(SlashContextOperator.class);
    final List<Pair<String, String>> registeredCommands = new LinkedList<>();
    final DiscordEngine discordOperator = new DiscordEngine() {
        @Override
        public SlashContextOperator fromMessageEvent(@NotNull SlashCommandEvent event) {
            return operator;
        }

        @Override
        public void registerSlashCommand(String name, String description) {
            registeredCommands.add(Pair.of(name, description));
        }

        @Override
        public void flushCommands() {
        }

        @Override
        public void addListener(EventListener listener) {
        }
    };

    @Test
    public void processTextCommand_ExecutesTheCommandClass() {
        final SlashCommandDispatcher dispatcher = new SlashCommandDispatcher(discordOperator);
        dispatcher.register(TestCommand.class);
        dispatcher.register(AnotherTestCommand.class);

        final SlashCommandEvent messageEvent = mock(SlashCommandEvent.class);
        when(messageEvent.getName()).thenReturn("test");
        dispatcher.onSlashCommand(messageEvent);

        verify(operator).reply("test-result");

        assertEquals(
            "(test,This is a test command),(anotherTest,This is another test command)",
            registeredCommands.stream().map(Pair::toString).collect(joining(","))
        );
    }

    @Test
    public void whenCommandRequiresGuild_ShouldValidateBefore() {
        final SlashCommandDispatcher dispatcher = new SlashCommandDispatcher(discordOperator);
        dispatcher.register(ACommandThatRequiresAGuild.class);

        final SlashCommandEvent messageEvent = mock(SlashCommandEvent.class);
        when(messageEvent.isFromGuild()).thenReturn(false);
        when(messageEvent.getName()).thenReturn("aCommandThatRequiresAGuild");
        dispatcher.onSlashCommand(messageEvent);

        verify(operator).reply("You must run the command in a guild channel");
    }

    @Command(name = "test", description = "This is a test command")
    public static class TestCommand implements SlashCommand {
        @Override
        public CommandReply execute(SlashContextOperator operator) {
            return BasicCommandReply.basicReply("test-result");
        }
    }

    @Command(name = "anotherTest", description = "This is another test command")
    public static class AnotherTestCommand implements SlashCommand {
        @Override
        public CommandReply execute(SlashContextOperator operator) {
            return BasicCommandReply.basicReply("anotherTest-result");
        }
    }

    @Command(name = "aCommandThatRequiresAGuild",
            description = "A command that requires a guild",
            requiresGuild = true)
    public static class ACommandThatRequiresAGuild implements SlashCommand {
        @Override
        public CommandReply execute(SlashContextOperator operator) {
            return BasicCommandReply.basicReply("this command required a guild");
        }
    }
}
