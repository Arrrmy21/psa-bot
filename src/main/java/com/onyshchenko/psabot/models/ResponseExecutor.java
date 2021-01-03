package com.onyshchenko.psabot.models;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class ResponseExecutor {

    private SendMessage responseMessage;
    private EditMessageText editMessage;

    private UserUpdateData userUpdateData;
    private CommandLine command;

    private InlineKeyboardMarkup keyboardForResponse;
    private String textForReply = "Default reply";


    private ResponseExecutor() {

    }

    private void prepareMethodToExecute() {

        if (command.getCommand().equals(Commands.REGISTERUSER)
                || command.getCommand().equals(Commands.SEARCH) || command.getCommand().equals(Commands.GETWL)
                || command.getCommand().equals(Commands.GETGAME)) {
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


    public BotApiMethod<?> getMethodToExecute() {
        prepareMethodToExecute();
        return responseMessage != null ? responseMessage : editMessage;
    }


    public class ResponseExecutorBuilder {

        public ResponseExecutorBuilder addCommand(CommandLine commandLine) {
            ResponseExecutor.this.command = commandLine;
            return this;
        }

        public ResponseExecutorBuilder addTextForReply(String textForReply){
            ResponseExecutor.this.textForReply = textForReply;
            return this;
        }

        public ResponseExecutorBuilder addInlineKeyboard(InlineKeyboardMarkup inlineKeyboardMarkup){
            ResponseExecutor.this.keyboardForResponse = inlineKeyboardMarkup;
            return this;
        }

        public ResponseExecutor build() {
            return ResponseExecutor.this;
        }

        public ResponseExecutorBuilder addUserInfo(UserUpdateData userUpdateData) {
            ResponseExecutor.this.userUpdateData = userUpdateData;

            return this;
        }

    }
}
