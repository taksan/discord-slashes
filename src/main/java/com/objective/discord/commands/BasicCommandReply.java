package com.objective.discord.commands;

import java.util.Objects;
import java.util.function.Consumer;

public class BasicCommandReply implements CommandReply {
    private final String replyMessage;
    public BasicCommandReply(String replyMessage) {
        this.replyMessage = replyMessage;
    }
    
    @Override
    public void accept(Consumer<String> reply) {
        reply.accept(replyMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().isAssignableFrom(o.getClass())) return false;
        BasicCommandReply that = (BasicCommandReply) o;
        return replyMessage.equals(that.replyMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyMessage);
    }

    @Override
    public String toString() {
        return "CommandReplyString{" +
                "replyMessage='" + replyMessage + '\'' +
                '}';
    }

	public static CommandReply basicReply(String reply) {
	    return new BasicCommandReply(reply);
	}
}
