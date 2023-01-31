package fr.uge.jdict;

import java.text.Normalizer;

public class DictionaryNormalizer {

    public static int compareNormalized(String o1, String o2) {
        int i = normalize(o1).compareTo(normalize(o2));
        if (i == 0) {
            return o1.compareTo(o2);
        }
        return i;
    }


    public static String lightNormalize(String word){
        word = word.replaceAll("œ", "oe").replaceAll("œ".toUpperCase(), "OE");
        word = word.replaceAll("æ", "ae").replaceAll("æ".toUpperCase(), "AE");
        
        return word;
    }

    public static String normalize(String word) {
        word = word.toLowerCase();
        word = lightNormalize(word);
    

        String string = Normalizer
                .normalize(word, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return string.toUpperCase();
    }

}
