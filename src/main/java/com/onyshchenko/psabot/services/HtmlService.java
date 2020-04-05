package com.onyshchenko.psabot.services;

import com.onyshchenko.psabot.models.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class HtmlService {

    private Logger logger = LoggerFactory.getLogger(HtmlService.class);

    private static final String APP_URL = "https://ps-analyzer.herokuapp.com/";


    public JSONObject getJsonFromURL(String urlName) {

        try {
            URL url = new URL(APP_URL + urlName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
            e.printStackTrace();
            throw new JSONException("Failed to parse JSON");
        }
    }

    public String registerUser(User user) {

        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(APP_URL + "users/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
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

    public String addToWishList(String stringUrl) {

        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(APP_URL + stringUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.addRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

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

    @Scheduled(fixedDelay = 600000)
    public void scheduledCall() {

        logger.info("Scheduled call");

        String https_url = "https://ps-analyzer.herokuapp.com/";
        String https_url_bot = "https://ps-analyzer-bot.herokuapp.com/";
        URL url;
        URL url_self;
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            url_self = new URL(https_url_bot);
            HttpsURLConnection con_self = (HttpsURLConnection) url_self.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
