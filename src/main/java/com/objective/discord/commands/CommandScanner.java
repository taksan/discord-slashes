package com.objective.discord.commands;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandScanner {
    private final List<Class<? extends SlashCommand>> commands;

    public CommandScanner(String commandPackages) {
        this.commands = this.scan(commandPackages);
    }

    public void forEachCommand(Consumer<Class<? extends SlashCommand>> classConsumer) {
        this.commands.forEach(classConsumer);
    }

    private List<Class<? extends SlashCommand>> scan(String commandPackages) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(commandPackages))
            .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner())
            .useParallelExecutor()
        );

        final Set<Class<?>> returnedTypes = reflections.getTypesAnnotatedWith(Command.class);
        return returnedTypes.stream()
                .filter(SlashCommand.class::isAssignableFrom)
                .map(this::forceToInterpretAsCommand)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Class<? extends SlashCommand> forceToInterpretAsCommand(Class<?> clazz) {
        return (Class<? extends SlashCommand>)clazz;
    }
}
