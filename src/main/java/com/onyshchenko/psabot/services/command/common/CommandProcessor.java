package com.onyshchenko.psabot.services.command.common;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.domainserver.DomainServiceInterface;
import com.onyshchenko.psabot.services.parser.ServerResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class CommandProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommandProcessor.class);

    @Autowired
    protected DomainServiceInterface mainServerService;
    @Autowired
    protected ServerResponseParser serverResponseParser;

    public static final String GAMES = "games";
    public static final String USERS = "users/";
    public static final String PAGE = "page=";
    public static final String ADD_FILTER = "&filter=";

    public abstract ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine);

}
