package fr.uge.jdict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONObject;

public class NormalizedExtractor {

    public static void main(String[] args) {
        String definition_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\dump-test.json";
        boolean stdoutput = false;
        String definitionsCategories = null;
        // On teste si on récupère la sortie standard
        if (System.console() == null) {
            stdoutput = true;
        }
        if (args.length < 2) {
            System.out.println("Usage NormalizedExtractor definitions <fichier definitions> [categories]");
            System.exit(1);
        }
        if (args.length >= 2) {
            definition_PATH = args[1];
        }
        if (args.length >= 3) {
            definitionsCategories = args[2];
        }

        extract(definitionsCategories, definition_PATH, stdoutput);

    }

    private static void extract(String categoriesARG, String definitions_PATH, boolean stdoutput) {
        String language = null;
        String categories[] = categoriesARG == null ? null : categoriesARG.split(","); 
        Set<String> wordSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DictionaryNormalized.compareNormalized(o1, o2);
            }
        });
        String line;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(definitions_PATH), StandardCharsets.UTF_8)) {
            while ((line = reader.readLine()) != null) {

                JSONObject definition = new JSONObject(line);
                if (!definition.has("title")) {
                    language = definition.getString("language");
                    continue;
                }
                JSONObject categoriesJSON = definition.getJSONObject("definitions");
                if(categories == null){
                    String normalizedWord = DictionaryNormalized.normalize(definition.getString("title"));
                    wordSet.add(normalizedWord);
                    continue;
                }
                for (String type : categories) {
                    if (categoriesJSON.has(type.trim())) {
                        String normalizedWord = DictionaryNormalized.normalize(definition.getString("title"));
                        wordSet.add(normalizedWord);
                        break;
                    }

                }

            }

        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du fichier");
            e.printStackTrace();
            System.exit(1);
        }

        if (stdoutput) {
            for (String word : wordSet) {
                System.out.println(word);
            }
        } else {
            String saveFile_PATH = Paths.get(definitions_PATH).getParent().toString() + File.separator +"words_list_" + language
                    + ".txt";
            saveToFile(wordSet, Path.of(saveFile_PATH));
        }

    }

    private static void saveToFile(Set<String> wordSet, Path saveFile_PATH) {
        try (BufferedWriter writer = Files.newBufferedWriter(saveFile_PATH, StandardCharsets.UTF_8)) {
            for (String word : wordSet) {
                writer.write(word);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du fichier");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
