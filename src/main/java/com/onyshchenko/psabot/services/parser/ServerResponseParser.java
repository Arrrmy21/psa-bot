package com.onyshchenko.psabot.services.parser;

import com.onyshchenko.psabot.models.response.ResponseBody;
import org.springframework.stereotype.Service;

@Service
public interface ServerResponseParser {

    ResponseBody parseResponseBody(String data);
}
