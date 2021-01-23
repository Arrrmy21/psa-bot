package com.onyshchenko.psabot.models.menu;

public class ButtonCallbackRequestBuilder {

    private final StringBuilder stringBuilder;
    private static final String CMD = "\"cmd\":\"";
    private static final String CURRENT_PAGE = ",\"curPage\":\"";
    private static final String PREVIOUS_PAGE = ",\"prevPage\":\"";
    private static final String FILTER = ",\"f\":\"";
    private static final String ID = ",\"id\":\"";
    private static final String ADD = ",\"add\":\"";
    private static final String VERSION = ",\"v\":\"";

    public ButtonCallbackRequestBuilder() {
        stringBuilder = new StringBuilder("{");
    }

    public ButtonCallbackRequestBuilder addCommand(int command) {
        this.stringBuilder.append(CMD).append(command).append("\"");
        return this;
    }

    public ButtonCallbackRequestBuilder addCurrentPage(String curPage) {
        this.stringBuilder.append(CURRENT_PAGE).append(curPage).append("\"");
        return this;
    }

    public ButtonCallbackRequestBuilder addPreviousPage(String prevPage) {
        this.stringBuilder.append(PREVIOUS_PAGE).append(prevPage).append("\"");
        return this;
    }

    public ButtonCallbackRequestBuilder addFilter(int filter) {
        this.stringBuilder.append(FILTER).append(filter).append("\"");
        return this;
    }

    public ButtonCallbackRequestBuilder addId(String id) {
        this.stringBuilder.append(ID).append(id).append("\"");
        return this;
    }

    public ButtonCallbackRequestBuilder addAdditionalParams(Object add) {
        this.stringBuilder.append(ADD).append(add).append("\"");
        return this;
    }

    public ButtonCallbackRequestBuilder addVersion(String version) {
        this.stringBuilder.append(VERSION).append(version).append("\"");
        return this;
    }

    public String buildRequest() {
        this.stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
