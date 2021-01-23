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

    public UserRequest prepareUserRequestFromRequestData(String data) {

        UserRequest userRequest;
        if (data.equalsIgnoreCase("/start")) {
            return new UserRequest(Command.REGISTERUSER);
        } else if (data.equalsIgnoreCase("/wishlist")) {
            return new UserRequest((Command.GETWL));
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
            userRequest = new ObjectMapper().readValue(data, UserRequest.class);

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

    private void redirectUserToMainMenu(UserRequest requestFromUser) {

        requestFromUser.setCommand(Command.HELP);
    }
}
