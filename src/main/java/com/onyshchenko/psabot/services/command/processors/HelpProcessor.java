package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class HelpProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        mainServerService.registerUser(userUpdateData.getUser());

        return new ServerResponse(StandardResponse.VERSION_ERROR);
    }
}
