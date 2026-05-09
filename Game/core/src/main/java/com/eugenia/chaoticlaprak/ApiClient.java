package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static Long playerId = null;
    private static Long sessionId = null;

    public static Long getPlayerId() { return playerId; }
    public static Long getSessionId() { return sessionId; }

    // Login
    public static void login(String username, String password,
                             Runnable onSuccess, Runnable onFail) {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        Net.HttpRequest req = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/auth/login")
            .header("Content-Type", "application/json")
            .content(body)
            .build();

        Gdx.net.sendHttpRequest(req, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == 200) {
                    JsonValue json = new JsonReader().parse(response.getResultAsString());
                    playerId = json.getLong("id");
                    onSuccess.run();
                } else {
                    onFail.run();
                }
            }
            @Override
            public void failed(Throwable t) { onFail.run(); }
            @Override
            public void cancelled() { onFail.run(); }
        });
    }

    // Register
    public static void register(String username, String password,
                                Runnable onSuccess, Runnable onFail) {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        Net.HttpRequest req = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/auth/register")
            .header("Content-Type", "application/json")
            .content(body)
            .build();

        Gdx.net.sendHttpRequest(req, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == 200) {
                    onSuccess.run();
                } else {
                    onFail.run();
                }
            }
            @Override
            public void failed(Throwable t) { onFail.run(); }
            @Override
            public void cancelled() { onFail.run(); }
        });
    }

    // Start session
    public static void startSession(Runnable onSuccess) {
        if (playerId == null) return;

        Net.HttpRequest req = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/session/start/" + playerId)
            .header("Content-Type", "application/json")
            .build();

        Gdx.net.sendHttpRequest(req, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == 200) {
                    JsonValue json = new JsonReader().parse(response.getResultAsString());
                    sessionId = json.getLong("id");
                    onSuccess.run();
                }
            }
            @Override
            public void failed(Throwable t) {}
            @Override
            public void cancelled() {}
        });
    }

    // End session (kirim skor)
    public static void endSession(long durationSeconds, Runnable onSuccess) {
        if (sessionId == null) return;

        String body = "{\"sessionId\":" + sessionId +
            ",\"durationSeconds\":" + durationSeconds + "}";

        Net.HttpRequest req = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/session/end")
            .header("Content-Type", "application/json")
            .content(body)
            .build();

        Gdx.net.sendHttpRequest(req, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == 200) {
                    onSuccess.run();
                }
            }
            @Override
            public void failed(Throwable t) {}
            @Override
            public void cancelled() {}
        });
    }
}
