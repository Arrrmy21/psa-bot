package com.onyshchenko.psabot.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JsonCustomParser {

    private JsonCustomParser() {
    }

    public static String getGameResponse(JSONObject jsonObject) {

        String name = jsonObject.getString("name");
        JSONObject price = (JSONObject) jsonObject.get("price");

        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n\n");
        sb.append("Current price: ").append(price.get("currentPrice")).append("\n");
        sb.append("Current discount: ").append(price.get("currentDiscount")).append("\n\n");
        sb.append("Release date: ").append(jsonObject.get("releaseDate")).append("\n");
        sb.append("Lowest price: ").append(price.get("lowestPrice")).append("\n");
        sb.append("Highest price: ").append(price.get("highestPrice")).append("\n");
        sb.append("Publisher: ").append(jsonObject.get("publisher")).append("\n");

        return sb.toString();
    }

    public static String getListResponse(JSONObject jsonObject) {

        StringBuilder sb = new StringBuilder();
        JSONArray gameList = jsonObject.getJSONArray("content");
        int totalObjects = gameList.length();

        sb.append(".!.                   List of games                   .!.\n");
        for (int i = 0; i < totalObjects; i++) {
            sb.append(i + 1).append(") ");
            JSONObject game = gameList.getJSONObject(i);
            String name = game.getString("name");
            sb.append(name).append("\nPrice: ");
            JSONObject price = (JSONObject) game.get("price");
            int currentPrice = (int) price.get("currentPrice");
            sb.append(currentPrice).append("\nCurrent discount: ");
            int currentDiscount = (int) price.get("currentDiscount");
            sb.append(currentDiscount).append("\nHighest discount: ");
            int highestDiscount = (int) price.get("highestDiscount");
            sb.append(highestDiscount).append("\n\n");
        }

        return sb.toString();
    }

}
