package com.onyshchenko.psabot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onyshchenko.psabot.models.CommandLine;
import com.onyshchenko.psabot.models.Commands;
import org.springframework.stereotype.Component;

@Component
public class CommandService {

    public CommandLine prepareCommandFromRequest(String data) {

        CommandLine commandLine;
        if (data.equalsIgnoreCase("/start")) {
            return new CommandLine(Commands.REGISTERUSER);
        }
        try {
            commandLine = new ObjectMapper().readValue(data, CommandLine.class);
        } catch (JsonProcessingException e) {
            commandLine = new CommandLine(Commands.REGULARREPLY);
        }

        return commandLine;
    }
}
