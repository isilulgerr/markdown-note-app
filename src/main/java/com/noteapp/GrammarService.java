package com.noteapp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GrammarService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String checkGrammar(String text) {
        try {
            // Metni URL formatına uygun hale getirelim (boşluklar %20 olsun vb.)
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            // LanguageTool ücretsiz API'sine istek atıyoruz
            String url = "https://api.languagetool.org/v2/check?text=" + encodedText + "&language=en-US";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody()) // API POST istiyor
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); // Bize hataların listesini JSON olarak döner

        } catch (Exception e) {
            return "{\"error\": \"Grammar check failed: " + e.getMessage() + "\"}";
        }
    }
}