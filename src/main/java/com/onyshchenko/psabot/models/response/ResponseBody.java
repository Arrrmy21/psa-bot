package com.onyshchenko.psabot.models.response;

import com.onyshchenko.psabot.models.response.dto.GameDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResponseBody {

    private List<GameDto> gameList;
    private int totalPages;
    private boolean currentNotificationStatus;
    private String uniqueText;

    public ResponseBody() {
        this.gameList = new ArrayList<>();
    }

    public ResponseBody(GameDto[] listOfGames, int totalPages) {
        this.totalPages = totalPages;
        this.gameList = Arrays.stream(listOfGames).collect(Collectors.toList());
    }

    public ResponseBody(GameDto[] listOfGames) {
        this.gameList = Arrays.stream(listOfGames).collect(Collectors.toList());
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<GameDto> getGameList() {
        if (gameList == null) {
            this.gameList = new ArrayList<>(10);
        }
        return gameList;
    }

    public void setGameList(List<GameDto> gameList) {
        this.gameList = gameList;
    }

    public boolean isCurrentNotificationStatus() {
        return currentNotificationStatus;
    }

    public void setCurrentNotificationStatus(boolean currentNotificationStatus) {
        this.currentNotificationStatus = currentNotificationStatus;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getUniqueText() {
        return uniqueText;
    }

    public void setUniqueText(String uniqueText) {
        this.uniqueText = uniqueText;
    }
}
