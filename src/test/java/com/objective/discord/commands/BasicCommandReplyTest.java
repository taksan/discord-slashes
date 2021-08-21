package com.objective.discord.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BasicCommandReplyTest {
	@Test
	public void whenAccept_ShouldInvokeLambdaWithMessage() {
		BasicCommandReply subject = new BasicCommandReply("a message");
		subject.accept(txt -> assertEquals("a message", txt));
	}
	
	@Test
	public void whenEquals_ShouldConsiderInstancesWithSameTextEquals() {
		BasicCommandReply subject = new BasicCommandReply("a message");
		BasicCommandReply another = new BasicCommandReply("a message");
		
		assertEquals(subject, another);
		
		assertEquals(subject.hashCode(), another.hashCode());
	}
}