package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.models.response.dto.GameDto;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.onyshchenko.psabot.models.common.Command.ADDTOWL;
import static com.onyshchenko.psabot.models.common.Command.CLEARWL;
import static com.onyshchenko.psabot.models.common.Command.GETGAMES;
import static com.onyshchenko.psabot.models.common.Command.GETWL;

public class GameMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData, UserRequest commandLine) {

        String baseUrl = "https://store.playstation.com/ru-ua/product/";

        Integer filter = null;
        if (commandLine.getFilter() != null) {
            filter = commandLine.getFilter().getFilterId();
        }

        ResponseBody responseBody = (ResponseBody) uniqueObject;
        GameDto game = responseBody.getGameList().get(0);

        boolean isGameInWishList = game.isInWl();
        String url = game.getUrl();
        String gameId = game.getId();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(new InlineKeyboardButton().setText("Buy game on PS Store").setUrl(baseUrl + url));

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String previousPageData = commandLine.getPreviousPageInfo();

        if (!isGameInWishList) {
            ButtonCallbackRequestBuilder addToWithListTextBuilder = new ButtonCallbackRequestBuilder()
                    .addCommand(ADDTOWL.getId())
                    .addId(userUpdateData.getUserId() + "/" + gameId)
                    .addAdditionalParams(previousPageData);

            if (filter != null) {
                addToWithListTextBuilder.addFilter(filter);
            }
            String addToWithListText = addToWithListTextBuilder.buildRequest();

            row2.add(new InlineKeyboardButton().setText("Add to wishList").setCallbackData(addToWithListText));
        } else {
            ButtonCallbackRequestBuilder deleteFromWithListBuilder = new ButtonCallbackRequestBuilder()
                    .addCommand(CLEARWL.getId())
                    .addId(userUpdateData.getUserId() + "/" + gameId)
                    .addAdditionalParams(previousPageData);

            if (filter != null) {
                deleteFromWithListBuilder.addFilter(filter);
            }
            String deleteFromWithListText = deleteFromWithListBuilder.buildRequest();
            row2.add(new InlineKeyboardButton().setText("Delete from wishList")
                    .setCallbackData(deleteFromWithListText));
        }

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(GREETINGS_COMMAND));

        if (previousPageData != null) {
            String[] previousPageDataList = previousPageData.split("-");

            if (!previousPageDataList[0].equalsIgnoreCase("SE")) {

                ButtonCallbackRequestBuilder previousPageBuilder = new ButtonCallbackRequestBuilder();
                if (previousPageDataList[0].equalsIgnoreCase("GG")) {
                    previousPageBuilder.addCommand(GETGAMES.getId());
                } else {
                    previousPageBuilder.addCommand(GETWL.getId());
                }
                previousPageBuilder.addCurrentPage(previousPageDataList[1])
                        .addPreviousPage(previousPageDataList[1]);

                if (filter != null) {
                    previousPageBuilder.addFilter(filter);
                }

                String previousPage = previousPageBuilder.buildRequest();
                row3.add(new InlineKeyboardButton().setText("Back to list").setCallbackData(previousPage));
            }
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}


