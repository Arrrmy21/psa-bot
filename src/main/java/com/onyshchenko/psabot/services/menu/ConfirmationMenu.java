package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.onyshchenko.psabot.models.common.Command.CLEARWL;
import static com.onyshchenko.psabot.models.common.Command.GETWL;

public class ConfirmationMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData, UserRequest commandLine) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        String clear = new ButtonCallbackRequestBuilder()
                .addCommand(CLEARWL.getId())
                .addId(userUpdateData.getUserId() + "/" + "all")
                .buildRequest();

        String wishListCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETWL.getId())
                .addAdditionalParams(userUpdateData.getUserId())
                .buildRequest();

        row1.add(new InlineKeyboardButton().setText("Yes").setCallbackData(clear));
        row1.add(new InlineKeyboardButton().setText("No").setCallbackData(wishListCallback));
        row2.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(GREETINGS_COMMAND));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
