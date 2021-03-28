package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.common.Command;
import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class GamesMenuProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest userRequest) {

        if (userRequest.getCommand().equals(Command.GAMESMENU)) {
            return new ServerResponse(StandardResponse.GAMES_MENU);
        } else {
            return new ServerResponse(StandardResponse.OTHER_PRODUCTS_MENU);
        }
    }
}
