package com.onyshchenko.psabot.services.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.dto.GameDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JsonContentParser implements ServerResponseParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ResponseBody parseResponseBody(String data) {

        JSONObject jsonObject = new JSONObject(data);

        try {
            if (jsonObject.has("content")) {
                int totalPages = jsonObject.getInt("totalPages");
                JSONArray contentData = jsonObject.getJSONArray("content");
                GameDto[] listOfGames = objectMapper.readValue(contentData.toString(), GameDto[].class);

                return new ResponseBody(listOfGames, totalPages);
            } else {
                GameDto game = objectMapper.readValue(jsonObject.toString(), GameDto.class);
                GameDto[] newArray = new GameDto[]{game};

                return new ResponseBody(newArray);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseBody();
        }
    }

}
