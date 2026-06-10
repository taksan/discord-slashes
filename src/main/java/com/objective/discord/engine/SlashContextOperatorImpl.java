package com.objective.discord.engine;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SlashContextOperatorImpl implements SlashContextOperator {
    private final SlashCommandEvent event;

    public SlashContextOperatorImpl(SlashCommandEvent event) {
        this.event = event;
    }

    @Override
    public void reply(String replyMessage) {
        event.reply(replyMessage).setEphemeral(true).queue();
    }

    @Override
    public GuildMember getCurrentMember() {
        final Member member = event.getMember();
        if (member == null)
            return new NoGuildMember();
        return new GuildMemberImpl(member);
    }

    @Override
    public String getCurrentUserName() {
        return event.getUser().getName();
    }

    @Override
    public boolean isFromGuild() {
        return event.isFromGuild();
    }
    
    @Override
    public Guild getCurrentGuild() {
    	return event.getGuild();
    }
}
