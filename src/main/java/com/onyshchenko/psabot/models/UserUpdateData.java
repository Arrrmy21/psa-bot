package com.onyshchenko.psabot.models;

import org.telegram.telegrambots.meta.api.objects.Update;

public class UserUpdateData {

    private final int userId;
    private final long chatId;
    private final String requestData;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final Integer messageId;

    public UserUpdateData(Update update) {
        if (update.hasMessage() && update.getMessage().getText() != null) {
            this.userId = update.getMessage().getFrom().getId();
            this.requestData = update.getMessage().getText();
            this.chatId = update.getMessage().getChatId();
            this.userName = update.getMessage().getFrom().getUserName();
            this.messageId = null;
            this.firstName = update.getMessage().getFrom().getFirstName();
            this.lastName = update.getMessage().getFrom().getLastName();
        } else {
            this.userId = update.getCallbackQuery().getFrom().getId();
            this.userName = update.getCallbackQuery().getFrom().getUserName();
            this.requestData = update.getCallbackQuery().getData();
            this.chatId = update.getCallbackQuery().getMessage().getChatId();
            this.messageId = update.getCallbackQuery().getMessage().getMessageId();
            this.firstName = update.getCallbackQuery().getFrom().getFirstName();
            this.lastName = update.getCallbackQuery().getFrom().getLastName();
        }
    }

    public int getUserId() {
        return userId;
    }

    public long getChatId() {
        return chatId;
    }

    public String getRequestData() {
        return requestData;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNameToAddress() {
        return getFirstName() != null ? getFirstName() : getAlternateAddressName();
    }

    private String getAlternateAddressName() {
        return getLastName() != null ? getLastName() : getUserName();
    }

    public User getUser() {
        User user = new User();

        user.setUserId(getUserId());
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setUsername(getUserName());
        user.setChatId(getChatId());

        return user;
    }
}
