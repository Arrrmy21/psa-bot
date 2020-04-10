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

    public String get_list_format = "{\"cmd\":\"%s\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\"}";
    public String get_wishList_format = "{\"cmd\":\"%s\",\"id\":\"%s\",\"curPage\":\"%d\","
            + "\"prevPage\":\"%d\"}";
    public String get_id_format = "{\"cmd\":\"%s\",\"id\":\"%s\"}";
    public String menu_format = "{\"cmd\":\"%s\"}";
    String greetings = String.format(menu_format, "GREETINGS");

    public InlineKeyboardMarkup getMainMenuInlineKeyboard(int userId) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String callBack = String.format(get_list_format, "GETGAMES", 0, 0);
        String wishList = String.format(get_id_format, "GETWL", userId);

        row1.add(new InlineKeyboardButton().setText("Get list of games").setCallbackData(callBack));
        row1.add(new InlineKeyboardButton().setText("My wishList").setCallbackData(wishList));
        row2.add(new InlineKeyboardButton().setText("Hello").setCallbackData(greetings));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getListMenu(JSONObject responseJson, Commands command, int currentPage, int userId) {

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

        sb.append("List of games:\n");
        for (int i = 0; i < totalObjects; i++) {
            sb.append(i + 1).append(") ");
            JSONObject game = gameList.getJSONObject(i);
            String name = game.getString("name");
            String id = game.getString("id");
            sb.append(name).append("\nPrice: ");
            JSONObject price = (JSONObject) game.get("price");
            int currentPrice = (int) price.get("currentPrice");
            sb.append(currentPrice).append("\nCurrent discount: ");
            int currentDiscount = (int) price.get("currentDiscount");
            sb.append(currentDiscount).append("\n\n");

            String callBack = String.format(get_id_format, "GETGAME", id);
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
                    prevPageString = String.format(get_list_format, command, prevPage, currentPage);
                    break;
                case GETWL:
                    prevPageString = String.format(get_wishList_format, command, userId, prevPage, currentPage);
                    break;
            }
            row3.add(new InlineKeyboardButton().setText("Previous page").setCallbackData(prevPageString));
        }

        int lastPage = totalPages - 1;
        if (currentPage < lastPage) {
            String nextPageString = null;
            switch (command) {
                case GETGAMES:
                    nextPageString = String.format(get_list_format, command, nextPage, currentPage);
                    break;
                case GETWL:
                    nextPageString = String.format(get_wishList_format, command, userId, nextPage, currentPage);
                    break;
            }
            row3.add(new InlineKeyboardButton().setText("Next page").setCallbackData(nextPageString));
        }

        row4.add(new InlineKeyboardButton().setText("Back to menu").setCallbackData(greetings));


        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        if (command.equals(Commands.GETWL)) {
            List<InlineKeyboardButton> row5 = new ArrayList<>();
            String clearWl = String.format(get_id_format, Commands.CLEARWL, userId + "/" + "all");
            row5.add(new InlineKeyboardButton().setText("Clear wish list").setCallbackData(clearWl));
            keyboard.add(row5);
        }
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getGameMenu(JSONObject responseJson, int userId) {
        String baseUrl = "https://store.playstation.com/ru-ua/product/";

        String url = responseJson.getString("url");
        String gameId = responseJson.getString("id");
//        ToDo: Show more info about game;

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(new InlineKeyboardButton().setText("Buy game on PS Store").setUrl(baseUrl + url));

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String addToWithListText = String.format(get_id_format, Commands.ADDTOWISHLIST, userId + "/" + gameId);
        row2.add(new InlineKeyboardButton().setText("Add to wishList")
                .setCallbackData(addToWithListText));
        String deleteFromWithListText = String.format(get_id_format, Commands.CLEARWL, userId + "/" + gameId);
        row2.add(new InlineKeyboardButton().setText("Delete from wishList")
                .setCallbackData(deleteFromWithListText));

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(new InlineKeyboardButton().setText("Back to main menu").setCallbackData("Main menu"));


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
