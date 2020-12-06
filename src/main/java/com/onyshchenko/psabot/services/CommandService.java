package com.onyshchenko.psabot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onyshchenko.psabot.models.CommandLine;
import com.onyshchenko.psabot.models.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandService.class);

    public CommandLine prepareCommandFromRequest(String data) {

        CommandLine commandLine;
        if (data.equalsIgnoreCase("/start")) {
            return new CommandLine(Commands.REGISTERUSER);
        } else if (data.equalsIgnoreCase("/wishlist")) {
            return new CommandLine((Commands.GETWL));
        } else if (data.contains("Name: ") || data.contains("name: ")) {
            String nameOfGame = data.substring(6);
            String encodedUrl = null;
            try {
                encodedUrl = URLEncoder.encode(nameOfGame, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("Failed to encode name for filter");
            }

            return new CommandLine(Commands.SEARCH, encodedUrl);
        }
        try {
            commandLine = new ObjectMapper().readValue(data, CommandLine.class);
        } catch (JsonProcessingException e) {
            commandLine = new CommandLine(Commands.REGULARREPLY);
        }

        return commandLine;
    }
}
