package com.onyshchenko.psabot.services.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.common.Command;
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

    private String convertStringDataToJson(String data) {
        StringBuilder sb = new StringBuilder("{");
        String[] array = data.split(",");
        for (int keyPair = 0; keyPair < array.length; keyPair++) {
            String value = array[keyPair];
            String[] keyValue = value.split(":");
            sb.append("\"").append(keyValue[0]).append("\"")
                    .append(":")
                    .append("\"").append(keyValue[1]).append("\"");
            if (keyPair != array.length - 1) {
                sb.append(",");
            }
        }
        return sb.append("}").toString();
    }

    private void redirectUserToMainMenu(UserRequest requestFromUser) {

        requestFromUser.setCommand(Command.HELP);
    }
}
