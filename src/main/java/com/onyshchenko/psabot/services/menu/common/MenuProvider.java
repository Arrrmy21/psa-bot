package com.onyshchenko.psabot.services.menu.common;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public abstract class MenuProvider {

    public static final String BACK_TO_MAIN_MENU = "Back to main menu";
    public static final String GREETINGS_COMMAND = new ButtonCallbackRequestBuilder().addCommand(3)
            .buildRequest();

    public abstract InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData,
                                                     UserRequest commandLine);

}
