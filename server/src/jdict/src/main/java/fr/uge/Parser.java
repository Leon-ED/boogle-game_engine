package fr.uge;

import org.json.JSONArray;
import org.json.JSONObject;

public record Parser() {

    public void JSON(StringBuilder page, String title, String text) {
        JSONObject json = new JSONObject();
        json.put("title", title);

        JSONObject definitions = new JSONObject();
        JSONArray definitionsList = new JSONArray();
        Boolean lookingForDefinition = false;

        for (String line : text.split("\n")) {

            if (line.contains(" |fr|") || line.endsWith("|fr}} ===")) {
                String[] category = line.split("\\|");
                if (category.length == 3) {
                    definitions.put(category[1], definitionsList);
                    lookingForDefinition = true;

                }
            }
            if (line.startsWith("=== {{") && !line.contains("|fr|") && !line.endsWith("|fr}} ===")) {
                lookingForDefinition = false;
                definitionsList = new JSONArray();
            }

            if (lookingForDefinition && line.startsWith("# ")) {
                definitionsList.put(line.substring(2));
            }

        }
        page.append(json.put("definitions", definitions).toString());
        page.append("\n");
    }

}
