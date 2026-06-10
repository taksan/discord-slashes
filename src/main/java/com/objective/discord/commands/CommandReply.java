package com.objective.discord.commands;

import java.util.function.Consumer;

public interface CommandReply {
    void accept(Consumer<String> reply);
}
