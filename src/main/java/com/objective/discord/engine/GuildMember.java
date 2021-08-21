package com.objective.discord.engine;

import java.util.Optional;

public interface GuildMember {
    boolean isOwner();
    Optional<String> getNickname();
    void replaceNickname(String nickname);
    String getEffectiveName();

}
