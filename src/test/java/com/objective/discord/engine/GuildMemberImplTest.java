package com.objective.discord.engine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

class GuildMemberImplTest {
    final Member member = mock(Member.class);
    final GuildMemberImpl subject = new GuildMemberImpl(member);

    @Test
    void whenIsOwner_ShouldDelegate() {
        subject.isOwner();
        verify(member).isOwner();
    }

    @Test
    void whenGetNickname_ShouldDelegate() {
        subject.getNickname();
        verify(member).getNickname();
    }

    @Test
    void whenReplaceNickname_ShouldDelegate() {
        @SuppressWarnings("unchecked") AuditableRestAction<Void> action = mock(AuditableRestAction.class);
        when(member.modifyNickname("foo")).thenReturn(action);
        subject.replaceNickname("foo");
        verify(action).complete();
    }
}
