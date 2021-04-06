package com.onyshchenko.psabot.services.menu;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.menu.ButtonCallbackRequestBuilder;
import com.onyshchenko.psabot.models.common.Command;
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
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.EA_ACCESS;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.EXCLUSIVE;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.FREE_GAMES_FILTER;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.GAMES_FILTER;
import static com.onyshchenko.psabot.models.request.UserRequest.Filter.PS_PLUS;

public class GamesMenu extends MenuProvider {

    @Override
    public InlineKeyboardMarkup prepareMenu(Object uniqueObject, UserUpdateData userUpdateData, UserRequest userRequest) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        List<InlineKeyboardButton> row7 = new ArrayList<>();

        String gamesWithDiscountCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        DISCOUNT_FILTER.getFilterId(),
                        GAMES_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String psPlusGamesCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        PS_PLUS.getFilterId(),
                        GAMES_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String eaAccessGamesCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        EA_ACCESS.getFilterId(),
                        GAMES_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String exclusiveGamesCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        EXCLUSIVE.getFilterId(),
                        GAMES_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String allGamesCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Collections.singletonList(
                        GAMES_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String freeGamesCallback = new ButtonCallbackRequestBuilder()
                .addCommand(GETGAMES.getId())
                .addCurrentPage(String.valueOf(0))
                .addPreviousPage(String.valueOf(0))
                .addFilters(Arrays.asList(
                        FREE_GAMES_FILTER.getFilterId(),
                        GAMES_FILTER.getFilterId()))
                .addVersion(userRequest.getVersion())
                .buildRequest();

        String greetings = new ButtonCallbackRequestBuilder()
                .addCommand(Command.GREETINGS.getId())
                .addVersion(userRequest.getVersion())
                .buildRequest();

        row1.add(new InlineKeyboardButton().setText("Games with discount").setCallbackData(gamesWithDiscountCallback));
        row2.add(new InlineKeyboardButton().setText("PS Plus Discount").setCallbackData(psPlusGamesCallback));
        row3.add(new InlineKeyboardButton().setText("EA Access games").setCallbackData(eaAccessGamesCallback));
        row4.add(new InlineKeyboardButton().setText("PS Exclusives").setCallbackData(exclusiveGamesCallback));

        row5.add(new InlineKeyboardButton().setText("Free games").setCallbackData(freeGamesCallback));
        row6.add(new InlineKeyboardButton().setText("All games").setCallbackData(allGamesCallback));
        row7.add(new InlineKeyboardButton().setText(BACK_TO_MAIN_MENU).setCallbackData(greetings));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);
        keyboard.add(row7);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}


