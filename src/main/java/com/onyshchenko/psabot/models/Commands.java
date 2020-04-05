package com.onyshchenko.psabot.models;

public enum Commands {

    GETGAMES("getGames"),
    GETGAME("getGame"),
    REGISTERUSER("registerUser"),
    GREETINGS("hello"),
    ADDTOWISHLIST("addToWishList"),
    GETWL("getWishList"),
    REGULARREPLY("regularReply");

    public String commandName;

    Commands(String commandName) {
        this.commandName = commandName;
    }
}
