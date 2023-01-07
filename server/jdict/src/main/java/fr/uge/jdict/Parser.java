package fr.uge.jdict;

import org.json.JSONArray;
import org.json.JSONObject;

public record Parser(OutputFormat formatExport) {

    public void toJSON(StringBuilder page, String title, String text, String langue) {
        // !!! On return rien car on ajoute directement au StringBuilder passé en
        // référence !!!

        JSONObject json = new JSONObject();
        json.put("title", title);

        JSONObject definitions = new JSONObject();
        Boolean lookingForDefinition = false;
        String categoryTitle = null;
        // On parcours chaque ligne du texte
        for (String line : text.split("\n")) {

            // Ligne de définition en français
            if (line.startsWith("=== {{") || line.startsWith("==={{")) {
                if (line.contains("|" + langue + "|") || line.endsWith("|" + langue + "}} ===") || line.endsWith("|" + langue + "}}===")) {
                    String[] category = line.split("\\|");
                    categoryTitle = category[1];

                    // Evite de se retrouver avec plusieurs fois la même catégorie
                    if (!definitions.has(categoryTitle)) {
                        definitions.put(categoryTitle, new JSONArray());
                    }
                    lookingForDefinition = true;
                }
                // Ligne ne contenant pas de définition en français
                if (!line.contains("|" + langue + "|") && !line.endsWith("|" + langue + "}} ===")) {
                    lookingForDefinition = false;
                    // On vide la liste des définitions pour en accueillir de nouvelles
                }
            }
            // Si on recherche les définitions et que la ligne commence par '# ' on l'ajoute
            // à la liste sauf le #
            if (lookingForDefinition && line.startsWith("#") && !(line.startsWith("#*") || line.startsWith("##"))
                    && line.length() > 2) {
                definitions.getJSONArray(categoryTitle).put(line.substring(2));
            }

        }
        // On a finit de parser les balises <text> </text> de la page on l'ajoute au
        // StringBuilder passé en référence
        json.put("definitions", definitions);
        try {
            IndexMaker.getIndexMaker().addTitle(title, (int) (json.toString() + "\n").getBytes("UTF-8").length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if(title.equalsIgnoreCase("ACCUEIL")){
        // // System.out.println(json.toString());
        // // System.out.println("ACCUEIL");
        // // System.out.println((json.toString() + "\n").length());
        // }
        page.append(json.toString() + "\n");
    }

}
