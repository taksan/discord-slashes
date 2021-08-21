package com.objective.discord.engine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

class DiscordEngineImplTest {
    @Test
    void whenFromMessageEvent_CreatesASlashContextOperator() {
        final JDA jda = mock(JDA.class);
        final DiscordEngineImpl subject = new DiscordEngineImpl(jda);
        final SlashContextOperator actual = subject.fromMessageEvent(mock(SlashCommandEvent.class));

        Assertions.assertNotNull(actual);
    }


    @Test
    void registerSlashCommand() {
        final JDA jda = mock(JDA.class);
        final CommandCreateAction createAction = mock(CommandCreateAction.class);
        when(jda.upsertCommand("foo", "foo command")).thenReturn(createAction);
        final CommandListUpdateAction commandListUpdateAction = mock(CommandListUpdateAction.class);
        when(jda.updateCommands()).thenReturn(commandListUpdateAction);

        final DiscordEngineImpl subject = new DiscordEngineImpl(jda);
        subject.registerSlashCommand("foo", "foo command");
        subject.flushCommands();

        verify(commandListUpdateAction).queue();
    }

    @Test
    public void addListener() {
        final JDA jda = mock(JDA.class);
        final DiscordEngineImpl subject = new DiscordEngineImpl(jda);
        final EventListener addedListener = mock(EventListener.class);
        subject.addListener(addedListener);
        verify(jda).addEventListener(addedListener);
    }
}
