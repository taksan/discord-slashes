package com.objective.discord.commands;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();
    String description();

    boolean requiresGuild() default false;
}
