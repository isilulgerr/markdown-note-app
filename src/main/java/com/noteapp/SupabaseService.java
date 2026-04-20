package com.noteapp;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class SupabaseService {
    private String SUPABASE_URL;
    private String SUPABASE_KEY;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public SupabaseService() {
        loadConfig();
    }

    private void loadConfig() {
        try (java.io.InputStream input = new java.io.FileInputStream("config.properties")) {
            java.util.Properties prop = new java.util.Properties();
            prop.load(input);
            SUPABASE_URL = prop.getProperty("supabase.url");
            SUPABASE_KEY = prop.getProperty("supabase.key");
        } catch (java.io.IOException ex) {
            System.err.println("Error loading config.properties: " + ex.getMessage());
            // Fallback or default values could be set here if needed
        }
    }

    public String saveNote(String title, String content) {
        try {
            // Prepare the JSON body correctly
            Map<String, String> noteData = Map.of(
                    "title", title,
                    "content", content);
            String jsonPayload = gson.toJson(noteData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SUPABASE_URL))
                    .header("apikey", SUPABASE_KEY)
                    .header("Authorization", "Bearer " + SUPABASE_KEY)
                    .header("Content-Type", "application/json")
                    // This header is important for Supabase to accept the insert
                    .header("Prefer", "return=minimal")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Log the response code for debugging
            System.out.println("Supabase Response Code: " + response.statusCode());
            return response.body();

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    // Fetch a single note by its ID
    public String getNoteById(String id) {
        try {
            // We filter by id using the query parameter ?id=eq.ID
            String url = SUPABASE_URL + "?id=eq." + id + "&select=content";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SUPABASE_KEY)
                    .header("Authorization", "Bearer " + SUPABASE_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); // This returns a JSON array with one object

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}