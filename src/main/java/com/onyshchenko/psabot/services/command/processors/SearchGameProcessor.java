package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class SearchGameProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        String searchByName = GAMES +
                PAGE + commandLine.getCurPage() +
                ADD_FILTER + "name=" + commandLine.getIdToPass();
        String textFromServer = mainServerService.executeByUrl(searchByName, userUpdateData.getUserName());

        ResponseBody responseBody = serverResponseParser.parseResponseBody(textFromServer);

        return new ServerResponse(StandardResponse.SEARCH_GAMES, responseBody);
    }
}
