package com.objective.discord.oauth;

@SuppressWarnings("unused")
public class DiscordClientConfiguration {
	public String clientId;
	public String clientSecret;
	public String botToken;

	public DiscordClientConfiguration(){}

	public DiscordClientConfiguration(String clientId, String clientSecret, String botToken) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.botToken = botToken;
	}
}
