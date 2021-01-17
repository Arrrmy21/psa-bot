package com.onyshchenko.psabot.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onyshchenko.psabot.models.common.Command;

public class UserRequest {

    @JsonProperty("cmd")
    private Command command;
    @JsonProperty("id")
    private String idToPass;
    private int curPage;
    private int prevPage;
    @JsonProperty("add")
    private String previousPageInfo;
    @JsonProperty("f")
    private Filter filter;

    public UserRequest() {
    }

    public UserRequest(Command cmd) {
        this.command = cmd;
    }

    public UserRequest(Command cmd, String id) {
        this.command = cmd;
        this.idToPass = id;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getIdToPass() {
        return idToPass;
    }

    public void setIdToPass(String idToPass) {
        this.idToPass = idToPass;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public String getPreviousPageInfo() {
        return previousPageInfo;
    }

    public void setPreviousPageInfo(String previousPageInfo) {
        this.previousPageInfo = previousPageInfo;
    }

    public Filter getFilter() {
        return filter;
    }

    public enum Filter {

        DISCOUNT_FILTER("disc%3E00", 0),
        FREE_GAMES_FILTER("price=00", 1);

        private final String filterName;
        private final int filterId;

        Filter(String filterName, int filterId) {
            this.filterName = filterName;
            this.filterId = filterId;
        }

        public int getFilterId() {
            return filterId;
        }

        public String getFilterName() {
            return filterName;
        }
    }
}
