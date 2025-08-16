package com.mburakaltun.guessbuddy.common.service;

import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.enums.ContentFilterErrorCode;
import com.mburakaltun.guessbuddy.common.util.StringUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CleanContentService {

    @Value("${app.content-filter.objectionable-words}")
    private String objectionableWordsConfig;

    @Value("${app.content-filter.case-sensitive}")
    private boolean caseSensitive;

    @Value("${app.content-filter.strict-mode}")
    private boolean strictMode;

    private Set<String> objectionableWords;
    private List<Pattern> objectionablePatterns;

    @PostConstruct
    public void init() {
        try {
            initializeObjectionableWords();
            initializePatterns();
        } catch (Exception e) {
            log.error("Failed to initialize content filter", e);
            throw new RuntimeException("Content filter initialization failed", e);
        }
    }

    private void initializeObjectionableWords() {
        objectionableWords = new HashSet<>();
        if (StringUtility.isNotBlank(objectionableWordsConfig)) {
            String[] words = objectionableWordsConfig.split(",");
            for (String word : words) {
                String trimmedWord = word.trim();
                if (StringUtility.isNotBlank(trimmedWord)) {
                    objectionableWords.add(caseSensitive ? trimmedWord : trimmedWord.toLowerCase());
                }
            }
        }
    }

    private void initializePatterns() {
        objectionablePatterns = new ArrayList<>();
        for (String word : objectionableWords) {
            String escapedWord = Pattern.quote(word);

            String basicPattern = "\\b" + escapedWord + "\\b";

            String substitutionPattern = escapedWord
                    .replace("a", "[a@4]")
                    .replace("e", "[e3]")
                    .replace("i", "[i1!]")
                    .replace("o", "[o0]")
                    .replace("s", "[s5$]");
            substitutionPattern = "\\b" + substitutionPattern + "\\b";

            String spacedPattern = escapedWord.replace("", "[\\s\\-_]*");

            int flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
            objectionablePatterns.add(Pattern.compile(basicPattern, flags));
            objectionablePatterns.add(Pattern.compile(substitutionPattern, flags));

            if (strictMode) {
                objectionablePatterns.add(Pattern.compile(spacedPattern, flags));
            }
        }
    }

    public void validateContent(String text) throws AppException {
        if (containsObjectionableContent(text)) {
            throw new AppException(ContentFilterErrorCode.OBJECTIONABLE_CONTENT_DETECTED);
        }
    }

    public void validateContent(String... texts) throws AppException {
        if (texts == null) {
            return;
        }

        for (String text : texts) {
            validateContent(text);
        }
    }

    public boolean containsObjectionableContent(String text) {
        if (StringUtility.isBlank(text)) {
            return false;
        }

        String textToCheck = caseSensitive ? text : text.toLowerCase();

        for (Pattern pattern : objectionablePatterns) {
            if (pattern.matcher(textToCheck).find()) {
                return true;
            }
        }

        return false;
    }
}
