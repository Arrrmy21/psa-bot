package com.onyshchenko.psabot;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.services.ResponseExecutor;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    @Autowired
    private CommandService commandService;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.userName}")
    private String username;

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received update message. Id: [{}]", update.getUpdateId());
        UserUpdateData userUpdateData = new UserUpdateData(update);

        UserRequest requestFromUser = commandService
                .prepareUserRequestFromRequestData(userUpdateData.getRequestData());

        ResponseExecutor.ResponseExecutorBuilder responseExecutorBuilder = ResponseExecutor.newBuilder();
        responseExecutorBuilder.addUserRequest(requestFromUser);
        responseExecutorBuilder.addUserInfo(userUpdateData);

        ResponseExecutor responseExecutor = responseExecutorBuilder.startProcessOfPreparingResponse();

        try {
            execute(responseExecutor.getMethodToExecute());
        } catch (TelegramApiException e) {
            LOGGER.info("Telegram API Exception.");
            e.printStackTrace();
        } catch (Exception ex) {
            LOGGER.info("Unknown exception occurred.");
            ex.printStackTrace();
        }
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