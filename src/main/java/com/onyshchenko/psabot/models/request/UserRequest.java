package com.onyshchenko.psabot.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onyshchenko.psabot.models.common.Command;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

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
    private Set<Filter> filter;
    @JsonProperty("v")
    private String version;

    public UserRequest() {
    }

    public UserRequest(Command cmd) {
        this.command = cmd;
    }

    public UserRequest(Command cmd, String id, String version) {
        this.command = cmd;
        this.idToPass = id;
        this.version = version;
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

    public void setFilter(Set<Filter> filter) {
        this.filter = filter;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Filter> getFilters() {
        return filter;
    }

    public List<Integer> getFilterIds() {
        return filter.stream().map(Filter::getFilterId).collect(Collectors.toList());
    }


    public enum Filter {

        DISCOUNT_FILTER("disc%3E00", 0),
        FREE_GAMES_FILTER("price=00", 1),
        GAMES_FILTER("category=games", 2),
        OTHER_PRODUCTS_FILTER("category=otherProducts", 3),
        PS_PLUS("psplus=true", 4),
        EA_ACCESS("eaaccess=true", 5),
        EXCLUSIVE("exclusive=true", 6),
        PUBLISHER("publisher=%s", 7),
        PUBLISHER_ID("publisherId=%s", 8);

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
