package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class CabinetProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        String getUserNotificationsUrl = USERS + userUpdateData.getUserId() + "/notifications";
        String textResponseFromServer = mainServerService.executeByUrl(getUserNotificationsUrl, userUpdateData.getUserName());

        StandardResponse standardResponse = StandardResponse.getResponseByValue(textResponseFromServer);
        boolean currentNotificationStatus = textResponseFromServer
                .equalsIgnoreCase(StandardResponse.NOTIFICATIONS_ON.getTextResponse());

        ResponseBody responseBody = new ResponseBody();
        responseBody.setCurrentNotificationStatus(currentNotificationStatus);

        return new ServerResponse(standardResponse, responseBody);
    }
}
