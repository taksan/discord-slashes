package com.objective.discord.utils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;

public class HttpClientWrapper {
    private final CookieManager cookieManager = new CookieManager();
    private final HttpClient httpClient;

    public HttpClientWrapper() {
        CookieHandler.setDefault(this.cookieManager);
        this.httpClient = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).cookieHandler(CookieHandler.getDefault()).build();
    }

    public HttpResponse<String> send(HttpRequest request) {
        try {
            return this.httpClient.send(request, BodyHandlers.ofString());
        } catch (InterruptedException | IOException var3) {
            throw new IllegalStateException(var3);
        }
    }

    public void addCookie(String location, String cookieName, String cookieValue) {
        this.cookieManager.getCookieStore().add(URI.create(location), new HttpCookie(cookieName, cookieValue));
    }
}
