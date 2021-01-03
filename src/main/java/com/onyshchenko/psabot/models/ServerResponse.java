package com.onyshchenko.psabot.models;

public enum ServerResponse {

    USER_CREATED("User created."),
    USER_EXIST("User already exists."),
    USERNAME_ABSENCE("Sorry, service is available only for users with username. Check your settings"),
    SERVICE_UNAVAILABLE("Sorry, service is temporary unavailable."),
    FAILED_RESPONSE("Failed to get server response."),
    NOTIFICATIONS_ON("User notification = [true]."),
    NOTIFICATIONS_OFF("User notification = [false]."),
    DEFAULT_REPLY("Default reply.");

    private final String textResponse;

    ServerResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public String getTextResponse() {
        return textResponse;
    }
}
