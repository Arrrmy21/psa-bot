package com.onyshchenko.psabot;

import com.onyshchenko.psabot.models.CommandLine;
import com.onyshchenko.psabot.models.ResponseExecutor;
import com.onyshchenko.psabot.models.ServerResponse;
import com.onyshchenko.psabot.models.UserUpdateData;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    private static final String GAMES = "games?";
    private static final String USERS = "users/";
    private static final String PAGE = "page=";
    private static final String ADD_FILTER = "&filter=";
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

        UserUpdateData userUpdateData = new UserUpdateData(update);
        LOGGER.info("Update has message from user: [{}]", userUpdateData.getUserId());

        InlineKeyboardMarkup keyboardForResponse = menuService.getMainMenuInlineKeyboard(userUpdateData.getUserId(), null);
        String textForResponse = "Error!";

        CommandLine command = commandService.prepareCommandFromRequest(userUpdateData.getRequestData());
        String textFromServer = "textFromServer: default";
        JSONObject jsonFromURL;


        switch (command.getCommand()) {
            case REGISTERUSER:
                if (userUpdateData.getUserName() == null) {
                    textForResponse = ServerResponse.USERNAME_ABSENCE.getTextResponse();
                    break;
                }

                String registerUserResponse = htmlService.registerUser(userUpdateData.getUser());
                StringBuilder textForResponseBuilder = new StringBuilder();
                if (registerUserResponse.equalsIgnoreCase(ServerResponse.USER_CREATED.getTextResponse())) {
                    textForResponseBuilder.append("Welcome, ").append(userUpdateData.getUserName()).append(".");
                    textForResponseBuilder.append(TIP1).append(TIP2);
                    textForResponse = textForResponseBuilder.toString();
                    takeTokenForUser(userUpdateData.getUserName());
                } else if (registerUserResponse.equalsIgnoreCase(ServerResponse.USER_EXIST.getTextResponse())) {
                    textForResponseBuilder.append("Welcome back, ").append(userUpdateData.getUserName()).append(".");
                    textForResponseBuilder.append(TIP1).append(TIP2);
                    textForResponse = textForResponseBuilder.toString();
                    takeTokenForUser(userUpdateData.getUserName());

                } else {
                    textForResponse = ServerResponse.SERVICE_UNAVAILABLE.getTextResponse();
                    break;
                }
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(
                        userUpdateData.getUserId(), command);
                break;
            case GETGAME:
                String gameId = command.getIdToPass();
                String urlName = "games/" + gameId;

                textFromServer = htmlService.getTextResponseFromURL(urlName, userUpdateData.getUserName());
                jsonFromURL = new JSONObject(textFromServer);

                textForResponse = JsonCustomParser.getGameResponse(jsonFromURL);
                keyboardForResponse = menuService.getGameMenu(jsonFromURL, userUpdateData.getUserId(), command);
                break;
            case GETGAMES:
                StringBuilder getGamesUrlBuilder = new StringBuilder(GAMES);
                getGamesUrlBuilder.append(PAGE).append(command.getCurPage());
                if (command.getFilter() != null) {
                    getGamesUrlBuilder.append(ADD_FILTER).append(command.getFilter().getFilterName());
                }
                String getGamesUrl = getGamesUrlBuilder.toString();
                try {
                    textFromServer = htmlService.getTextResponseFromURL(getGamesUrl, userUpdateData.getUserName());
                } catch (Exception ex) {
                    LOGGER.info("Exception occurred.");
                }
                if (!textFromServer.equals(ServerResponse.FAILED_RESPONSE.getTextResponse())) {
                    jsonFromURL = new JSONObject(textFromServer);
                    textForResponse = JsonCustomParser.getListResponse(jsonFromURL);
                    keyboardForResponse = menuService.getListMenu(jsonFromURL, command, 0);
                }
                break;
            case GREETINGS:
                textForResponse = "Hello, " + userUpdateData.getFirstName() +
                        " :)" +
                        TIP1 +
                        TIP2;
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(
                        userUpdateData.getUserId(), command);
                break;
            case GAMESMENU:
                textForResponse = "Games menu";
                keyboardForResponse = menuService.getGamesMenu();
                break;
            case SWITCH:
                String switchUserNotificationsUrl = USERS + userUpdateData.getUserId() + "/notifications/" + command.getIdToPass().toLowerCase();
                htmlService.getTextResponseFromURL(switchUserNotificationsUrl, userUpdateData.getUserName());

            case CABINET:
                String getUserNotificationsUrl = USERS + userUpdateData.getUserId() + "/notifications";
                textForResponse = htmlService.getTextResponseFromURL(getUserNotificationsUrl, userUpdateData.getUserName());

                String commandToSwitchNotificationStatus;
                if (textForResponse.equalsIgnoreCase(ServerResponse.NOTIFICATIONS_ON.getTextResponse())) {
                    commandToSwitchNotificationStatus = "OFF";
                } else {
                    commandToSwitchNotificationStatus = "ON";
                }
                textForResponse = "Daily notifications in case of sales on games in your wishlist.";
                keyboardForResponse = menuService.getCabinetMenu(userUpdateData.getUserId(), commandToSwitchNotificationStatus);
                break;
            case GETWL:
                String getWishListUrl = GAMES +
                        PAGE + command.getCurPage() +
                        ADD_FILTER + "userId=" + userUpdateData.getUserId();
                textFromServer = htmlService.getTextResponseFromURL(getWishListUrl, userUpdateData.getUserName());
                JSONObject getWishListJson = new JSONObject(textFromServer);

                textForResponse = JsonCustomParser.getListResponse(getWishListJson);
                keyboardForResponse = menuService.getListMenu(getWishListJson, command, userUpdateData.getUserId());
                break;
            case ADDTOWL:
                String addToWishListUrl = USERS + command.getIdToPass();
                textForResponse = htmlService.addToWishList(addToWishListUrl, userUpdateData.getUserName());
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userUpdateData.getUserId(), command);
                break;
            case CLEARWL:
                String clearWishListUrl = USERS + command.getIdToPass();
                textForResponse = htmlService.deleteFromWishList(clearWishListUrl, userUpdateData.getUserName());
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userUpdateData.getUserId(), command);
                break;
            case SEARCH:
                String searchByName = GAMES +
                        PAGE + command.getCurPage() +
                        ADD_FILTER + "name=" + command.getIdToPass();
                textFromServer = htmlService.getTextResponseFromURL(searchByName, userUpdateData.getUserName());
                JSONObject getGameByNameJson = new JSONObject(textFromServer);
                textForResponse = JsonCustomParser.getListResponse(getGameByNameJson);
                keyboardForResponse = menuService.getListMenu(getGameByNameJson, command, command.getIdToPass());
                break;
            default:
                keyboardForResponse = menuService.getMainMenuInlineKeyboard(userUpdateData.getUserId(), command);
        }

        ResponseExecutor responseExecutor = ResponseExecutor.newBuilder()
                .addCommand(command)
                .addUserInfo(userUpdateData)
                .addTextForReply(textForResponse)
                .addInlineKeyboard(keyboardForResponse)
                .build();

        try {
            execute(responseExecutor.getMethodToExecute());
        } catch (TelegramApiException e) {
            LOGGER.info("Telegram APU Exception.");
            e.printStackTrace();
        } catch (Exception ex) {
            LOGGER.info("Unknown exception occurred.");
            ex.printStackTrace();
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