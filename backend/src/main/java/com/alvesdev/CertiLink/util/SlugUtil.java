package com.alvesdev.CertiLink.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {
    private static final Pattern NON_ASCII_MARKS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private SlugUtil() {}

    public static String slugify(String input) {
        if (input == null) return null;
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = NON_ASCII_MARKS.matcher(normalized).replaceAll("");
        String cleaned = withoutAccents
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return cleaned.isEmpty() ? "template" : cleaned;
    }
}
