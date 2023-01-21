package fr.uge.jdict;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.stream.XMLStreamException;

public class DictionaryMaker {

    public static void main(String[] args) {
        String bzip_PATH;
        String json_PATH;
        String langue;
        long time = System.currentTimeMillis();

        if (args.length < 4) {
            System.out.println(
                    "Usage: DictionaryMaker <langue> definitions <input.xml.bz2 | input.xml > <fichier_sortie>");
            System.exit(1);
        }
        if (!args[1].equals("definitions"))
            System.exit(1);

        if (!args[0].equals("fr")) {
            System.out.println("Seulement le français est supporté pour le moment");
            System.exit(1);
        }
        bzip_PATH = args[2];
        json_PATH = args[3];
        langue = args[0];

        File bzipFILE = new File(bzip_PATH);
        if (!bzipFILE.exists() || !bzipFILE.isFile()) {
            System.out.println("Le fichier " + bzip_PATH + " n'existe pas");
            System.exit(1);
        }

        System.out.println("Début du parsing");
        String index_PATH = Path.of(json_PATH).getParent().toString() + File.separator + "dictionary.index";
        IndexMaker.getIndexMaker(index_PATH, json_PATH);
        try {
            XMLManager.exportToFile(bzip_PATH, json_PATH, OutputFormat.JSON, langue);

        } catch (XMLStreamException e) {
            System.out.println("Erreur lors du parsing du fichier XML");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.print("Fin du parsing en :" + (System.currentTimeMillis() - time) / 60000.000 + " min, ");
        System.out.println("soit : " + (System.currentTimeMillis() - time) / 1000.000 + "s");
        System.out.println("Début de la création de l'index");

        try {
            IndexMaker.getIndexMaker().writeIndex();
        } catch (IOException e) {
            System.out.println("Erreur lors de la création de l'index");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Fin, temps d'exécution total : " + (System.currentTimeMillis() - time) / 60000.000 + " min");

    }
}
