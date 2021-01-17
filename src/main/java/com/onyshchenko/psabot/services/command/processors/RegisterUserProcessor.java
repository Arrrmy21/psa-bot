package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserProcessor extends CommandProcessor {


    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        if (userUpdateData.getUserName() == null) {
            return new ServerResponse(StandardResponse.USERNAME_ABSENCE, null);
        }

        String registerUserResponse = mainServerService.registerUser(userUpdateData.getUser());
        ResponseBody responseBody = new ResponseBody();
        responseBody.setUniqueText(userUpdateData.getNameToAddress());

        if (registerUserResponse.equalsIgnoreCase(StandardResponse.USER_CREATED.getTextResponse())) {
            mainServerService.getTokenFromService(userUpdateData.getUserName());

            return new ServerResponse(StandardResponse.WELCOME, responseBody);
        } else if (registerUserResponse.equalsIgnoreCase(StandardResponse.USER_EXIST.getTextResponse())) {
            mainServerService.getTokenFromService(userUpdateData.getUserName());

            return new ServerResponse(StandardResponse.WELCOME_BACK, responseBody);
        } else {
            return new ServerResponse(StandardResponse.SERVICE_UNAVAILABLE);
        }

    }

}
