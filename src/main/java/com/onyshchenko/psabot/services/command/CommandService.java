package com.onyshchenko.psabot.services.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.common.Command;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CommandService {

    @Value("${bot.version}")
    private String version;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandService.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public UserRequest prepareUserRequestFromRequestData(String data) {

        UserRequest userRequest;
        if (data.equalsIgnoreCase("/start")) {
            userRequest = new UserRequest(Command.REGISTERUSER);
            userRequest.setVersion(version);

            return userRequest;
        } else if (data.equalsIgnoreCase("/wishlist")) {
            userRequest = new UserRequest((Command.GETWL));
            userRequest.setVersion(version);

            return userRequest;
        } else if (data.contains("Name:") || data.contains("name:")) {
            String nameOfGame = data.substring(5);
            while (nameOfGame.startsWith(" ")) {
                nameOfGame = nameOfGame.substring(1);
            }

            String encodedUrl = null;
            try {
                encodedUrl = URLEncoder.encode(nameOfGame, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("Failed to encode name for filter");
            }

            return new UserRequest(Command.SEARCH, encodedUrl, version);
        }
        try {
            boolean isDataNew = validateRequestIsNotDeprecatedJson(data);
            if (!isDataNew) {
                UserRequest request = new UserRequest(Command.HELP);
                request.setVersion(version);

                return request;
            }
            String convertedData = convertStringDataToJson(data);
            userRequest = objectMapper.readValue(convertedData, UserRequest.class);

            if (userRequest.getVersion() == null || !userRequest.getVersion().equalsIgnoreCase(version)) {
                LOGGER.info("Version of user's keyboard differs from server's.");
                userRequest.setVersion(version);
                redirectUserToMainMenu(userRequest);
            }
        } catch (JsonProcessingException e) {
            userRequest = new UserRequest(Command.REGULAR_REPLY);
        }

        return userRequest;
    }

    private boolean validateRequestIsNotDeprecatedJson(String data) {
        try {
            new JSONObject(data);
            return false;
        } catch (JSONException ex) {
            return true;
        }
    }

    private String convertStringDataToJson(String data) {
        StringBuilder sb = new StringBuilder("{");
        String[] array = data.split(",");
        int filterCount = 0;
        StringBuilder filters = new StringBuilder("\"f\":[");
        for (int keyPair = 0; keyPair < array.length; keyPair++) {
            String value = array[keyPair];
            String[] keyValue = value.split(":");
            if (keyValue[0].equalsIgnoreCase("f")) {
                if (filterCount > 0) {
                    filters.append(",");
                }
                filters.append("\"").append(keyValue[1]).append("\"");
                filterCount++;
                continue;
            }
            sb.append("\"").append(keyValue[0]).append("\"")
                    .append(":")
                    .append("\"").append(keyValue[1]).append("\"");
            if (keyPair != array.length - 1) {
                sb.append(",");
            }
        }
        if (filterCount > 0) {
            filters.append("]");
            if (sb.charAt(sb.length() - 1) != ',') {
                sb.append(",");
            }
            sb.append(filters.toString());
        }
        return sb.append("}").toString();
    }

    private void redirectUserToMainMenu(UserRequest requestFromUser) {

        requestFromUser.setCommand(Command.HELP);
    }
}
