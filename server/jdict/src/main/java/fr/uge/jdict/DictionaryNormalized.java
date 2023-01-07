package fr.uge.jdict;

import java.text.Normalizer;

public class DictionaryNormalized {
    
    public static int compareNormalized(String o1, String o2) {
        int i = normalize(o1).compareTo(normalize(o2));
        if(i == 0){
            return o1.compareTo(o2);
        }
        return i;
    }

    public static String normalize(String word) {
        String string = Normalizer.normalize(word, Normalizer.Form.NFD);
        string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return string.toUpperCase();
    }



}
