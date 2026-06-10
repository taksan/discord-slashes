package com.objective.discord.engine;

import java.util.Optional;

public class NoGuildMember implements GuildMember{
    @Override
    public boolean isOwner() {
        return false;
    }

    @Override
    public Optional<String> getNickname() {
        return Optional.empty();
    }

    @Override
    public void replaceNickname(String nickname) {
    }

    @Override
    public String getEffectiveName() {
        return "Not a Guild Member";
    }
}
