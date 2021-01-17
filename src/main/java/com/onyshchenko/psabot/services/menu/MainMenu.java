package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.onyshchenko.psabot.models.common.Command.CABINET;
import static com.onyshchenko.psabot.models.common.Command.GAMESMENU;
import static com.onyshchenko.psabot.models.common.Command.GETGAMES;
import static com.onyshchenko.psabot.models.common.Command.GETWL;

public class MainMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData,
                                            UserRequest commandLine) {

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

        String gamesMenuCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GAMESMENU.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .buildRequest();

        String cabinetMenuCallback = new ButtonCallbackRequestBuilder()
                .addCommand(CABINET.getId())
                .addId(String.valueOf(userUpdateData.getUserId()))
                .buildRequest();

        row1.add(new InlineKeyboardButton().setText("Games menu").setCallbackData(gamesMenuCallback));
        row1.add(new InlineKeyboardButton().setText("My cabinet").setCallbackData(cabinetMenuCallback));

        if (previousPageData != null) {
            String[] previousPageDataList = previousPageData.split("-");
            ButtonCallbackRequestBuilder previousPageDataBuilder = new ButtonCallbackRequestBuilder();
            if (previousPageDataList[0].equalsIgnoreCase("GG")) {
                previousPageDataBuilder.addCommand(GETGAMES.getId());
                previousPageDataBuilder.addCurrentPage(previousPageDataList[1]);
                previousPageDataBuilder.addPreviousPage(previousPageDataList[1]);
                if (filter != null) {
                    previousPageDataBuilder.addFilter(filter);
                }
            } else {
                previousPageDataBuilder.addCommand(GETWL.getId());
                previousPageDataBuilder.addId(String.valueOf(userUpdateData.getUserId()));
                previousPageDataBuilder.addAdditionalParams(previousPageDataList[1]);
            }

            row2.add(new InlineKeyboardButton().setText("Back to list").setCallbackData(previousPageDataBuilder.buildRequest()));
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
