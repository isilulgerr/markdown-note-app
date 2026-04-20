package com.noteapp;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        // Markdown metnini analiz eden araç
        this.parser = Parser.builder().build();
        // Analiz edilen metni HTML'e çeviren araç
        this.renderer = HtmlRenderer.builder().build();
    }

    public String convertToHtml(String markdownText) {
        if (markdownText == null || markdownText.isEmpty()) {
            return "";
        }
        Node document = parser.parse(markdownText);
        return renderer.render(document);
    }
}