package com.koy.kaviewer.shell.core;

import org.jline.reader.ParsedLine;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class KaViewerParsedLine implements ParsedLine {
    private final String line;

    private final List<String> words;

    private final int wordIndex;

    private final int wordCursor;

    private final int cursor;


    public KaViewerParsedLine(final String line, final List<String> words, final int wordIndex,
                              final int wordCursor, final int cursor) {
        this.line = line;
        this.words = Collections.unmodifiableList(Objects.requireNonNull(words));
        this.wordIndex = wordIndex;
        this.wordCursor = wordCursor;
        this.cursor = cursor;
    }

    @Override
    public String word() {
        if ((wordIndex < 0) || (wordIndex >= words.size())) {
            return "";
        }
        return words.get(wordIndex);
    }

    @Override
    public int wordCursor() {
        return this.wordCursor;
    }

    @Override
    public int wordIndex() {
        return this.wordIndex;
    }

    @Override
    public List<String> words() {
        return this.words;
    }

    @Override
    public String line() {
        return this.line;
    }

    @Override
    public int cursor() {
        return this.cursor;
    }

}
