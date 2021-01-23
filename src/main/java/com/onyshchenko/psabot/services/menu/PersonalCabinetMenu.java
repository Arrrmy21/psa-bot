package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.common.Command;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.onyshchenko.psabot.models.common.Command.GETWL;

public class PersonalCabinetMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData,
                                            UserRequest userRequest) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        String wishListCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETWL.getId())
                .addAdditionalParams(userUpdateData.getUserId())
                .addVersion(userRequest.getVersion())
                .buildRequest();

        ResponseBody responseBody = (ResponseBody) uniqueObject;

        String switchStatusCommand;
        if (responseBody.isCurrentNotificationStatus()) {
            switchStatusCommand = "OFF";
        } else {
            switchStatusCommand = "ON";
        }

        String notificationButtonName = "Turn " + switchStatusCommand + " notifications";

        String switchNotificationStatusCallback = new ButtonCallbackRequestBuilder()
                .addCommand(Command.SWITCH.getId())
                .addAdditionalParams(switchStatusCommand)
                .addVersion(userRequest.getVersion())
                .buildRequest();

        row1.add(new InlineKeyboardButton().setText(notificationButtonName).setCallbackData(switchNotificationStatusCallback));
        row2.add(new InlineKeyboardButton().setText("Wishlist").setCallbackData(wishListCallback));
        String greetingsCommandCallBack = new ButtonCallbackRequestBuilder()
                .addCommand(3)
                .addVersion(userRequest.getVersion())
                .buildRequest();
        row3.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(greetingsCommandCallBack));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
