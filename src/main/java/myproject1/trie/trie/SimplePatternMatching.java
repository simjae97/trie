package myproject1.trie.trie;

import java.util.ArrayList;
import java.util.List;

public class SimplePatternMatching {
    public static List<String> searchPatterns(String text, List<String> patterns) {
        List<String> foundPatterns = new ArrayList<>();
        for (String pattern : patterns) {
            if (text.contains(pattern)) {
                foundPatterns.add(pattern);
            }
        }
        return foundPatterns;
    }
}
