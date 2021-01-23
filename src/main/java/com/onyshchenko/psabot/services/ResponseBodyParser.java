package com.onyshchenko.psabot.services;

import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.response.dto.GameDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseBodyParser {

    private ResponseBodyParser() {
    }

    public static String convertServerResponseToText(ServerResponse serverResponse) {

        if (serverResponse.getTextResponse().equals(StandardResponse.GET_GAME_RESPONSE)) {
            return getGameResponse(serverResponse.getResponseBody());
        } else if (serverResponse.getTextResponse().equals(StandardResponse.GET_GAMES_RESPONSE)
                || serverResponse.getTextResponse().equals(StandardResponse.GET_WISHLIST)
                || serverResponse.getTextResponse().equals(StandardResponse.SEARCH_GAMES)) {
            return getListResponse(serverResponse.getResponseBody());
        } else if (serverResponse.getTextResponse().equals(StandardResponse.GAME_ADDED_TO_WL) ||
                serverResponse.getTextResponse().equals(StandardResponse.GAME_DELETED_FROM_WL)) {
            return serverResponse.getResponseBody().getUniqueText();
        } else if (serverResponse.getTextResponse().equals(StandardResponse.GREETINGS)) {
            return StandardResponse.GREETINGS.getTextResponse()
                    .replace("{}", serverResponse.getResponseBody().getUniqueText());
        } else if (serverResponse.getTextResponse().equals(StandardResponse.WELCOME)) {
            return StandardResponse.WELCOME.getTextResponse()
                    .replace("{}", serverResponse.getResponseBody().getUniqueText());
        } else if (serverResponse.getTextResponse().equals(StandardResponse.WELCOME_BACK)) {
            return StandardResponse.WELCOME_BACK.getTextResponse()
                    .replace("{}", serverResponse.getResponseBody().getUniqueText());
        } else {
            return serverResponse.getTextResponse().getTextResponse();
        }
    }

    private static String getGameResponse(ResponseBody responseBody) {

        GameDto game = responseBody.getGameList().get(0);
        GameDto.Price price = game.getPrice();

        return game.getName() + "\n\n" +
                "Current price: " + price.getCurrentPrice() + "\n" +
                "Current discount: " + price.getCurrentDiscount() + "\n\n" +
                "Release date: " + game.getReleaseDate() + "\n" +
                "Lowest price: " + price.getLowestPrice() + "\n" +
                "Highest price: " + price.getHighestPrice() + "\n" +
                "Publisher: " + game.getPublisher() + "\n";
    }

    private static String getListResponse(ResponseBody responseBody) {

        List<GameDto> gameList = responseBody.getGameList();

        if (gameList.isEmpty()) {
            return "There are no games in list";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(".!.                   List of games                   .!.\n");
        int position = 1;
        for (GameDto game : gameList) {
            GameDto.Price price = game.getPrice();
            sb.append(position).append(") ");
            sb.append(game.getName()).append("\nPrice: ");
            sb.append(price.getCurrentPrice()).append("\nCurrent discount: ");
            sb.append(price.getCurrentDiscount()).append("\nHighest discount: ");
            sb.append(price.getHighestDiscount()).append("\n\n");
            position++;
        }

        return sb.toString();
    }

}
