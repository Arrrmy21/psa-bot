package com.onyshchenko.psabot.services.command.processors;

import com.onyshchenko.psabot.models.request.UserRequest;
import com.onyshchenko.psabot.models.response.ResponseBody;
import com.onyshchenko.psabot.models.response.ServerResponse;
import com.onyshchenko.psabot.models.common.StandardResponse;
import com.onyshchenko.psabot.models.user.UserUpdateData;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import org.springframework.stereotype.Service;

@Service
public class GetGameListProcessor extends CommandProcessor {

    @Override
    public ServerResponse getMainServerResponse(UserUpdateData userUpdateData, UserRequest userRequest) {

        StringBuilder getGamesUrlBuilder = new StringBuilder(GAMES);
        getGamesUrlBuilder.append("?").append(PAGE).append(userRequest.getCurPage());
        if (userRequest.getFilters() != null) {
            getGamesUrlBuilder.append(ADD_FILTER);
            userRequest.getFilters().forEach(f -> getGamesUrlBuilder.append(f.getFilterName()).append(","));
            getGamesUrlBuilder.deleteCharAt(getGamesUrlBuilder.length() - 1);
        }
        String getGamesUrl = getGamesUrlBuilder.toString();
        if (getGamesUrl.contains("publisherId")) {
            getGamesUrl = getGamesUrl.replace("%s", preparePublisherId(userRequest.getPreviousPageInfo()));
        }
        String textFromServer = "";
        try {
            textFromServer = mainServerService.executeByUrl(getGamesUrl, userUpdateData.getUserName());
        } catch (Exception ex) {
            LOGGER.info("Exception occurred.");
        }
        if (textFromServer.equals(StandardResponse.FAILED_RESPONSE.getTextResponse())) {

            return new ServerResponse(StandardResponse.FAILED_RESPONSE);
        }

        ResponseBody responseBody = serverResponseParser.parseResponseBody(textFromServer);

        return new ServerResponse(StandardResponse.GET_GAMES_RESPONSE, responseBody);
    }

    private String preparePublisherId(String previousPageInfo) {
        if (previousPageInfo.length() == 1) {
            return "0" + previousPageInfo;
        } else return previousPageInfo;
    }
}
