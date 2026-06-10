package com.objective.discord.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class SlashContextOperatorImplTest {
    @Test
    public void whenChangingNickName_ShouldChangeCurrentUserNickname() {
        final Member member = mock(Member.class);
        final SlashCommandEvent event = mock(SlashCommandEvent.class);
        when(event.getMember()).thenReturn(member);
        @SuppressWarnings("unchecked")
        AuditableRestAction<Void> action = mock(AuditableRestAction.class);
        when(member.modifyNickname("Arthur")).thenReturn(action);

        final SlashContextOperatorImpl subject = new SlashContextOperatorImpl(event);
        subject.getCurrentMember().replaceNickname("Arthur");

        verify(action).complete();
    }

    @Test
    public void whenGettingNickName_ShouldGetCurrentUserNickname() {
        final SlashCommandEvent event = mock(SlashCommandEvent.class);
        when(event.getGuild()).thenReturn(mock(Guild.class));

        final Member member = mock(Member.class);
        when(member.getNickname()).thenReturn("Arthur Dent");
        when(event.getMember()).thenReturn(member);

        final SlashContextOperatorImpl subject = new SlashContextOperatorImpl(event);
        assertEquals(Optional.of("Arthur Dent"), subject.getCurrentMember().getNickname());
    }

    @Test
    public void whenGettingNickNameOutsideOfGuild_ShouldGetEmptyOptional() {
        final SlashCommandEvent event = mock(SlashCommandEvent.class);
        final Member member = mock(Member.class);
        when(member.getGuild()).thenReturn(null);

        final SlashContextOperatorImpl subject = new SlashContextOperatorImpl(event);
        assertEquals(Optional.empty(), subject.getCurrentMember().getNickname());
    }


    @Test
    public void whenGettingUserId_ShouldGetCurrentUserName() {
        final SlashCommandEvent event = mock(SlashCommandEvent.class);
        when(event.getGuild()).thenReturn(mock(Guild.class));

        final User user = mock(User.class);
        when(user.getName()).thenReturn("ArthurDent");
        when(event.getUser()).thenReturn(user);

        final SlashContextOperatorImpl subject = new SlashContextOperatorImpl(event);
        assertEquals("ArthurDent", subject.getCurrentUserName());
    }

    @Test
    public void whenReply_ShouldReplyAndQueueMessage() {
        final SlashCommandEvent event = mock(SlashCommandEvent.class);
        final ReplyAction replyAction = mock(ReplyAction.class);
        when(event.reply("message")).thenReturn(replyAction);
        when(replyAction.setEphemeral(true)).thenReturn(replyAction);

        final SlashContextOperatorImpl subject = new SlashContextOperatorImpl(event);
        subject.reply("message");
        verify(replyAction).queue();
    }

    @Test
    public void whenIsFromGuild_ShouldReplyAndQueueMessage() {
        final SlashCommandEvent event = mock(SlashCommandEvent.class);
        when(event.isFromGuild()).thenReturn(true);

        final SlashContextOperatorImpl subject = new SlashContextOperatorImpl(event);
        subject.isFromGuild();
        verify(event).isFromGuild();
    }
}
