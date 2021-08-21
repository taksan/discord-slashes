package com.objective.discord.engine;

import net.dv8tion.jda.api.entities.Guild;

public interface SlashContextOperator {
    void reply(String s);
    boolean isFromGuild();
    GuildMember getCurrentMember();
    String getCurrentUserName();
	Guild getCurrentGuild();
}
