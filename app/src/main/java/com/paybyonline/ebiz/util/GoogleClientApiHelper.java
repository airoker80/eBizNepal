package com.paybyonline.ebiz.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.people.v1.PeopleService;

import java.io.IOException;

/**
 * Created by Swatin on 12/8/2017.
 */

class GoogleClientApiHelper {

    private static NetHttpTransport httpTransport;
    private static JacksonFactory jsonFactory;
    private static String tempAccessToken;

    private static GoogleCredential init(String serverAuthCode, String clientId, String clientSecret) throws IOException {
        httpTransport = new NetHttpTransport();
        jsonFactory = JacksonFactory.getDefaultInstance();

        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";


        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory, clientId, clientSecret,
                serverAuthCode,
                redirectUrl).execute();

        tempAccessToken = tokenResponse.getAccessToken();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(clientId, clientSecret)
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build();

        credential.setFromTokenResponse(tokenResponse);

        return credential;
    }

    static PeopleService setUpPeople(String serverAuthCode, String clientId, String clientSecret, String appName) throws IOException {

        GoogleCredential credential = init(serverAuthCode, clientId, clientSecret);

        return new PeopleService.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(appName)
                .build();
    }

    static Gmail setUpMail(String appName) throws IOException {

        GoogleCredential credential = new GoogleCredential().setAccessToken(tempAccessToken);

        return new com.google.api.services.gmail.Gmail.Builder(
                httpTransport, jsonFactory, credential)
                .setApplicationName(appName)
                .build();

    }
}
