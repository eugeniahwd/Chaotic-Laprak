package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;

public class ApiClient {
    private static final String BASE_URL = "https://chaotic-laprak-8dy-production.up.railway.app";
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
                System.out.println("LOGIN STATUS: " + response.getStatus().getStatusCode());
                String responseBody = response.getResultAsString();
                System.out.println("LOGIN BODY: " + responseBody);
                if (response.getStatus().getStatusCode() == 200) {
                    try {
                        JsonValue json = new JsonReader().parse(responseBody);
                        playerId = json.getLong("id");
                        System.out.println("playerId set to: " + playerId);
                        onSuccess.run();
                    } catch (Exception e) {
                        System.out.println("Parse error: " + e.getMessage());
                        onFail.run();
                    }
                } else {
                    onFail.run();
                }
            }
            @Override
            public void failed(Throwable t) {
                System.out.println("FAILED: " + t.getMessage());
                onFail.run();
            }
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
            public void failed(Throwable t) {
                System.out.println("FAILED: " + t.getMessage());
                onFail.run();
            }
            @Override
            public void cancelled() { onFail.run(); }
        });
    }

    // Start session
    public static void startSession(Runnable onSuccess) {
        System.out.println("START SESSION called, playerId: " + playerId);
        if (playerId == null) {
            System.out.println("playerId is NULL, aborting!");
            return;
        }

        Net.HttpRequest req = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/session/start/" + playerId)
            .header("Content-Type", "application/json")
            .build();

        Gdx.net.sendHttpRequest(req, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                System.out.println("SESSION STATUS: " + response.getStatus().getStatusCode());
                System.out.println("SESSION BODY: " + response.getResultAsString());
                if (response.getStatus().getStatusCode() == 200) {
                    JsonValue json = new JsonReader().parse(response.getResultAsString());
                    sessionId = json.getLong("id");
                    onSuccess.run();
                }
            }
            @Override
            public void failed(Throwable t) {
                System.out.println("FAILED: " + t.getMessage());
            }
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
            public void failed(Throwable t) {
                System.out.println("FAILED: " + t.getMessage());
            }
            @Override
            public void cancelled() {}
        });
    }
}
