package fr.uge;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

class XMLManager {

    static XMLStreamReader open(String path) {
        InputStream file;
        XMLStreamReader eventReader;

        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier n'existe pas, vérifiez le chemin");
            return null;
        }

        try {
            CompressorInputStream gzippedOut = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.BZIP2, file);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            eventReader = factory.createXMLStreamReader(gzippedOut);
        } catch (Exception e) {
            System.out.println("Le fichier a bien été trouvé mais il y a eu une erreur lors de sa lecture");
            return null;
        }
        return eventReader;

    }

    static void parse(String inputPath, String outputPath, OutputFormat format) throws XMLStreamException {
        switch (format) {
            case TXT:
                // parseToTXT(inputPath,outputPath);
                break;
            case JSON:
                parseToJSON(inputPath, outputPath);
                break;
            case YAML:
                // parseToYAML(inputPath,outputPath);
                break;
            default:
                break;
        }

    }

    private static BufferedWriter openOutputFile(String outputPath) {
        try {
            // Pour écrirer dans le fichier en utf8
            // charset utf 8
            // StandardOpenOption.CREATE : créer le fichier s'il n'existe pas
            // StandardOpenOption.TRUNCATE_EXISTING : si le fichier existe, le tronquer
            // buffer size : 8192
            // set buffer size to 8199
            return Files.newBufferedWriter(Path.of(outputPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du fichier de sortie");
            return null;
        }
    }

    private static void parseToJSON(String inputPath, String outputPath) {
        XMLStreamReader streamReader = open(inputPath);

        Boolean inPage = false;
        Boolean toParse = false;
        String title = "";

        StringBuilder textPage = new StringBuilder();
        StringBuilder jsonPage = new StringBuilder();
        Parser parser = new Parser();

        int PAGE_BEFORE_WRITING = 10;
        int pageCounter = 0;

        try (BufferedWriter writer = openOutputFile(outputPath)) {

            while (streamReader.hasNext()) {
                int event = streamReader.next();
                // On cherche le début d'une page
                if (event == XMLEvent.START_ELEMENT && streamReader.getLocalName().equals("page")) {
                    inPage = true;
                }
                // On sauvegarde le titre de la page
                if (event == XMLEvent.START_ELEMENT && streamReader.getLocalName().equals("title") && inPage) {
                    title = streamReader.getElementText();
                }
                // Au autorise la lecture de la page car elle est ns = 0
                if (event == XMLEvent.START_ELEMENT && streamReader.getLocalName().equals("ns")
                        && streamReader.getElementText().equals("0") && inPage) {
                    // System.out.println("ns = 0");
                    toParse = true;
                }
                // On sauvegarde le texte de la page
                if (event == XMLEvent.START_ELEMENT && streamReader.getLocalName().equals("text") && toParse) {
                    // La page n'est pas en français : pas intéressante
                    String text = streamReader.getElementText();
                    if (!text.contains("== {{langue|fr}} ==")) {
                        toParse = false;
                        inPage = false;
                        continue;
                    }
                    // System.out.println(title);
                    textPage.append(text);

                    toParse = false;
                    parser.JSON(jsonPage, title, textPage.toString());
                    pageCounter++;

                    if (pageCounter == PAGE_BEFORE_WRITING) {
                        writer.write(jsonPage.toString());
                        jsonPage.setLength(0);
                        pageCounter = 0;
                    }
                }
                // On a fini de lire la page
                if (event == XMLEvent.END_ELEMENT && streamReader.getLocalName().equals("page")) {
                    inPage = false;
                    textPage.setLength(0);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String parsePageToTXT(String parsedPage, String title) {

        Boolean getHashtags = false;
        StringBuilder result = new StringBuilder();
        try {
            // Pour écrirer dans le fichier en utf8

            result.append(title + "\n");
            for (String ligne : parsedPage.split("\n")) {

                // Si la ligne contient le pattern === {{type|fr}} ===
                if (ligne.startsWith("=== {{") && ligne.endsWith("}} ===") && ligne.contains("|fr}}")) {
                    // On ajoute le type (nom,verbe,adjectif, etc...) à la ligne
                    result.append("\t" + ligne.split("\\|")[1] + "\n");
                    // On accepte de lire les lignes commencant par # à partir de maintenant
                    getHashtags = true;

                    // Si la ligne ne contient pas le pattern === {{type|fr}} ===, on ne veut plus
                    // lire les lignes commencant par #
                } else if (ligne.startsWith("=== {{") && ligne.endsWith("}} ===") && !ligne.contains("|fr")) {
                    getHashtags = false;
                }
                // Si la ligne commence par # et qu'on a accepté de lire les lignes commencant
                // par #, on ajoute la ligne à la liste des mots
                if (getHashtags && ligne.startsWith("#") && !ligne.startsWith("#*")) {
                    ligne = ligne.substring(1);
                    result.append("\t\t-" + ligne + "\n");
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la fermeture du fichier");
            e.printStackTrace();
            return null;
        }
        return result.toString();

    }

}