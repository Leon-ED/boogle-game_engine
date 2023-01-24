package fr.uge.jdict;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.json.JSONObject;

public class DictionarySearcher {

    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding","UTF-8");
        DictionarySearcher searcher = new DictionarySearcher();
        String definitions_PATH = null;
        String index_PATH = null;
        String word = "";
        OutputFormat format = OutputFormat.JSON;
        if (args.length < 4) {
            System.out.println(
                    "Usage: DictionarySearcher definitions <format:word> <fichier index> <fichier definitions>");
            System.exit(1);
        } else {
            if (!args[0].equals("definitions")) {
                System.out.println(args[0]);
                throw new IllegalArgumentException("Le premier argument doit être 'definitions'");
            }
            word = args[1];
            if (word.contains(":")) {
                String[] split = word.split(":");
                word = split[1];
                format = OutputFormat.valueOf(split[0].toUpperCase());
            }
            index_PATH = args[2];
            definitions_PATH = args[3];
        }
        File index_FILE = new File(index_PATH);
        if (!index_FILE.exists() || !index_FILE.isFile()) {
            System.out.println("Le fichier INDEX: " + index_PATH + " n'existe pas");
            System.exit(1);
        }
        File definitions_FILE = new File(definitions_PATH);
        if (!definitions_FILE.exists() || !definitions_FILE.isFile()) {
            System.out.println("Le fichier DEFINITIONS: " + definitions_PATH + " n'existe pas");
            System.exit(1);
        }
        index_FILE = null;
        definitions_FILE = null;
        searcher.search(definitions_PATH, index_PATH, word, format);

    }

    public void search(String definitions_PATH, String index_PATH, String word, OutputFormat format) throws Exception {
        RandomAccessFile indexFile = new RandomAccessFile(index_PATH, "r");
        boolean searchSiblings = (word.equals(word.toUpperCase()));
        // On vérifie que le fichier d'index est valide
        byte[] magic = new byte[8];
        indexFile.read(magic);
        if (!new String(magic).equals("DICTINDX")) {
            indexFile.close();
            throw new IllegalStateException("Le fichier INDEX est invalide (aucun en-tête trouvé)");
        }
        RandomAccessFile definitionsFile = new RandomAccessFile(definitions_PATH, "r");
        // Si le mot est en majuscule, on le considère comme normalisé, on retournera
        // alors
        // tous les mots qui correspondent à la normalisation du mot

        getDefinition(indexFile, definitionsFile, word, searchSiblings, format);

        indexFile.close();
        definitionsFile.close();
        System.exit(0);
    }

    public void getDefinition(RandomAccessFile index, RandomAccessFile definitions, String word, boolean searchSiblings,
            OutputFormat format) {
                
        if (searchSiblings) {
            word = DictionaryNormalizer.normalize(word);
        }
        String normalizedTitle = "";
        try {
            int startPos = 0;
            int endPos = (int) (index.length() / 8) - 1;

            while (startPos <= endPos) {
                int midPos = startPos + (endPos - startPos) / 2;
                index.seek(midPos * 8);
                int start = index.readInt();
                int end = index.readInt();
                if (start >= definitions.length() || end >= definitions.length()) {
                    break;
                }
                definitions.seek(start);
                byte[] b = new byte[end - start + 1];
                definitions.read(b);
                // String definition = new String(definitions.readLine().getBytes(),
                // StandardCharsets.UTF_8);
                String definition = new String(b, "UTF-8");

                // System.out.println("definition : " + definition);
                JSONObject json = new JSONObject(definition);
                String title = json.getString("title");

                // Si on veut chercher les mêmes mots normalisés
                if (searchSiblings) {
                    normalizedTitle = DictionaryNormalizer.normalize(title);

                } else {
                    normalizedTitle = title;
                }
                // System.out.println("word : " + word + " title : " + normalizedTitle);

                if (normalizedTitle.equals(word)) {
                    // System.out.println("Word found : ");
                    // System.out.println(json.toString());
                    System.out.println(OutputFormat.convert(json.toString(), format));

                    // On a trouvé notre élément et on ne veut pas de frères
                    if (!searchSiblings) {
                        return;
                    } else {
                        searchSiblingsDefinitions(index, definitions, word, midPos, format);
                        return;
                    }
                }

                if (DictionaryNormalizer.compareNormalized(word, normalizedTitle) > 0) {
                    startPos = midPos + 1;
                } else {
                    endPos = midPos - 1;
                }
            }

            System.out.println("Aucune définition trouvée pour le mot " + word + "");
            System.exit(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchSiblingsDefinitions(RandomAccessFile index, RandomAccessFile definitions, String word, int midPos,
            OutputFormat format) throws IOException {
        // lit les prochains index pour trouver les frères
        int start = midPos + 1;
        while (true) {
            // on lit l'index
            index.seek(start * 8);
            int startDef = index.readInt();
            int endDef = index.readInt();
            byte[] b = new byte[endDef - startDef + 1];
            definitions.read(b);
            // String definition = new String(definitions.readLine().getBytes(),
            // StandardCharsets.UTF_8);
            String definition = new String(b, "UTF-8");
            JSONObject json = new JSONObject(definition);
            String title = json.getString("title");
            String normalizedTitle = DictionaryNormalizer.normalize(title);
            // System.out.println("word : " + word + " title : " + normalizedTitle);
            if (normalizedTitle.equals(word)) {
                // System.out.println("Word found : ");
                System.out.println(OutputFormat.convert(json.toString(), format));

            } else {
                break;
            }
            start++;
        }
        start = midPos - 1;
        while (true) {
            index.seek(start * 8);
            int startDef = index.readInt();
            int endDef = index.readInt();
            byte[] b = new byte[endDef - startDef + 1];
            definitions.read(b);
            // String definition = new String(definitions.readLine().getBytes(),
            // StandardCharsets.UTF_8);
            String definition = new String(b, "UTF-8");
            JSONObject json = new JSONObject(definition);
            String title = json.getString("title");
            String normalizedTitle = DictionaryNormalizer.normalize(title);
            // System.out.println("word : " + word + " title : " + normalizedTitle);
            if (normalizedTitle.equals(word)) {
                // System.out.println("Word found : ");
                System.out.println(OutputFormat.convert(json.toString(), format));

            } else {
                break;
            }
            start--;
        }

    }
}