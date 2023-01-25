package fr.uge.jdict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

class CharTableMaker {

    public static void main(String[] args) {
        String definition_PATH = null;
        String output_PATH = null;
        boolean stdoutput = false;
        if (System.console() == null) {
            stdoutput = true;
        }
        if (args.length < 1) {
            System.out.println("Usage: CharTableMaker <input.json>");
            System.exit(1);
            return;
        } else if (args.length >= 2) {
            definition_PATH = args[0];
        } else {
            definition_PATH = args[0];
        }
        File definition_FILE = new File(definition_PATH);
        if (!definition_FILE.exists() || definition_FILE.isDirectory()) {
            System.out.println("JDICT : Le fichier de définitions n'existe pas");
            return;
        }
        if (args.length >= 2) {
            output_PATH = args[1];
        } else {
            output_PATH = Paths.get(definition_PATH).getParent().toString() + File.separator + "frequences.txt";
        }

        TreeMap<String, Float> freqs = createCharTable(definition_PATH);
        if (freqs == null) {
            System.out.println("JDICT : Une erreur a eu lieu lors de la création de la table de fréquences");
        }
        if (stdoutput) {
            freqs.forEach((k, v) -> System.out.println(k + "  " + v));
        } else {
            writeCharTable(freqs, output_PATH);
        }
    }

    private static void convertToPercent(TreeMap<String, Float> charTable) {
        int sum = 0;
        for (String key : charTable.keySet()) {
            sum += charTable.get(key);
        }
        for (String key : charTable.keySet()) {
            charTable.put(key, charTable.get(key) * 100.0f / sum);
        }
    }

    /***
     * {@summary} Crée une table de fréquences des lettres et des paires de lettres
     * 
     * @param definition_PATH : chemin du fichier de définitions
     * @return TreeMap<String, Integer> : table de fréquences des lettres et des
     *         paires de lettres triée par ordre alphabétique
     */
    private static TreeMap<String, Float> createCharTable(String definition_PATH) {
        TreeMap<String, Float> charTable = new TreeMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(definition_PATH))) {
            while (reader.readLine() != null) {

                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.toUpperCase();
                for (int i = 0; i < line.length(); i++) {
                    char character = line.charAt(i);
                    if (!Character.isLetter(character) || !isPrintable(character)) {
                        continue;
                    }

                    String elem = character + "";
                    if (line.charAt(i) == 'Q' && line.charAt(i + 1) == 'U') {
                        elem = "QU";
                    }

                    if (charTable.containsKey(elem)) {
                        charTable.put(elem, charTable.get(elem) + 1.0f);
                    } else {
                        charTable.put(elem, 1.0f);
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

    /***
     * {@summary} Ecrit la table de fréquences dans un fichier
     * 
     * @param charTable   TreeMap<String, Integer> : table de fréquences des lettres
     *                    et des paires de lettres triée par ordre alphabétique
     * @param output_PATH : chemin du fichier de sortie
     */
    private static void writeCharTable(TreeMap<String, Float> charTable, String output_PATH) {
        convertToPercent(charTable);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(output_PATH))) {
            for (String key : charTable.keySet()) {
                if (!isPrintable(key.charAt(0))) {
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

    /***
     * {@summary} Vérifie si un caractère doit être affiché ou non
     * 
     * @param c char : caractère à vérifier
     * @return boolean : true si le caractère est imprimable, false sinon
     */
    private static boolean isPrintable(char c) {
        return c >= 32 && c <= 126;
    }

}
