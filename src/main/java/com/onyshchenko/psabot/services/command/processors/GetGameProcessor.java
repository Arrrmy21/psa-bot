package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class GetGameProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        String gameId = commandLine.getIdToPass();
        String urlName = "games/" + gameId;

        String textResponseFromServer = mainServerService.executeByUrl(urlName, userUpdateData.getUserName());

        ResponseBody responseBody = serverResponseParser.parseResponseBody(textResponseFromServer);

        return new ServerResponse(StandardResponse.GET_GAME_RESPONSE, responseBody);
    }
}
