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

    public Commands getCmd() {
        return cmd;
    }


    public String getId() {
        return id;
    }


    public int getCurPage() {
        return curPage;
    }


}
