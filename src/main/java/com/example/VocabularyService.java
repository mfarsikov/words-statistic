package com.example;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VocabularyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyService.class);

    private final Map<String, Long> wordToCountInText;

    private final Map<String, Long> wordToCountRequests;

    public VocabularyService(@Value("${library.folder}") String path) throws IOException {

        wordToCountInText = Files.list(Paths.get(path))
            .filter(FileSystems.getDefault().getPathMatcher("glob:**.txt")::matches)
            .peek(currentFile -> LOGGER.info("Reading file: {}", currentFile))
            .flatMap(VocabularyService::lines)
            .flatMap(Pattern.compile("\\W+")::splitAsStream)
            .map(String::toLowerCase)
            .collect(groupingBy(Function.identity(), counting()));

        LOGGER.info("Loaded {} distinct of {} words from {}",
            wordToCountInText.size(),
            wordToCountInText.values().stream()
                                      .mapToLong(Long::longValue)
                                      .sum(),
            path);

        wordToCountRequests = new ConcurrentHashMap<>();
    }

    private static Stream<String> lines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WordStatistics getStatistics(String word) {
        String lowerCase = word.toLowerCase();

        Long requestedTimes = wordToCountRequests
            .compute(lowerCase, (k, v) -> Optional.ofNullable(v)
                                                  .map(count -> count + 1)
                                                  .orElse(1L));

        Long textOccurrences = wordToCountInText.getOrDefault(lowerCase, 0L);

        return new WordStatistics(word, requestedTimes, textOccurrences);
    }

}
