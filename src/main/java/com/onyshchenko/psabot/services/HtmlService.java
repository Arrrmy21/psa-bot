package com.onyshchenko.psabot.services;

import com.onyshchenko.psabot.models.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class HtmlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlService.class);

    @Value("${bot.main.server.url}")
    private String appUrl;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APP_PROPERTY = "application/json; utf-8";
    private static final String ACCEPT = "Accept";
    private static final String APP_JSON = "application/json";

    private Map<String, String> userTokens = new HashMap<>();


    public JSONObject getJsonFromURL(String urlName, String username) {

        try {
            URL url = new URL(appUrl + urlName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            fillRequestWithToken(con, username);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString());
        } catch (IOException e) {
            throw new JSONException("Failed to parse JSON");
        }
    }

    public String registerUser(User user) {

        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(appUrl + "users/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty(CONTENT_TYPE, APP_PROPERTY);
            con.setRequestProperty(ACCEPT, APP_JSON);
            con.setDoOutput(true);


            JSONObject jsonUser = new JSONObject(user);
            String request = jsonUser.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = request.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String addToWishList(String stringUrl, String username) {

        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(appUrl + stringUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.addRequestProperty(CONTENT_TYPE, APP_PROPERTY);
            con.setRequestProperty(ACCEPT, APP_JSON);
            con.setDoOutput(true);

            fillRequestWithToken(con, username);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String deleteFromWishList(String stringUrl, String username) {

        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(appUrl + stringUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.addRequestProperty(CONTENT_TYPE, APP_PROPERTY);
            con.setRequestProperty(ACCEPT, APP_JSON);
            con.setDoOutput(true);

            fillRequestWithToken(con, username);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public void getTokenFromService(String username) {

        LOGGER.info("Starting procedure of getting token");
        String authPath = "auth/login";
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(appUrl + authPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty(CONTENT_TYPE, APP_PROPERTY);
            con.setRequestProperty(ACCEPT, APP_JSON);
            con.setDoOutput(true);

            String request = "{ \"username\": \"" + username + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = request.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String token = response.toString();
        JSONObject jsonObject = new JSONObject(token);
        String tokenString = String.valueOf(jsonObject.get("token"));
        if (!token.isEmpty()) {
            LOGGER.info("Saving token for user");
            userTokens.put(username, tokenString);
        }
    }

    private void fillRequestWithToken(HttpURLConnection connection, String username) {
        String token = userTokens.get(username);
        if (token == null || token.isEmpty()) {
            getTokenFromService(username);
            token = userTokens.get(username);
        }
        connection.addRequestProperty("Authorization", "Bearer_" + token);
    }
}
