package com.objective.discord.engine;

import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;

public class GuildMemberImpl implements GuildMember{
    private final Member member;

    public GuildMemberImpl(Member member) {
        this.member = member;
    }

    @Override
    public boolean isOwner() {
        return this.member.isOwner();
    }

    @Override
    public Optional<String> getNickname() {
        return Optional.ofNullable(member.getNickname());
    }

    @Override
    public void replaceNickname(String nickname) {
        member.modifyNickname(nickname).complete();
    }

    @Override
    public String getEffectiveName() {
        return member.getEffectiveName();
    }
}
