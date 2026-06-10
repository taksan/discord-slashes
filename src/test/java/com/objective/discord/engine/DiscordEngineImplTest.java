package com.objective.discord.engine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.RestAction;

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
        when(commandListUpdateAction.addCommands(any(Collection.class))).thenReturn(commandListUpdateAction);

        @SuppressWarnings("unchecked")
        final RestAction<List<Command>> retrieveAction = mock(RestAction.class);
        when(jda.retrieveCommands()).thenReturn(retrieveAction);
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            final Consumer<List<Command>> callback = invocation.getArgument(0);
            callback.accept(Collections.emptyList());
            return null;
        }).when(retrieveAction).queue(any(Consumer.class));

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
