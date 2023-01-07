package fr.uge.jdict;

import java.io.File;
import java.nio.file.Path;

public class DictionaryMaker {

    public static void main(String[] args) {
        String bzip_PATH;
        String json_PATH;
        String langue;
        long time = System.currentTimeMillis();

        if (args.length < 4) {
            System.out.println("Usage: DictionaryMaker <langue> definitions <input.xml.bzip2> <output>");
            // System.exit(1);
            // return;
            bzip_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dump-wikipedia.xml.bz2";
            // String json_PATH =
            // "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\dump-wikipedia2.json";
            json_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\dump-test.json";
            langue = "fr";
        } else {
            if (!args[1].equals("definitions"))
                System.exit(1);
            bzip_PATH = args[2];
            json_PATH = args[3];

            langue = args[0];
            if(!langue.equals("fr")){
                System.out.println("Seulement le français est supporté pour le moment");
                System.exit(1);
            }

        }
        File bzipFILE = new File(bzip_PATH);
        if (!bzipFILE.exists() || !bzipFILE.isFile()) {
            System.out.println("Le fichier " + bzip_PATH + " n'existe pas");
            System.exit(1);
        }
        System.out.println("Début du parsing");
        String index_PATH = Path.of(json_PATH).getParent().toString() + File.separator +"dictionary.index";
        System.out.println("index_PATH: " + index_PATH);
        IndexMaker.getIndexMaker(index_PATH,
                json_PATH);
        try {
            XMLManager.exportToFile(bzip_PATH, json_PATH, OutputFormat.JSON, langue);

        } catch (Exception e) {
            System.out.println("Erreur lors du parsing du fichier XML");
            e.printStackTrace();
            System.exit(1);
        }
        try {
            IndexMaker.getIndexMaker().writeIndex();
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'index");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Temps d'execution: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println("Min: " + (System.currentTimeMillis() - time) / 60000.000 + "min");
        System.out.println("SECONDES : " + (System.currentTimeMillis() - time) / 1000.000 + "s");

    }
}
