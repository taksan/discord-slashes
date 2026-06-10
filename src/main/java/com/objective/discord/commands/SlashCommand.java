package com.objective.discord.commands;

import com.objective.discord.engine.SlashContextOperator;

public interface SlashCommand {
    CommandReply execute(SlashContextOperator discordOperator);
}
