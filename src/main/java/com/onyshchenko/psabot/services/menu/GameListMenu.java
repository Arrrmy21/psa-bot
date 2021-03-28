package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.common.Command;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.models.response.dto.GameDto;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.onyshchenko.psabot.models.common.Command.CONFIRM;
import static com.onyshchenko.psabot.models.common.Command.GETGAME;

public class GameListMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData,
                                            UserRequest userRequest) {

        Command command = userRequest.getCommand();
        int currentPage = userRequest.getCurPage();
        int nextPage = currentPage + 1;
        int prevPage = Math.max(currentPage - 1, 0);

        ResponseBody responseBody = (ResponseBody) uniqueObject;

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();

        String current;
        if (command.equals(Command.GETGAMES)) {
            current = "GG-" + currentPage;
        } else if (command.equals(Command.SEARCH)) {
            current = "SE-" + currentPage;
        } else {
            current = "WL-" + currentPage;
        }

        int buttonName = 0;
        List<GameDto> gameList = responseBody.getGameList();
        for (GameDto game : gameList) {
            buttonName++;
            ButtonCallbackRequestBuilder callBackBuilder = new ButtonCallbackRequestBuilder()
                    .addCommand(GETGAME.getId())
                    .addId(game.getId())
                    .addAdditionalParams(current)
                    .addVersion(userRequest.getVersion());
            if (userRequest.getFilters() != null) {
                callBackBuilder.addFilters(userRequest.getFilterIds());
            }
            if (buttonName <= 5) {
                row1.add(new InlineKeyboardButton().setText(String.valueOf(buttonName)).setCallbackData(callBackBuilder.buildRequest()));
            } else {
                row2.add(new InlineKeyboardButton().setText(String.valueOf(buttonName)).setCallbackData(callBackBuilder.buildRequest()));
            }
        }

        if (prevPage != currentPage) {
            ButtonCallbackRequestBuilder prevPageBuilder = new ButtonCallbackRequestBuilder()
                    .addCommand(command.getId())
                    .addPreviousPage(String.valueOf(currentPage))
                    .addCurrentPage(String.valueOf(prevPage))
                    .addVersion(userRequest.getVersion());
            if (userRequest.getFilters() != null) {
                prevPageBuilder.addFilters(userRequest.getFilterIds());
            }
            row3.add(new InlineKeyboardButton().setText("Previous page").setCallbackData(prevPageBuilder.buildRequest()));
        }

        int totalPages = responseBody.getTotalPages();
        int lastPage = totalPages - 1;
        if (currentPage < lastPage) {
            ButtonCallbackRequestBuilder nextPageBuilder = new ButtonCallbackRequestBuilder()
                    .addCommand(command.getId())
                    .addCurrentPage(String.valueOf(nextPage))
                    .addPreviousPage(String.valueOf(currentPage))
                    .addVersion(userRequest.getVersion());
            if (userRequest.getFilters() != null) {
                nextPageBuilder.addFilters(userRequest.getFilterIds());
            }
            row3.add(new InlineKeyboardButton().setText("Next page").setCallbackData(nextPageBuilder.buildRequest()));
        }
        String greetingsCommandCallBack = new ButtonCallbackRequestBuilder().addCommand(3)
                .addVersion(userRequest.getVersion())
                .buildRequest();
        row4.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(greetingsCommandCallBack));

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        if (command.equals(Command.GETWL) && !gameList.isEmpty()) {
            String clearWishlistCallback = new ButtonCallbackRequestBuilder()
                    .addCommand(CONFIRM.getId())
                    .addId(userUpdateData.getUserId() + "/" + "all")
                    .addVersion(userRequest.getVersion())
                    .buildRequest();
            row5.add(new InlineKeyboardButton().setText("Clear wish list").setCallbackData(clearWishlistCallback));
            keyboard.add(row5);
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
