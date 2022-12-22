package fr.uge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class CharTableMaker {

    public static void main(String[] args) {
        String definition_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\dump-wikipedia.json";
        String output_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\frequences.txt";
        System.out.println("JDICT : Création de la table de fréquences");
        if (args.length == 2) {
            definition_PATH = args[0];
            output_PATH = args[1];
        }
        TreeMap<String, Integer> freqs = createCharTable(definition_PATH, output_PATH);
        if (freqs == null) {
            System.out.println("JDICT : Une erreur a eu lieu lors de la création de la table de fréquences");
        }
        writeCharTable(freqs, output_PATH);

    }

    private static TreeMap<String, Integer> createCharTable(String definition_PATH, String output_PATH) {
        TreeMap<String, Integer> charTable = new TreeMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(definition_PATH))) {
            while (reader.readLine() != null) {
                String line = reader.readLine().toUpperCase();
                for (int i = 0; i < line.length(); i++) {
                    char character = line.charAt(i);
                    if (!Character.isLetter(character)) {
                        continue;
                    }
                    String elem = character + "";
                    if (line.charAt(i) == 'Q' && line.charAt(i + 1) == 'U') {
                        elem = "QU";
                    }

                    if (charTable.containsKey(elem)) {
                        charTable.put(elem, charTable.get(elem) + 1);
                    } else {
                        charTable.put(elem, 1);
                    }
                }
            }
            return charTable;
        } catch (Exception e) {
            System.out.println(
                    "JDICT : Une erreur a eu lieu lors de la lecture du fichier de définitions" + definition_PATH);
            e.printStackTrace();
        }
        return null;

    }

    private static void writeCharTable(TreeMap<String, Integer> charTable, String output_PATH) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(output_PATH))) {
            for (String key : charTable.keySet()) {
                if(!isPrintable(key.charAt(0))){
                    continue;
                }
                writer.write(key + " " + charTable.get(key) + " \n");
            }

        } catch (Exception e) {
            System.out
                    .println("JDICT : Une erreur a eu lieu lors de l'écriture du fichier de fréquences" + output_PATH);
            e.printStackTrace();
        }
    }

    private static boolean isPrintable(char c) {
        return c >= 32 && c <= 126;
    }

}
