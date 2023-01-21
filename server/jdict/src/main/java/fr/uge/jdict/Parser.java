package fr.uge.jdict;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public record Parser(OutputFormat formatExport) {

    private static final HashSet<String> INVALID_CATEGORIES = new HashSet<>(Arrays.asList("onomatopée", "symbole"));

    public String toJSON(StringBuilder page, String title, String text, String langue) {
        JSONObject json = new JSONObject();
        json.put("title", title);
        int catDefCounter = 0;
        JSONObject definitions = new JSONObject();
        Boolean lookingForDefinition = false;
        String categoryTitle = null;
        // On parcours chaque ligne du texte
        for (String line : text.split("\n")) {

            // Ligne de définition en français
            if (line.startsWith("=== {{") || line.startsWith("==={{")) {
                if (line.contains("|" + langue + "|") || line.endsWith("|" + langue + "}} ===")
                        || line.endsWith("|" + langue + "}}===")) {
                    String[] category = line.split("\\|");
                    categoryTitle = category[1];
                    catDefCounter = 0;
                    // On vérifie que la catégorie n'est pas invalide
                    if (INVALID_CATEGORIES.contains(categoryTitle)) {
                        categoryTitle = null;
                        lookingForDefinition = false;
                        return "";
                    }

                    // Evite de se retrouver avec plusieurs fois la même catégorie
                    if (!definitions.has(categoryTitle)) {
                        definitions.put(categoryTitle, new JSONArray());
                    }
                    lookingForDefinition = true;
                } else {
                    lookingForDefinition = false;
                    if (catDefCounter == 0 && categoryTitle != null) {
                        if (definitions.has(categoryTitle) && definitions.getJSONArray(categoryTitle).length() == 0) {
                            definitions.remove(categoryTitle);
                        }
                    }

                }
                // // Ligne ne contenant pas de définition en français
                // if (!line.contains("|" + langue + "|") && !line.endsWith("|" + langue + "}}
                // ===")) {
                // lookingForDefinition = false;
                // // On vide la liste des définitions pour en accueillir de nouvelles
                // }
            }

            if (lookingForDefinition && line.startsWith("#") && !(line.startsWith("##") || line.startsWith("#*"))
                    && line.length() > 2) {
                if (categoryTitle == null) {
                    System.out.println(line);
                    throw new RuntimeException("categoryTitle is null");
                }
                if (!definitions.has(categoryTitle)) {
                    System.out.println(line);
                    System.out.println(categoryTitle);
                    throw new RuntimeException("categoryTitle is not in definitions");
                }
                catDefCounter++;
                definitions.getJSONArray(categoryTitle).put(line.substring(2));
            }

        }

        json.put("definitions", definitions);

        // Si une catégorie n'a pas de définitions, on l'ajoute à la liste des
        // catégories à supprimer
        List<String> toRemove = new ArrayList<>();
        definitions.keySet().stream().forEach(string -> {
            if (definitions.getJSONArray(string).length() == 0) {
                toRemove.add(string);
            }
        });

        // On supprime les catégories vides
        toRemove.stream().forEach(definitions::remove);

        if (definitions.length() == 0) {
            return "";
        }

        String jsonStr = json.toString() + "\n";

        // Si il est impossible d'encoder la chaine en UTF-8 :
        try {
            IndexMaker.getIndexMaker().addTitle(title, (int) (jsonStr).getBytes("UTF-8").length);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Impossible d'encoder la chaine en UTF-8");
            System.err.println("Chaine : " + jsonStr);
            e.printStackTrace();
            System.exit(1);
        }

        return jsonStr;
    }

}
