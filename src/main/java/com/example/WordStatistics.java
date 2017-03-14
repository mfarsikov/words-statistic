package com.example;

public class WordStatistics {
   private final String word;
   private final Long requestedTimes;
   private final Long textOccurrences;

    public WordStatistics(String word, Long requestedTimes, Long textOccurrences) {
        this.word = word;
        this.requestedTimes = requestedTimes;
        this.textOccurrences = textOccurrences;
    }

    public String getWord() {
        return word;
    }

    public Long getRequestedTimes() {
        return requestedTimes;
    }

    public Long getTextOccurrences() {
        return textOccurrences;
    }
}
