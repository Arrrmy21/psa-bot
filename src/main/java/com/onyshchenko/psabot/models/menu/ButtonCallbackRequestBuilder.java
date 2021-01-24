package com.onyshchenko.psabot.models.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonCallbackRequestBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonCallbackRequestBuilder.class);

    private final StringBuilder stringBuilder;
    private static final String CMD = "cmd:";
    private static final String CURRENT_PAGE = ",curPage:";
    private static final String PREVIOUS_PAGE = ",prevPage:";
    private static final String FILTER = ",f:";
    private static final String ID = ",id:";
    private static final String ADD = ",add:";
    private static final String VERSION = ",v:";

    public ButtonCallbackRequestBuilder() {
        stringBuilder = new StringBuilder();
    }

    public ButtonCallbackRequestBuilder addCommand(int command) {
        this.stringBuilder.append(CMD).append(command);
        return this;
    }

    public ButtonCallbackRequestBuilder addCurrentPage(String curPage) {
        this.stringBuilder.append(CURRENT_PAGE).append(curPage);
        return this;
    }

    public ButtonCallbackRequestBuilder addPreviousPage(String prevPage) {
        this.stringBuilder.append(PREVIOUS_PAGE).append(prevPage);
        return this;
    }

    public ButtonCallbackRequestBuilder addFilter(int filter) {
        this.stringBuilder.append(FILTER).append(filter);
        return this;
    }

    public ButtonCallbackRequestBuilder addId(String id) {
        this.stringBuilder.append(ID).append(id);
        return this;
    }

    public ButtonCallbackRequestBuilder addAdditionalParams(Object add) {
        this.stringBuilder.append(ADD).append(add);
        return this;
    }

    public ButtonCallbackRequestBuilder addVersion(String version) {
        this.stringBuilder.append(VERSION).append(version);
        return this;
    }

    public String buildRequest() {
        String buttonCallBack = stringBuilder.toString();
        int callbackLength = buttonCallBack.length();
        if (callbackLength > 64) {
            LOGGER.info("Prohibited callback length [{}].", callbackLength);
            LOGGER.info("Callback data: [{}]", buttonCallBack);
        }
        return buttonCallBack;
    }
}
