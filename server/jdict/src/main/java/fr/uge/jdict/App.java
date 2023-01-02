package fr.uge.jdict;

import javax.xml.stream.XMLStreamException;

public class App {

    public static void main(String[] args) {
        // list all files in the directory from the path

        String input_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dump-wikipedia.xml.bz2";
        String output_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\dump-wikipedia.json";    
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