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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@PropertySource("classpath:bot.properties")
public class Bot extends TelegramLongPollingBot {

    private Logger logger = LoggerFactory.getLogger(Bot.class);

    @Autowired
    private CommandService commandService = new CommandService();

    @Autowired
    private HtmlService htmlService = new HtmlService();

    @Autowired
    private MenuService menuService = new MenuService();

    @Value("${bot.token}")
    private String TOKEN;

    @Value("${bot.userName}")
    private String USERNAME;

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Received update message. Id:  " + update.getUpdateId());
        CommandLine command;
        int userId;
        long chatId;
        String requestData;
        SendMessage responseMessage = null;
        EditMessageText editMessage = null;
        InlineKeyboardMarkup keyboardForResponse;

        if (update.hasMessage() && update.getMessage().getText() != null) {
            userId = update.getMessage().getFrom().getId();
            requestData = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            logger.info("Update has message from user: " + userId);
        } else {
            userId = update.getCallbackQuery().getFrom().getId();
            requestData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            logger.info("Update has callback form user: " + userId);
        }
        String textForResponse = "Default reply";
        command = commandService.prepareCommandFromRequest(requestData);

        if (command.getCmd().equals(Commands.GETGAME) || command.getCmd().equals(Commands.REGISTERUSER)) {
            responseMessage = new SendMessage();
            responseMessage.setChatId(chatId);
        } else {
            editMessage = new EditMessageText();
            editMessage.setChatId(chatId);
            editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        }

        switch (command.getCmd()) {
            case REGISTERUSER:
                User newUser = new User();
                newUser.setUserId(update.getMessage().getFrom().getId());
                newUser.setFirstName(update.getMessage().getFrom().getFirstName());
                newUser.setUserName(update.getMessage().getFrom().getUserName());
                newUser.setChatId(chatId);

                textForResponse = htmlService.registerUser(newUser);
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId);
                break;
            case GETGAME:
                responseMessage = new SendMessage();
                responseMessage.setChatId(chatId);

                String gameId = command.getId();
                String urlName = "games/" + gameId;

                JSONObject jsonFromURL = htmlService.getJsonFromURL(urlName);


                textForResponse = JsonCustomParser.getGameResponse(jsonFromURL);
                keyboardForResponse = menuService.getGameMenu(jsonFromURL, userId);
                break;
            case GETGAMES:
                StringBuilder getGamesUrl = new StringBuilder();
                getGamesUrl.append("games?");
                getGamesUrl.append("page=").append(command.getCurPage());

                JSONObject responseJson = htmlService.getJsonFromURL(getGamesUrl.toString());

                textForResponse = JsonCustomParser.getListResponse(responseJson);
                keyboardForResponse = menuService.getListMenu(responseJson, Commands.GETGAMES, command.getCurPage(), 0);
                break;
            case GREETINGS:
                textForResponse = "Hello, " + update.getCallbackQuery().getFrom().getFirstName() + "!";
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId);
                break;
            case GETWL:
                StringBuilder getWishListUrl = new StringBuilder();
                getWishListUrl.append("games?");
                getWishListUrl.append("page=").append(command.getCurPage());
                getWishListUrl.append("&filter=").append("userId=").append(command.getId());

                JSONObject getWishListJson = htmlService.getJsonFromURL(getWishListUrl.toString());
                textForResponse = JsonCustomParser.getListResponse(getWishListJson);
                keyboardForResponse = menuService.getListMenu(getWishListJson, Commands.GETWL, command.getCurPage(), userId);
                break;
            case ADDTOWISHLIST:
                String addToWishListUrl = "users/" + command.getId();
                textForResponse = htmlService.addToWishList(addToWishListUrl);
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId);
                break;
            default:
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userId);
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

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}