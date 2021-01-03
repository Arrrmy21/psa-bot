package com.onyshchenko.psabot.models;

public enum Commands {

    GETGAMES("getGames", 0),
    GETGAME("getGame", 1),
    REGISTERUSER("registerUser", 2),
    GREETINGS("hello", 3),
    ADDTOWL("addToWishList", 4),
    GETWL("getWishList", 5),
    CLEARWL("clearWishList", 6),
    SEARCH("searchGame", 7),
    GAMESMENU("gamesMenu", 8),
    CABINET("cabinet", 9),
    SWITCH("switchNotifications", 10),
    REGULARREPLY("regularReply", 11);

    private final String commandName;
    private final int id;

    Commands(String commandName, int id) {
        this.commandName = commandName;
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
