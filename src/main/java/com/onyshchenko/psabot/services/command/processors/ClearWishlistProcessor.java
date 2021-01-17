package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class ClearWishlistProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest commandLine) {

        String clearWishListUrl = USERS + commandLine.getIdToPass();
        String textResponseFromServer = mainServerService.deleteFromWishList(clearWishListUrl, userUpdateData.getUserName());

        if (textResponseFromServer.contains("removed from wish list")) {
            ResponseBody responseBody = new ResponseBody();
            responseBody.setUniqueText(textResponseFromServer);
            return new ServerResponse(StandardResponse.GAME_ADDED_TO_WL, responseBody);
        }
        return new ServerResponse(StandardResponse.ERROR_RESPONSE);
    }
}
