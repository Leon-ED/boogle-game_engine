package fr.uge;

import javax.xml.stream.XMLStreamException;

public class App {

    public static void main(String[] args) {
        // list all files in the directory from the path

        String input_PATH = "./server/files/dump-wikipedia.xml.bz2";
        String output_PATH = "./server/files/dumps/dump-wikipedia.json";
        if (args.length == 2) {
            input_PATH = args[0];
            output_PATH = args[1];
        }

        try {
            long time = System.currentTimeMillis();
            System.out.println("Début du parsing");
            XMLManager.exportToFile(input_PATH, output_PATH, OutputFormat.JSON);
            System.out.println("Temps d'execution: " + (System.currentTimeMillis() - time) + "ms");
            System.out.println("Min: " + (System.currentTimeMillis() - time) / 60000.000 + "min");
            System.out.println("SECONDES : " + (System.currentTimeMillis() - time) / 1000.000 + "s");

        } catch (XMLStreamException e) {
            System.out.println("Erreur lors du parsing du fichier XML");
            return;
        }
    }

}