package com.onyshchenko.psabot;

import com.onyshchenko.psabot.models.CommandLine;
import com.onyshchenko.psabot.models.Commands;
import com.onyshchenko.psabot.models.User;
import com.onyshchenko.psabot.services.CommandService;
import com.onyshchenko.psabot.services.HtmlService;
import com.onyshchenko.psabot.services.JsonCustomParser;
import com.onyshchenko.psabot.services.MenuService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    private static final String GAMES = "games?";
    private static final String PAGE = "page=";
    private static final String TIP1 = "\nYou can search for games typing \"name: [name of game]\"";
    private static final String TIP2 = "\nexample:\nname: Mafia";

    @Autowired
    private CommandService commandService;

    @Autowired
    private HtmlService htmlService;

    @Autowired
    private MenuService menuService;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.userName}")
    private String username;

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received update message. Id: [{}]", update.getUpdateId());
        CommandLine command;
        int userId;
        long chatId;
        String requestData;
        SendMessage responseMessage = null;
        EditMessageText editMessage = null;
        InlineKeyboardMarkup keyboardForResponse = null;
        String userUniqueName;

        if (update.hasMessage() && update.getMessage().getText() != null) {
            userId = update.getMessage().getFrom().getId();
            requestData = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            userUniqueName = update.getMessage().getFrom().getUserName();
            LOGGER.info("Update has message from user: [{}]", userId);
        } else {
            userId = update.getCallbackQuery().getFrom().getId();
            userUniqueName = update.getCallbackQuery().getFrom().getUserName();
            requestData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            LOGGER.info("Update has callback form user: [{}]", userId);
        }
        String textForResponse = "Default reply";
        command = commandService.prepareCommandFromRequest(requestData);

        if (command.getCmd().equals(Commands.REGISTERUSER)
                || command.getCmd().equals(Commands.SEARCH) || command.getCmd().equals(Commands.GETWL)
                || command.getCmd().equals(Commands.GETGAME)) {
            responseMessage = new SendMessage();
            responseMessage.setChatId(chatId);
        } else {
            editMessage = new EditMessageText();
            editMessage.setChatId(chatId);
            editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        }

        switch (command.getCmd()) {
            case REGISTERUSER:
                if (userUniqueName == null) {
                    textForResponse = "Sorry, service is available only for users with username. Check your settings";
                    break;
                }
                User newUser = new User();
                newUser.setUserId(update.getMessage().getFrom().getId());
                newUser.setFirstName(update.getMessage().getFrom().getFirstName());
                newUser.setLastName(update.getMessage().getFrom().getLastName());
                newUser.setUsername(update.getMessage().getFrom().getUserName());
                newUser.setChatId(chatId);

                String registerUserResponse = htmlService.registerUser(newUser);
                StringBuilder textForResponseBuilder = new StringBuilder();
                if (registerUserResponse.equalsIgnoreCase("User created.")) {
                    textForResponseBuilder.append("Welcome, ").append(newUser.getUsername()).append(".");
                    textForResponseBuilder.append(TIP1).append(TIP2);
                    textForResponse = textForResponseBuilder.toString();
                    takeTokenForUser(newUser.getUsername());
                } else if (registerUserResponse.equalsIgnoreCase("User already exists.")) {
                    textForResponseBuilder.append("Welcome back, ").append(newUser.getUsername()).append(".");
                    textForResponseBuilder.append(TIP1).append(TIP2);
                    textForResponse = textForResponseBuilder.toString();
                    takeTokenForUser(newUser.getUsername());

                } else {
                    textForResponse = "Sorry, service is temporary unavailable.";
                    break;
                }
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId, command.getPreviousPageInfo());
                break;
            case GETGAME:
                String gameId = command.getId();
                String urlName = "games/" + gameId;

                JSONObject jsonFromURL = htmlService.getJsonFromURL(urlName, userUniqueName);

                textForResponse = JsonCustomParser.getGameResponse(jsonFromURL);
                keyboardForResponse = menuService.getGameMenu(jsonFromURL, userId, command.getPreviousPageInfo());
                break;
            case GETGAMES:
                StringBuilder getGamesUrl = new StringBuilder();
                getGamesUrl.append(GAMES);
                getGamesUrl.append(PAGE).append(command.getCurPage());

                JSONObject responseJson = htmlService.getJsonFromURL(getGamesUrl.toString(), userUniqueName);

                textForResponse = JsonCustomParser.getListResponse(responseJson);
                keyboardForResponse = menuService.getListMenu(responseJson, Commands.GETGAMES, command.getCurPage(),
                        0);
                break;
            case GREETINGS:
                StringBuilder messageForResponseBuilder = new StringBuilder();
                messageForResponseBuilder.append("Hello, ").append(update.getCallbackQuery().getFrom().getFirstName())
                        .append(" :)");
                messageForResponseBuilder.append(TIP1);
                messageForResponseBuilder.append(TIP2);
                textForResponse = messageForResponseBuilder.toString();
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId, command.getPreviousPageInfo());
                break;
            case GETWL:
                StringBuilder getWishListUrl = new StringBuilder();
                getWishListUrl.append(GAMES);
                getWishListUrl.append(PAGE).append(command.getCurPage());

                getWishListUrl.append("&filter=").append("userId=").append(userId);

                JSONObject getWishListJson = htmlService.getJsonFromURL(getWishListUrl.toString(), userUniqueName);
                textForResponse = JsonCustomParser.getListResponse(getWishListJson);
                keyboardForResponse = menuService.getListMenu(getWishListJson, Commands.GETWL, command.getCurPage(),
                        userId);
                break;
            case ADDTOWL:
                String addToWishListUrl = "users/" + command.getId();
                textForResponse = htmlService.addToWishList(addToWishListUrl, userUniqueName);
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId, command.getPreviousPageInfo());
                break;
            case CLEARWL:
                String clearWishListUrl = "users/" + command.getId();
                textForResponse = htmlService.deleteFromWishList(clearWishListUrl, userUniqueName);
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId, command.getPreviousPageInfo());
                break;
            case SEARCH:
                StringBuilder searchByName = new StringBuilder();
                searchByName.append(GAMES);
                searchByName.append(PAGE).append(command.getCurPage());
                searchByName.append("&filter=").append("name=").append(command.getId());

                JSONObject getGameByNameJson = htmlService.getJsonFromURL(searchByName.toString(), userUniqueName);
                textForResponse = JsonCustomParser.getListResponse(getGameByNameJson);
                keyboardForResponse = menuService.getListMenu(getGameByNameJson, Commands.SEARCH, command.getCurPage(),
                        command.getId());
                break;
            default:
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId, command.getPreviousPageInfo());
        }

        try {
            if (responseMessage != null) {
                responseMessage.setText(textForResponse);
                responseMessage.setReplyMarkup(keyboardForResponse);
                execute(responseMessage);
            } else {
                editMessage.setText(textForResponse);
                editMessage.setReplyMarkup(keyboardForResponse);
                execute(editMessage);
            }
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void takeTokenForUser(String username) {
        htmlService.getTokenFromService(username);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}