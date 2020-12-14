package com.onyshchenko.psabot.services;

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

    private static final String GET_LIST_FORMAT = "{\"cmd\":\"%s\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\"}";
    private static final String GET_LIST_STRING_FORMAT = "{\"cmd\":\"%s\",\"curPage\":\"%s\","
            + "\"prevPage\":\"%s\"}";
    private static final String GET_LIST_WITH_ID_FORMAT = "{\"cmd\":\"%s\",\"id\":\"%s\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\"}";
    private static final String GET_ID_FORMAT = "{\"cmd\":\"%s\",\"id\":\"%s\"}";
    private static final String CMD_PLUS_RETURN = "{\"cmd\":\"%s\",\"id\":\"%s\",\"add\":\"%s\"}";
    private static final String MENU_FORMAT = "{\"cmd\":\"%s\"}";

    private static final String GREETINGS = String.format(MENU_FORMAT, "GREETINGS");
    private static final String GET_GAMES = "GETGAMES";
    private static final String GET_WISHLIST = "GETWL";

    public InlineKeyboardMarkup getMainMenuInlineKeyboard(int userId, String previousPageData) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String callBack = String.format(GET_LIST_FORMAT, GET_GAMES, 0, 0);
        String wishList = String.format(GET_ID_FORMAT, GET_WISHLIST, userId);

        row1.add(new InlineKeyboardButton().setText("Get list of games").setCallbackData(callBack));
        row1.add(new InlineKeyboardButton().setText("My wishList").setCallbackData(wishList));

        if (previousPageData != null) {
            String[] previousPageDataList = previousPageData.split("-");
            String previousPage = null;
            if (previousPageDataList[0].equalsIgnoreCase("GG")) {
                previousPage = String.format(GET_LIST_STRING_FORMAT, GET_GAMES, previousPageDataList[1], previousPageDataList[1]);
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

    public InlineKeyboardMarkup getListMenu(JSONObject responseJson, Commands command, int currentPage, Object id) {

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
            String callBack = String.format(CMD_PLUS_RETURN, "GETGAME", gameId, current);
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
                    prevPageString = String.format(GET_LIST_FORMAT, command, prevPage, currentPage);
                    break;
                default:
                    prevPageString = String.format(GET_LIST_WITH_ID_FORMAT, command, id, prevPage, currentPage);
            }
            row3.add(new InlineKeyboardButton().setText("Previous page").setCallbackData(prevPageString));
        }

        int lastPage = totalPages - 1;
        if (currentPage < lastPage) {
            String nextPageString = null;
            switch (command) {
                case GETGAMES:
                    nextPageString = String.format(GET_LIST_FORMAT, command, nextPage, currentPage);
                    break;
                default:
                    nextPageString = String.format(GET_LIST_WITH_ID_FORMAT, command, id, nextPage, currentPage);
            }
            row3.add(new InlineKeyboardButton().setText("Next page").setCallbackData(nextPageString));
        }

        row4.add(new InlineKeyboardButton().setText("Back to main menu").setCallbackData(GREETINGS));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        if (command.equals(Commands.GETWL)) {
            List<InlineKeyboardButton> row5 = new ArrayList<>();
            String clearWl = String.format(GET_ID_FORMAT, Commands.CLEARWL, id + "/" + "all");
            row5.add(new InlineKeyboardButton().setText("Clear wish list").setCallbackData(clearWl));
            keyboard.add(row5);
        }
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getGameMenu(JSONObject responseJson, int userId, String previousPageData) {
        String baseUrl = "https://store.playstation.com/ru-ua/product/";

        String url = responseJson.getString("url");
        String gameId = responseJson.getString("id");

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(new InlineKeyboardButton().setText("Buy game on PS Store").setUrl(baseUrl + url));

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String addToWithListText = String.format(CMD_PLUS_RETURN, Commands.ADDTOWL, userId + "/" + gameId, previousPageData);
        row2.add(new InlineKeyboardButton().setText("Add to wishList")
                .setCallbackData(addToWithListText));
        String deleteFromWithListText = String.format(CMD_PLUS_RETURN, Commands.CLEARWL, userId + "/" + gameId, previousPageData);
        row2.add(new InlineKeyboardButton().setText("Delete from wishList")
                .setCallbackData(deleteFromWithListText));

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(new InlineKeyboardButton().setText("Back to main menu").setCallbackData(GREETINGS));

        if (previousPageData != null) {
            String[] previousPageDataList = previousPageData.split("-");
            String previousPage = null;
            if (previousPageDataList[0].equalsIgnoreCase("GG")) {
                previousPage = String.format(GET_LIST_STRING_FORMAT, GET_GAMES, previousPageDataList[1], previousPageDataList[1]);
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
