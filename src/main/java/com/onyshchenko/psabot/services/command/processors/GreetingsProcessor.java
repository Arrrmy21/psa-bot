package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class GreetingsProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        ResponseBody responseBody = new ResponseBody();
        responseBody.setUniqueText(userUpdateData.getNameToAddress());

        return new ServerResponse(StandardResponse.GREETINGS, responseBody);
    }
}
