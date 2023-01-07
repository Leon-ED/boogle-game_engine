package fr.uge.jdict;

import org.json.JSONArray;
import org.json.JSONObject;

public record Parser(OutputFormat formatExport) {

    void formatExport(StringBuilder page, String title, String text) {
        switch (formatExport) {
            case JSON -> JSON(page, title, text);
            default -> throw new IllegalArgumentException("Format non supporté");

        }
    }

    private void JSON(StringBuilder page, String title, String text) {
        // !!! On return rien car on ajoute directement au StringBuilder passé en
        // référence !!!

        JSONObject json = new JSONObject();
        json.put("title", title);

        JSONObject definitions = new JSONObject();
        JSONArray definitionsList = new JSONArray();
        Boolean lookingForDefinition = false;
        // On parcours chaque ligne du texte
        for (String line : text.split("\n")) {

            // Ligne de définition en français
            if (line.startsWith("=== {{")) {
                if (line.contains("|fr|") || line.endsWith("|fr}} ===")) {
                    String[] category = line.split("\\|");

                        // Evite de se retrouver avec plusieurs fois la même catégorie
                        if(!definitions.has(category[1])){
                            definitions.put(category[1], definitionsList);
                        }
                            lookingForDefinition = true;
                }
                // Ligne ne contenant pas de définition en français
                if (!line.contains("|fr|") && !line.endsWith("|fr}} ===")) {
                    lookingForDefinition = false;
                    // On vide la liste des définitions pour en accueillir de nouvelles
                    definitionsList = new JSONArray();
                }
            }
            // Si on recherche les définitions et que la ligne commence par '# ' on l'ajoute
            // à la liste sauf le #
            if (lookingForDefinition && line.startsWith("#") && !(line.startsWith("#*") || line.startsWith("##")) && line.length() > 2) {
                definitionsList.put(line.substring(2));
            }

        }
        // On a finit de parser les balises <text> </text> de la page on l'ajoute au
        // StringBuilder passé en référence
        page.append(json.put("definitions", definitions).toString());
        page.append("\n");
    }




    private static void TXT(StringBuilder page, String title, String text) {

        Boolean lookingForDefinition = false;
        //On parcours chaque ligne du texte
        for (String line : text.split("\n")) {

            // Ligne de définition en français
            if (line.startsWith("=== {{") && (line.contains(" |fr|") || line.endsWith("|fr}} ==="))) {
                String[] category = line.split("\\|");

                // S'il y a plus 3 elem, c'est que c'est une catégorie spéciale :
                // TODO : Les afficher correctement et l'implémenter correctement
                if (category.length == 3) {
                    page.append("\t" + category[1] + "\n");
                    lookingForDefinition = true;
                }
            }
            // Ligne ne contenant pas de définition en français
            if (line.startsWith("=== {{") && !line.contains("|fr|") && !line.endsWith("|fr}} ===")) {
                lookingForDefinition = false;
            }

            // Si on recherche les définitions et que la ligne commence par '# ' on l'ajoute
            // à la liste sauf le #
            if (lookingForDefinition && line.startsWith("# ")) {
                page.append("\t\t-" + line.substring(2) + "\n");
            }

        }
        // On a finit de parser les balises <text> </text> de la page on l'ajoute au
        // StringBuilder passé en référence
        page.append("\n");

    }
}
