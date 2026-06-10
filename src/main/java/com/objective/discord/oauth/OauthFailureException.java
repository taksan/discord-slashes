package com.objective.discord.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;

@SuppressWarnings("serial")
public class OauthFailureException extends RuntimeException {

	public OauthFailureException(String message) {
		super(message);
	}

	public OauthFailureException(String message, JsonProcessingException cause) {
		super(message ,cause);
	}

}
