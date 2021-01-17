package com.onyshchenko.psabot.service;

import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.dto.GameDto;
import com.onyshchenko.psabot.services.parser.JsonContentParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonParserTest {

    private static final JsonContentParser jsonParser = new JsonContentParser();

    @Test
    void testJsonParserList() throws IOException, JSONException {
        String path = "/Users/oleksiionyshchenko/Documents/Projects/psabot/src/test/java/resources/service/jsonparser/GetGameListServerResponse";
        String json = new String(Files.readAllBytes(Paths.get(path)));

        JSONObject object = new JSONObject(json);

        ResponseBody responseBody = jsonParser.parseResponseBody(json);

        assertEquals(447, object.get("totalPages"));

        List<GameDto> listOfGames = responseBody.getGameList();

        assertEquals(10, listOfGames.size());

        GameDto secondGame = listOfGames.get(1);
        assertEquals("Little Nightmares II PS4 & PS5", secondGame.getName());
        assertEquals("BANDAI NAMCO ENTERTAINMENT EUROPE", secondGame.getPublisher());
        assertEquals("EP0700-CUSA12779_00-LITTLENIGHTS2000", secondGame.getUrl());
        assertEquals("949", secondGame.getPrice().getCurrentPrice());
        assertEquals("1000", secondGame.getPrice().getHighestPrice());
        assertEquals("0", secondGame.getPrice().getCurrentDiscount());
        assertEquals("10", secondGame.getPrice().getHighestDiscount());
        assertEquals("949", secondGame.getPrice().getLowestPrice());


    }

    @Test
    void testJsonParserGame() throws IOException {
        String path = "/Users/oleksiionyshchenko/Documents/Projects/psabot/src/test/java/resources/service/jsonparser/GetGameFromServerResponse";
        String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

        ResponseBody responseBody = jsonParser.parseResponseBody(json);
        List<GameDto> listOfGames = responseBody.getGameList();

        assertEquals(1, listOfGames.size());

        GameDto game = listOfGames.get(0);

        assertEquals("Persona®5 Strikers", game.getName());
        assertEquals("SEGA EUROPE LTD", game.getPublisher());
        assertEquals("1799", game.getPrice().getCurrentPrice());
        assertEquals("2000", game.getPrice().getHighestPrice());
        assertEquals("50", game.getPrice().getCurrentDiscount());
        assertEquals("100", game.getPrice().getHighestDiscount());
        assertEquals("1400", game.getPrice().getLowestPrice());
    }
}
