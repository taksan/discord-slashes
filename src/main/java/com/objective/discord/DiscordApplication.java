package com.objective.discord;

import com.objective.discord.commands.SlashCommandDispatcher;
import com.objective.discord.engine.DiscordEngineImpl;
import com.objective.discord.engine.GuildMember;
import com.objective.discord.engine.GuildMemberImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import java.util.function.Consumer;

public class DiscordApplication {
    private final JDA jda;
    private final String basePackageForCommands;

    public static DiscordApplication create(String botToken, String basePackageForCommands) throws LoginException {
        final JDA jda = JDABuilder
                .createDefault(botToken)
                .build();
        return new DiscordApplication(jda, basePackageForCommands);
    }
    public DiscordApplication(JDA jda, String basePackageForCommands) {
        this.jda = jda;
        this.basePackageForCommands = basePackageForCommands;
    }

    public void performOnGuildMemberNoCache(long userId, Consumer<GuildMember> action) {
        jda.retrieveUserById(userId, false).queue(
            user -> applyActionToUserInMutualGuilds(action, user)
        );
    }

    private void applyActionToUserInMutualGuilds(Consumer<GuildMember> action, User user) {
        user.getMutualGuilds().forEach(guild -> applyActionToGuildMember(action, user, guild));
    }

    private void applyActionToGuildMember(Consumer<GuildMember> action, User user, Guild guild) {
        guild.retrieveMember(user).queue(member -> action.accept(new GuildMemberImpl(member)));
    }

    public void start() throws InterruptedException {
        new SlashCommandDispatcher(new DiscordEngineImpl(jda)).initialize(basePackageForCommands);
        jda.awaitReady();
    }
}
