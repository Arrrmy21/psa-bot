package com.onyshchenko.psabot.models.common;

import java.util.HashMap;
import java.util.Map;

public enum StandardResponse {

    USER_CREATED("User created."),
    USER_EXIST("User already exists."),
    USERNAME_ABSENCE("Sorry, service is available only for users with username. Check your settings"),
    SERVICE_UNAVAILABLE("Sorry, service is temporary unavailable."),
    FAILED_RESPONSE("Failed to get server response."),
    NOTIFICATIONS_ON("User notification = [true]."),
    NOTIFICATIONS_OFF("User notification = [false]."),
    GREETINGS("Hello, {}.\nYou can search for games typing \"name: [name of game]\"\nexample:\nname: Mafia"),
    WELCOME("Welcome, {}.\nYou can search for games typing \"name: [name of game]\"\nexample:\nname: Mafia"),
    WELCOME_BACK("Welcome back, {}.\nYou can search for games typing \"name: [name of game]\"\nexample:\nname: Mafia"),
    GAMES_MENU("Games menu"),
    OTHER_PRODUCTS_MENU("Other products menu"),
    TURN_ON_NOTIFICATIONS("ON"),
    TURN_OFF_NOTIFICATIONS("OFF"),
    GAME_ADDED_TO_WL("Game added to wish list"),
    GAME_DELETED_FROM_WL("Game deleted from wish list"),
    ERROR_RESPONSE("Error occurred."),
    GET_GAME_RESPONSE("Get game response"),
    GET_GAMES_RESPONSE("Get games response"),
    GET_WISHLIST("Get wishlist response"),
    SEARCH_GAMES("Get search games result."),
    CONFIRM_DELETE("You really want to delete all games from wishlist?"),
    VERSION_ERROR("Your previous keyboards in bot are deprecated. \n\nPlease use keyboard below or clear history in bot settings."),
    DEFAULT_REPLY("Default reply.");

    private static final String TIP1 = "\nYou can search for games typing \"name: [name of game]\"";
    private static final String TIP2 = "\nexample:\nname: Mafia";

    private static final Map<String, StandardResponse> responses = new HashMap<>();

    private final String textResponse;

    StandardResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public String getTextResponse() {
        return textResponse;
    }

    static {
        for (StandardResponse e : values()) {
            responses.put(e.getTextResponse(), e);
        }
    }

    public static StandardResponse getResponseByValue(String value) {
        StandardResponse response = responses.get(value);
        if (response == null) {
            return ERROR_RESPONSE;
        }
        return response;
    }

}
