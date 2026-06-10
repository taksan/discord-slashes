package com.objective.discord.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective.discord.utils.HttpClientWrapper;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.objective.discord.utils.HttpClientUtils.toUrlParameters;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.net.http.HttpRequest.newBuilder;

public class DiscordUserClient {
    private final AuthenticatedHttpClientWrapper httpClient;

    public DiscordUserClient(OauthCredentialsDto userOauth) {
        httpClient = new AuthenticatedHttpClientWrapper(userOauth);
    }

    public DiscordUserInfo getUserInfo() {
        final ObjectMapper mapper = new ObjectMapper()
			.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

		final HttpResponse<String> userInfo = httpClient.get("https://discord.com/api/users/@me");
		if (userInfo.statusCode() > 399)
			throw new OauthFailureException("Failure fetching user info");

		try {
            return mapper.readValue(userInfo.body(), DiscordUserInfo.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new OauthFailureException("Failed to parse user info response", e);
		}
    }

    public static DiscordUserClient createFromAuthenticationChallenge(
            DiscordClientConfiguration configuration,
            String redirectionUri,
            String code) {
        final HttpClientWrapper httpClient = new HttpClientWrapper();

		final HttpRequest tokenRequest = newBuilder(URI.create("https://discord.com/api/oauth2/token"))
			.setHeader("Content-Type", "application/x-www-form-urlencoded")
			.POST(ofString(toUrlParameters(
                "client_id", configuration.clientId,
                "client_secret", configuration.clientSecret,
                "code", code,
                "grant_type", "authorization_code",
                "redirect_uri", redirectionUri,
                "scope", "identify"
            )))
			.build();

		final HttpResponse<String> response = httpClient.send(tokenRequest);
		if (response.statusCode() > 399 )
			throw new OauthFailureException(response.body());

		return new DiscordUserClient(parseOauthCredentials(response));
    }

    private static OauthCredentialsDto parseOauthCredentials(HttpResponse<String> response) {
        final ObjectMapper mapper = new ObjectMapper()
			.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			final String body = response.body();
			return mapper.readValue(body, OauthCredentialsDto.class);
		} catch (JsonProcessingException cause) {
			cause.printStackTrace();
			throw new OauthFailureException("Failed to parse oauth response", cause);
		}
	}

	static class AuthenticatedHttpClientWrapper {
        private final HttpClientWrapper httpClient = new HttpClientWrapper();
        private final OauthCredentialsDto userOauth;

        AuthenticatedHttpClientWrapper(OauthCredentialsDto userOauth) {
            this.userOauth = userOauth;
        }

        public HttpResponse<String> get(String uri) {
            final HttpRequest infoRequest = newBuilder(URI.create(uri))
				.setHeader("authorization", userOauth.token_type + " " + userOauth.access_token)
				.build();

            return httpClient.send(infoRequest);
        }
    }
}
