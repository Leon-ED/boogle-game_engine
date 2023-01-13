package fr.uge.jdict;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public record Parser(OutputFormat formatExport) {

    private static final List<String> INVALID_CATEGORIES = 
    List.of("onomatopée","symbole");


    public String toJSON(StringBuilder page, String title, String text, String langue) {
        // !!! On return rien car on ajoute directement au StringBuilder passé en
        // référence !!!
        // if (title.equalsIgnoreCase("MANUTENTIONNAIRE")) {
        //     System.out.println(title);
        //     }

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
                if (line.contains("|" + langue + "|") || line.endsWith("|" + langue + "}} ===") || line.endsWith("|" + langue + "}}===")) {
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
                }else{
                    lookingForDefinition = false;
                    if(catDefCounter == 0 && categoryTitle != null){
                        if(definitions.has(categoryTitle) && definitions.getJSONArray(categoryTitle).length() == 0){
                            definitions.remove(categoryTitle);
                        }
                    }


                }
                // // Ligne ne contenant pas de définition en français
                // if (!line.contains("|" + langue + "|") && !line.endsWith("|" + langue + "}} ===")) {
                //     lookingForDefinition = false;
                //     // On vide la liste des définitions pour en accueillir de nouvelles
                // }
            }
            // Si on recherche les définitions et que la ligne commence par '# ' on l'ajoute
            // à la liste sauf le #
            // if(title.equalsIgnoreCase("MANUTENTIONNAIRE")){
            //     System.out.println(line);
            // }

  
            if (lookingForDefinition && line.startsWith("#")  && !(line.startsWith("##") || line.startsWith("#*"))
            && line.length() > 2) {
                if(categoryTitle == null){
                    System.out.println(line);
                    throw new RuntimeException("categoryTitle is null");
                }
                if(!definitions.has(categoryTitle)){
                    System.out.println(line);
                    System.out.println(categoryTitle);
                    throw new RuntimeException("categoryTitle is not in definitions");
                }
                catDefCounter++;
                definitions.getJSONArray(categoryTitle).put(line.substring(2));
            }

        }

        json.put("definitions", definitions);
        
        List<String> toRemove = new ArrayList<>();
        for (String string : definitions.keySet()) {
            if(definitions.getJSONArray(string).length() == 0){
                toRemove.add(string);
            }
        }
        toRemove.stream().forEach(definitions::remove);
        
        if(definitions.length() == 0){
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
