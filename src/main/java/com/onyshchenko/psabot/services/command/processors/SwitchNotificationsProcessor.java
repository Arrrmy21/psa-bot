package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class SwitchNotificationsProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        String switchUserNotificationsUrl = USERS + userUpdateData.getUserId() + "/notifications/"
                + commandLine.getPreviousPageInfo().toLowerCase();
        String textResponseFromServer = mainServerService.
                executeByUrl(switchUserNotificationsUrl, userUpdateData.getUserName());

        boolean currentNotificationStatus = textResponseFromServer
                .equalsIgnoreCase(StandardResponse.NOTIFICATIONS_ON.getTextResponse());

        ResponseBody responseBody = new ResponseBody();
        responseBody.setCurrentNotificationStatus(currentNotificationStatus);

        return new ServerResponse(StandardResponse.getResponseByValue(textResponseFromServer), responseBody);
    }
}
