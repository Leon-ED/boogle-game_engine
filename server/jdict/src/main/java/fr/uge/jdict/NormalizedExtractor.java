package fr.uge.jdict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.json.JSONObject;

public class NormalizedExtractor {

    public static void main(String[] args) {
        String definition_PATH = null;
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
        // convert definitionsCategories to ArrayList

        File definition_FILE = new File(definition_PATH);
        if (!definition_FILE.exists() || definition_FILE.isDirectory()) {
            System.out.println("JDICT : Le fichier de définitions n'existe pas");
            return;
        }
        extract(definitionsCategories, definition_PATH, stdoutput);

    }

    private static void extract(String categoriesARG, String definitions_PATH, boolean stdoutput) {
        String language = null;
        ArrayList<String> categoriesList =  categoriesARG == null ? null : new ArrayList<>(Arrays.asList(categoriesARG.split(",")));
        TreeSet<String> wordSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DictionaryNormalizer.compareNormalized(o1, o2);
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

                if(categoriesList == null) {
                    String normalizedWord = DictionaryNormalizer.normalize(definition.getString("title"));
                    wordSet.add(normalizedWord);
                    continue;
                }

                categoriesList.stream().map(String::trim).filter(categoriesJSON::has).findFirst().ifPresent(s -> {
                    String normalizedWord = DictionaryNormalizer.normalize(definition.getString("title"));
                    wordSet.add(normalizedWord);
                });




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
            String saveFile_PATH = Paths.get(definitions_PATH).getParent().toString() + File.separator + "words_list_"
                    + language
                    + ".txt";
            saveToFile(wordSet, Path.of(saveFile_PATH));
        }

    }

    private static void saveToFile(TreeSet<String> wordSet, Path saveFile_PATH) {
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
