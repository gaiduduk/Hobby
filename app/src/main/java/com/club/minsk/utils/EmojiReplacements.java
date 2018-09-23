package com.club.minsk.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EmojiReplacements {
    /** Characters that may not occur immediately before or after an emoticon */
    private static final String REGEX_SURROUNDING_CHARS_DISALLOWED = "[-_a-zA-Z0-9)(;:*<>=/]";
    /** A negative look-behind ensuring that the match is not preceded by one of the characters above */
    private static final String REGEX_NEGATIVE_LOOKBEHIND = "(?<!"+REGEX_SURROUNDING_CHARS_DISALLOWED+")";
    /** A negative look-ahead ensuring that the match is not followed by one of the characters above */
    private static final String REGEX_NEGATIVE_LOOKAHEAD = "(?!"+REGEX_SURROUNDING_CHARS_DISALLOWED+")";

    private static class ReplacementsMap extends HashMap<String,Integer> {

        private static final long serialVersionUID = 4948071414363715959L;
        private static ReplacementsMap mInstance;

        private ReplacementsMap() {
            super();
            put(":)", 0x1F60A);
            put(":(", 0x1F61E);
            put(":D", 0x1F603);
            put(";)", 0x1F609);
            put(":P", 0x1F61C);
            put(":*", 0x1F618);
            put(">:[", 0x1F621);
            put(":'|", 0x1F625);
            put(":-[", 0x1F629);
            put(":'(", 0x1F62D);
            put("=O", 0x1F631);
            put("xD", 0x1F601);
            put(":')", 0x1F602);
            put(":/", 0x1F612);
            put(":|", 0x1F614);
            put("*_*", 0x1F60D);
        }

        public static ReplacementsMap getInstance() {
            if (mInstance == null) {
                mInstance = new ReplacementsMap();
            }
            return mInstance;
        }

    }

    private static String getUnicodeChar(int codepoint) {
        return new String(Character.toChars(codepoint));
    }

    /** Constructs a regular expression which ensures that the emoticon is not part of a longer string of special chars (e.g. <:)))> or <http://> which both include basic emoticons) */
    private static String getEmoticonSearchRegex(String emoticon) {
        return REGEX_NEGATIVE_LOOKBEHIND+"("+ Pattern.quote(emoticon)+")"+REGEX_NEGATIVE_LOOKAHEAD;
    }

    /**
     * Replaces all emoticons in the given text with their corresponding Unicode Emoji
     *
     * @param text the String to search and replace in
     * @return the new String containing the Unicode Emoji
     */
    public static String replaceInText(String text) {
        return replaceInText(text, false);
    }

    /**
     * Converts between emoticons and their corresponding Unicode Emoji in the given text
     *
     * @param text the String to search and replace in
     * @param reverse whether to replace emoticons with emoji as usual (false) or revert emoji to emoticons (true)
     * @return the new String containing the the converted entities
     */
    public static String replaceInText(String text, boolean reverse) {
        if (text == null || text.isEmpty())
            return null;
        final ReplacementsMap replacements = ReplacementsMap.getInstance();
        String emoticon;
        Integer codepoint;

        for (Map.Entry<String, Integer> entry : replacements.entrySet()) {
            if (entry != null) {
                emoticon = entry.getKey();
                codepoint = entry.getValue();
                if (emoticon != null && codepoint != null) {
                    String unicodeChar = getUnicodeChar(codepoint.intValue());
                    if (reverse) {
                        text = text.replace(unicodeChar, emoticon);
                    }
                    else {
                        text = text.replace(emoticon, unicodeChar);
                    }
                }
            }
        }

        return text;
    }
}
