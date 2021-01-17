package com.onyshchenko.psabot.models.response;

import com.onyshchenko.psabot.models.common.StandardResponse;

public class ServerResponse {

    StandardResponse textResponse;
    ResponseBody responseBody;

    public ServerResponse(StandardResponse textResponse) {
        this.textResponse = textResponse;
    }

    public ServerResponse(StandardResponse textResponse, ResponseBody responseBody) {
        this.textResponse = textResponse;
        this.responseBody = responseBody;
    }

    public StandardResponse getTextResponse() {
        return textResponse;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }
}
