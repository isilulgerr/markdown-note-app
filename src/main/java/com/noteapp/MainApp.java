package com.noteapp;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MainApp {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Initialize our core services
        MarkdownService markdownService = new MarkdownService();
        GrammarService grammarService = new GrammarService();
        SupabaseService supabaseService = new SupabaseService();

        // --- ENDPOINT: Grammar Check ---
        server.createContext("/api/check-grammar", (exchange) -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = readRequestBody(exchange);
                String result = grammarService.checkGrammar(body);
                sendResponse(exchange, result, 200);
            } else {
                sendResponse(exchange, "Method not allowed", 405);
            }
        });

        // --- ENDPOINT: Save Note (Markdown) ---
        server.createContext("/api/save", (exchange) -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String markdownContent = readRequestBody(exchange);
                // For now, we use a generic title. You can parse a real title later.
                String response = supabaseService.saveNote("My Markdown Note", markdownContent);
                sendResponse(exchange, response, 201);
            }
        });

        // --- ENDPOINT: Render Markdown to HTML ---
        server.createContext("/api/render", (exchange) -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String markdownContent = readRequestBody(exchange);
                String htmlOutput = markdownService.convertToHtml(markdownContent);
                sendResponse(exchange, htmlOutput, 200);
            }
        });

        // --- ENDPOINT: Get Rendered HTML by ID ---
        server.createContext("/api/view-note", (exchange) -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Simple way to get ?id= value from query string
                String query = exchange.getRequestURI().getQuery();
                String id = query.split("=")[1];

                // 1. Get raw markdown from Supabase
                String jsonResponse = supabaseService.getNoteById(id);

                // 2. Parse JSON to get the 'content' field
                // Note: For simplicity, we assume the content is there.
                // In a real app, you'd use Gson to parse this properly.
                String markdownContent = jsonResponse.contains("\"content\":\"")
                        ? jsonResponse.split("\"content\":\"")[1].split("\"")[0]
                        : "Note not found";

                // 3. Convert to HTML
                String htmlResult = markdownService.convertToHtml(markdownContent);

                // 4. Send as HTML (Not JSON!)
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                sendResponse(exchange, htmlResult, 200);
            }
        });
        server.setExecutor(null);
        System.out.println("Markdown Note App is running on port " + port);
        server.start();
    }

    // Helper method to read the incoming request body as a String
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    // Helper method to send a professional HTTP response
    private static void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}