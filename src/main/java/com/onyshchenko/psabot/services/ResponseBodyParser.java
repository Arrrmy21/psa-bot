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
        } else if (serverResponse.getTextResponse().equals(StandardResponse.VERSION_ERROR)) {

            return StandardResponse.VERSION_ERROR.getTextResponse();
        } else {
            return serverResponse.getTextResponse().getTextResponse();
        }
    }

    private static String getGameResponse(ResponseBody responseBody) {

        GameDto game = responseBody.getGameList().get(0);
        GameDto.Price price = game.getPrice();

        StringBuilder gameResponseBuilder = new StringBuilder(game.getName())
                .append("\n\nCurrent price: ").append(price.getCurrentPrice())
                .append("\nCurrent discount: ").append(price.getCurrentPercentageDiscount()).append("%");

        if (price.getCurrentPsPlusPrice() != price.getCurrentPrice()) {
            gameResponseBuilder.append("\nPrice with PS Plus: ").append(price.getCurrentPsPlusPrice());
        }
        gameResponseBuilder.append("\n\nRelease date: ").append(game.getReleaseDate())
                .append("\nLowest price: ").append(price.getLowestPrice())
                .append("\nHighest price: ").append(price.getHighestPrice())
                .append("\n\nPublisher: ").append(game.getPublisher().getName())
                .append("\nGenres: ").append(game.getGenres()).append("\n");
        if (game.isEaAccess()) {
            gameResponseBuilder.append("\nGame included in EA Access subscription");
        }
        if (game.isExclusive()) {
            gameResponseBuilder.append("\nGame is exclusive for PS platform");
        }

        return gameResponseBuilder.toString();
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
            sb.append(game.getName());

            sb.append("\nPrice: ").append(price.getCurrentPrice());
            sb.append("\nCurrent discount: ").append(price.getCurrentPercentageDiscount()).append("%");
            sb.append("\nCategory: ").append(game.getCategory().toLowerCase()).append("\n\n");
            position++;
        }

        return sb.toString();
    }

}
