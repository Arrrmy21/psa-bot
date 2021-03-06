package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.common.Command;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.onyshchenko.psabot.models.common.Command.GETGAMES;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.DISCOUNT_FILTER;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.FREE_GAMES_FILTER;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.OTHER_PRODUCTS_FILTER;

public class OtherProductsMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData, UserRequest userRequest) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();


        String productsWithDiscountCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        DISCOUNT_FILTER.getFilterId(),
                        OTHER_PRODUCTS_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String allProductsCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Collections.singletonList(OTHER_PRODUCTS_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String freeProductsCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        FREE_GAMES_FILTER.getFilterId(),
                        OTHER_PRODUCTS_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String greetings = new ButtonCallbackRequestBuilder()
                .addCommand(Command.GREETINGS.getId())
                .addVersion(userRequest.getVersion())
                .buildRequest();

        row1.add(new InlineKeyboardButton().setText("Products with discount").setCallbackData(productsWithDiscountCallback));
        row2.add(new InlineKeyboardButton().setText("Free products").setCallbackData(freeProductsCallback));
        row3.add(new InlineKeyboardButton().setText("All products").setCallbackData(allProductsCallback));
        row4.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(greetings));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}


