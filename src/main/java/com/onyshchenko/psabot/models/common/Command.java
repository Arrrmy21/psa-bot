package com.onyshchenko.psabot.models.common;

import com.onyshchenko.psabot.services.menu.ConfirmationMenu;
import com.onyshchenko.psabot.services.menu.OtherProductsMenu;
import com.onyshchenko.psabot.services.menu.PersonalCabinetMenu;
import com.onyshchenko.psabot.services.menu.GameSingleMenu;
import com.onyshchenko.psabot.services.menu.GamesMenu;
import com.onyshchenko.psabot.services.menu.GameListMenu;
import com.onyshchenko.psabot.services.menu.MainMenu;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;

import java.util.EnumMap;

public enum Command {

    GETGAMES("getGames", 0, new GameListMenu()),
    GETGAME("getGame", 1, new GameSingleMenu()),
    REGISTERUSER("registerUser", 2, new MainMenu()),
    GREETINGS("hello", 3, new MainMenu()),
    ADDTOWL("addToWishList", 4, new MainMenu()),
    GETWL("getWishList", 5, new GameListMenu()),
    CLEARWL("clearWishList", 6, new MainMenu()),
    SEARCH("searchGame", 7, new GameListMenu()),
    GAMESMENU("gamesMenu", 8, new GamesMenu()),
    CABINET("cabinet", 9, new PersonalCabinetMenu()),
    SWITCH("switchNotifications", 10, new PersonalCabinetMenu()),
    REGULAR_REPLY("regularReply", 11, new MainMenu()),
    CONFIRM("confirm", 12, new ConfirmationMenu()),
    OTHER_PRODUCTS_MENU("otherProductsMenu", 13, new OtherProductsMenu()),
    HELP("help", 20, new MainMenu());


    private final String commandName;
    private final int id;
    private final MenuProvider menuProvider;

    Command(String commandName, int id, MenuProvider menuProvider) {
        this.commandName = commandName;
        this.id = id;
        this.menuProvider = menuProvider;
    }

    public int getId() {
        return id;
    }

    private static final EnumMap<Command, MenuProvider> menusMap = new EnumMap<>(Command.class);

    static {
        for (Command command : values()) {
            menusMap.put(command, command.menuProvider);
        }
    }

    public static MenuProvider getCommandRelatedMenu(Command command) {
        MenuProvider menu = menusMap.get(command);
        if (menu == null) {
            return new MainMenu();
        }
        return menu;
    }
}
