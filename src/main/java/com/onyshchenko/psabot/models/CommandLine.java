package com.onyshchenko.psabot.models;

public class CommandLine {

    private Commands cmd;
    private String id;
    private int curPage;
    private int prevPage;
    private int nextPage;

    public CommandLine() {
    }

    public CommandLine(Commands cmd) {
        this.cmd = cmd;
    }

    public CommandLine(Commands cmd, String id) {
        this.cmd = cmd;
        this.id = id;
    }

    public Commands getCmd() {
        return cmd;
    }

    public void setCmd(Commands cmd) {
        this.cmd = cmd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
