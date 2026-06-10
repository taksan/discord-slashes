package com.objective.discord.oauth;

public class DiscordAuthenticationService {
	private final DiscordClientConfiguration configuration;
	
	public DiscordAuthenticationService(DiscordClientConfiguration configuration)  {
		this.configuration = configuration;
	}
	
	public DiscordUserInfo handleOauth(String redirectionUri, String code) {
		return DiscordUserClient.createFromAuthenticationChallenge(configuration, redirectionUri, code).getUserInfo();
	}
}
