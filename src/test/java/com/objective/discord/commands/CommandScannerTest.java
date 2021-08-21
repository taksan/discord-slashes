package com.objective.discord.commands;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.objective.discord.engine.SlashContextOperator;

public class CommandScannerTest {
    @Test
    public void scanAndAddCommandsToDiscord() {
        final CommandScanner subject = new CommandScanner(CommandScannerTest.class.getPackageName());
        final List<Class<? extends SlashCommand>> commandClasses = new LinkedList<>();

        subject.forEachCommand(commandClasses::add);

        assertEquals(
            TestCommand.class + "," + AnotherTestCommand.class,
            commandClasses.stream()
                    .map(Object::toString)
                    .filter(name -> name.contains("CommandScannerTest"))
                    .collect(Collectors.joining(","))
        );
    }

    @Command(name = "test", description = "This a  test command")
    public static class TestCommand implements SlashCommand {
        @Override
        public CommandReply execute(SlashContextOperator operator) {
            return BasicCommandReply.basicReply("Done");
        }
    }

    @Command(name = "anotherTest",  description = "This a another test command")
    public static class AnotherTestCommand implements SlashCommand {
        @Override
        public CommandReply execute(SlashContextOperator operator) {
            return BasicCommandReply.basicReply("Done");
        }
    }
}
