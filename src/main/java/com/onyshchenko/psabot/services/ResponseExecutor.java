package com.onyshchenko.psabot.services;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.common.Command;
import com.onyshchenko.psabot.services.command.factory.CommandProcessorFactory;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.menu.common.MenuProvider;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class ResponseExecutor {

    private SendMessage responseMessage;
    private EditMessageText editMessage;

    private UserUpdateData userUpdateData;
    private UserRequest userRequest;

    private InlineKeyboardMarkup keyboardForResponse;
    private String textForReply = "Default reply";


    private ResponseExecutor() {
    }

    public BotApiMethod<?> getMethodToExecute() {
        prepareMethodToExecute();
        return responseMessage != null ? responseMessage : editMessage;
    }

    private void prepareMethodToExecute() {

        if (userRequest.getCommand().equals(Command.REGISTERUSER)
                || userRequest.getCommand().equals(Command.SEARCH) || userRequest.getCommand().equals(Command.GETWL)
                || userRequest.getCommand().equals(Command.GETGAME)) {
            responseMessage = new SendMessage();
            responseMessage.setChatId(userUpdateData.getChatId());
            responseMessage.setText(textForReply);
            responseMessage.setReplyMarkup(keyboardForResponse);

        } else {
            editMessage = new EditMessageText();
            editMessage.setChatId(userUpdateData.getChatId());
            editMessage.setMessageId(userUpdateData.getMessageId());
            editMessage.setText(textForReply);
            editMessage.setReplyMarkup(keyboardForResponse);

        }
    }

    public static ResponseExecutorBuilder newBuilder() {
        return new ResponseExecutor().new ResponseExecutorBuilder();
    }


    public class ResponseExecutorBuilder {

        public ResponseExecutor startProcessOfPreparingResponse() {

            CommandProcessor processor = CommandProcessorFactory.getProcessorForExactCommand(userRequest.getCommand());
            ServerResponse serverResponse = processor.getMainServerResponse(userUpdateData, userRequest);

            ResponseExecutor.this.textForReply = ResponseBodyParser.convertServerResponseToText(serverResponse);

            MenuProvider menuProvider = Command.getCommandRelatedMenu(userRequest.getCommand());
            keyboardForResponse = menuProvider.prepareMenu(
                    serverResponse.getResponseBody(), userUpdateData, userRequest);

            return ResponseExecutor.this;
        }

        public void addUserRequest(UserRequest commandLine) {
            ResponseExecutor.this.userRequest = commandLine;
        }

        public void addUserInfo(UserUpdateData userUpdateData) {
            ResponseExecutor.this.userUpdateData = userUpdateData;
        }
    }

}
