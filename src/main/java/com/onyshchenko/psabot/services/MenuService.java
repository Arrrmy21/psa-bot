package com.onyshchenko.psabot.services;

import com.onyshchenko.psabot.models.CommandLine;
import com.onyshchenko.psabot.models.Commands;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuService {

    private static final String GET_LIST_FORMAT = "{\"cmd\":\"%d\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\"}";
    private static final String GET_LIST_FORMAT_FILTER = "{\"cmd\":\"%d\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\",\"f\":\"%d\"}";
    private static final String GET_LIST_STRING_FORMAT = "{\"cmd\":\"%d\",\"curPage\":\"%s\","
            + "\"prevPage\":\"%s\"}";
    private static final String GET_LIST_STRING_FORMAT_WITH_FILTER = "{\"cmd\":\"%d\",\"curPage\":\"%s\","
            + "\"prevPage\":\"%s\",\"f\":\"%d\"}";
    private static final String GET_LIST_WITH_ID_FORMAT = "{\"cmd\":\"%d\",\"id\":\"%s\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\"}";
    private static final String GET_ID_FORMAT = "{\"cmd\":\"%d\",\"id\":\"%s\"}";
    private static final String CMD_PLUS_RETURN = "{\"cmd\":\"%d\",\"id\":\"%s\",\"add\":\"%s\"}";
    private static final String CMD_PLUS_RETURN_WITH_FILTER = "{\"cmd\":\"%d\",\"id\":\"%s\",\"add\":\"%s\",\"f\":\"%d\"}";
    private static final String MENU_FORMAT = "{\"cmd\":\"%d\"}";

    private static final String BACK_TO_MAIN_MENU = "Back to main menu";
    private static final String GREETINGS = String.format(MENU_FORMAT, Commands.GREETINGS.getId());
    private static final int GET_GAMES = Commands.GETGAMES.getId();
    private static final int GAMES_MENU = Commands.GAMESMENU.getId();
    private static final int CABINET = Commands.CABINET.getId();
    private static final int GET_WISHLIST = Commands.GETWL.getId();


    public InlineKeyboardMarkup getMainMenuInlineKeyboard(int userId, CommandLine commandLine) {
        String previousPageData = null;
        Integer filter = null;
        if (commandLine != null) {
            previousPageData = commandLine.getPreviousPageInfo();
            if (commandLine.getFilter() != null) {
                filter = commandLine.getFilter().getFilterId();
            }
        }

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String gamesMenu = String.format(GET_LIST_FORMAT, GAMES_MENU, 0, 0);
        String cabinetMenu = String.format(GET_ID_FORMAT, CABINET, userId);

        row1.add(new InlineKeyboardButton().setText("Games menu").setCallbackData(gamesMenu));
        row1.add(new InlineKeyboardButton().setText("My cabinet").setCallbackData(cabinetMenu));

        if (previousPageData != null) {
            String[] previousPageDataList = previousPageData.split("-");
            String previousPage = null;
            if (previousPageDataList[0].equalsIgnoreCase("GG")) {
                if (filter == null) {
                    previousPage = String.format(GET_LIST_STRING_FORMAT, GET_GAMES, previousPageDataList[1], previousPageDataList[1]);
                } else {
                    previousPage = String.format(GET_LIST_STRING_FORMAT_WITH_FILTER, GET_GAMES, previousPageDataList[1], previousPageDataList[1], filter);
                }
            } else {
                previousPage = String.format(CMD_PLUS_RETURN, GET_WISHLIST, userId, previousPageDataList[1]);
            }
            row2.add(new InlineKeyboardButton().setText("Back to list").setCallbackData(previousPage));
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getGamesMenu() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        String gamesWithDiscountCallback = String.format(GET_LIST_FORMAT_FILTER, GET_GAMES, 0, 0, CommandLine.Filter.DISCOUNT_FILTER.getFilterId());
        String allGamesCallback = String.format(GET_LIST_FORMAT, GET_GAMES, 0, 0);
        String freeGamesCallback = String.format(GET_LIST_FORMAT_FILTER, GET_GAMES, 0, 0, CommandLine.Filter.FREE_GAMES_FILTER.getFilterId());

        row1.add(new InlineKeyboardButton().setText("Games with discount").setCallbackData(gamesWithDiscountCallback));
        row2.add(new InlineKeyboardButton().setText("Free games").setCallbackData(freeGamesCallback));
        row3.add(new InlineKeyboardButton().setText("All games").setCallbackData(allGamesCallback));
        row4.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(GREETINGS));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getCabinetMenu(int userId, String switchStatusCommand) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        String wishListCallback = String.format(GET_ID_FORMAT, GET_WISHLIST, userId);
        String switchNotificationStatusCallback = String.format(GET_ID_FORMAT, Commands.SWITCH.getId(), switchStatusCommand);
        String notificationButtonName = "Turn " + switchStatusCommand + " notifications";

        row1.add(new InlineKeyboardButton().setText(notificationButtonName).setCallbackData(switchNotificationStatusCallback));
        row2.add(new InlineKeyboardButton().setText("Wishlist").setCallbackData(wishListCallback));
        row3.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(GREETINGS));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }


    public InlineKeyboardMarkup getListMenu(JSONObject responseJson, CommandLine commandLine, Object id) {

        Commands command = commandLine.getCommand();
        int currentPage = commandLine.getCurPage();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        int totalPages = responseJson.getInt("totalPages");
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        if (prevPage < 0) {
            prevPage = 0;
        }

        StringBuilder sb = new StringBuilder();

        //ToDo: change key value
        JSONArray gameList = responseJson.getJSONArray("content");
        int totalObjects = gameList.length();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        sb.append("List of games:\n\n");
        for (int i = 0; i < totalObjects; i++) {
            sb.append(i + 1).append(") ");
            JSONObject game = gameList.getJSONObject(i);
            String name = game.getString("name");
            String gameId = game.getString("id");
            sb.append(name).append("\nPrice: ");
            JSONObject price = (JSONObject) game.get("price");
            int currentPrice = (int) price.get("currentPrice");
            sb.append(currentPrice).append("\nCurrent discount: ");
            int currentDiscount = (int) price.get("currentDiscount");
            sb.append(currentDiscount).append("\n\n");

            String current;
            if (command.equals(Commands.GETGAMES)) {
                current = "GG-" + currentPage;
            } else {
                current = "WL-" + currentPage;
            }
            String callBack;
            if (commandLine.getFilter() == null) {
                callBack = String.format(CMD_PLUS_RETURN, Commands.GETGAME.getId(), gameId, current);
            } else {
                callBack = String.format(CMD_PLUS_RETURN_WITH_FILTER, Commands.GETGAME.getId(), gameId, current, commandLine.getFilter().getFilterId());
            }
            if (i < 5) {
                row1.add(new InlineKeyboardButton().setText(String.valueOf(i + 1)).setCallbackData(callBack));
            } else {
                row2.add(new InlineKeyboardButton().setText(String.valueOf(i + 1)).setCallbackData(callBack));
            }

        }

        if (prevPage != currentPage) {
            String prevPageString = null;
            switch (command) {
                case GETGAMES:
                    if (commandLine.getFilter() != null) {
                        prevPageString = String.format(GET_LIST_FORMAT_FILTER, command.getId(), prevPage, currentPage, commandLine.getFilter().getFilterId());
                    } else {
                        prevPageString = String.format(GET_LIST_FORMAT, command.getId(), prevPage, currentPage);
                    }
                    break;
                default:
                    prevPageString = String.format(GET_LIST_WITH_ID_FORMAT, command.getId(), id, prevPage, currentPage);
            }
            row3.add(new InlineKeyboardButton().setText("Previous page").setCallbackData(prevPageString));
        }

        int lastPage = totalPages - 1;
        if (currentPage < lastPage) {
            String nextPageString = null;
            switch (command) {
                case GETGAMES:
                    if (commandLine.getFilter() != null) {
                        nextPageString = String.format(GET_LIST_FORMAT_FILTER, command.getId(), nextPage, currentPage, commandLine.getFilter().getFilterId());
                    } else {
                        nextPageString = String.format(GET_LIST_FORMAT, command.getId(), nextPage, currentPage);
                    }
                    break;
                default:
                    nextPageString = String.format(GET_LIST_WITH_ID_FORMAT, command.getId(), id, nextPage, currentPage);
            }
            row3.add(new InlineKeyboardButton().setText("Next page").setCallbackData(nextPageString));
        }

        row4.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(GREETINGS));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        if (command.equals(Commands.GETWL)) {
            List<InlineKeyboardButton> row5 = new ArrayList<>();
            String clearWl = String.format(GET_ID_FORMAT, Commands.CLEARWL.getId(), id + "/" + "all");
            row5.add(new InlineKeyboardButton().setText("Clear wish list").setCallbackData(clearWl));
            keyboard.add(row5);
        }
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getGameMenu(JSONObject responseJson, int userId, CommandLine commandLine) {
        String baseUrl = "https://store.playstation.com/ru-ua/product/";

        String previousPageData = commandLine.getPreviousPageInfo();
        Integer filter = null;
        if (commandLine.getFilter() != null) {
            filter = commandLine.getFilter().getFilterId();
        }
        String url = responseJson.getString("url");
        String gameId = responseJson.getString("id");

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(new InlineKeyboardButton().setText("Buy game on PS Store").setUrl(baseUrl + url));

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String addToWithListText;
        if (filter == null) {
            addToWithListText = String.format(CMD_PLUS_RETURN, Commands.ADDTOWL.getId(), userId + "/" + gameId, previousPageData);
        } else {
            addToWithListText = String.format(CMD_PLUS_RETURN_WITH_FILTER, Commands.ADDTOWL.getId(), userId + "/" + gameId, previousPageData, filter);
        }
        row2.add(new InlineKeyboardButton().setText("Add to wishList")
                .setCallbackData(addToWithListText));
        String deleteFromWithListText = String.format(CMD_PLUS_RETURN, Commands.CLEARWL.getId(), userId + "/" + gameId, previousPageData);
        row2.add(new InlineKeyboardButton().setText("Delete from wishList")
                .setCallbackData(deleteFromWithListText));

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(GREETINGS));

        if (previousPageData != null) {
            String[] previousPageDataList = previousPageData.split("-");
            String previousPage = null;
            if (previousPageDataList[0].equalsIgnoreCase("GG")) {
                if (filter == null) {
                    previousPage = String.format(GET_LIST_STRING_FORMAT, GET_GAMES, previousPageDataList[1], previousPageDataList[1]);
                } else {
                    previousPage = String.format(GET_LIST_STRING_FORMAT_WITH_FILTER, GET_GAMES, previousPageDataList[1], previousPageDataList[1], filter);
                }
            } else {
                previousPage = String.format(CMD_PLUS_RETURN, GET_WISHLIST, userId, previousPageDataList[1]);
            }
            row3.add(new InlineKeyboardButton().setText("Back to list").setCallbackData(previousPage));
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
