package fr.uge.jdict;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.json.JSONObject;

class XMLManager {

    static XMLStreamReader open(String path) {
        InputStream file;
        XMLStreamReader eventReader;

        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier n'existe pas, vérifiez le chemin");
            System.exit(1);
            return null;
        }
        // utf 8

        try {

            if (path.endsWith(".bz2")) {
                CompressorInputStream gzippedOut = new CompressorStreamFactory()
                        .createCompressorInputStream(CompressorStreamFactory.BZIP2, file);
                XMLInputFactory factory = XMLInputFactory.newInstance();
                eventReader = factory.createXMLStreamReader(gzippedOut, "UTF-8");
            } else {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                eventReader = factory.createXMLStreamReader(file, "UTF-8");
            }
        } catch (Exception e) {
            System.out.println("Le fichier a bien été trouvé mais il y a eu une erreur lors de son ouverture");
            System.exit(1);
            return null;
        }
        return eventReader;

    }

    public static boolean isValidtitle(String title) {
        HashSet<Character> BANNED_CHARS = new HashSet<>(Arrays.asList(':', '/', '\\', '?', '*', '<', '>', '|', '"', ' ',
                '-', '_', ')','`','’','·',
                '(', '[', ']', '{', '}', '!', '@', '#',
                '$', '%', '^', '&', '*', '=', '+', '`', '~', ';', '.', ',', '\'', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9'));
        return !BANNED_CHARS.stream().anyMatch(c -> title.contains(c + ""));

    }
    public static boolean isFrenchWord(String title){
        // return true if the string only contains latin characters
        title = DictionaryNormalizer.lightNormalize(title);
        return title.chars().allMatch(c -> (c >= 65 && c <= 90) || (c >= 97 && c <= 122));


    }

    private static BufferedWriter openOutputFile(String outputPath) {
        try {
            return Files.newBufferedWriter(Path.of(outputPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du fichier de sortie");
            System.exit(1);
            return null;
        }
    }

    @SuppressWarnings("unused")
    static void exportToFile(String inputPath, String outputPath, OutputFormat format, String langue)
            throws XMLStreamException {
        XMLStreamReader streamReader = open(inputPath);

        Boolean inPage = false;
        Boolean toParse = false;
        String title = "";

        StringBuilder textPage = new StringBuilder();
        StringBuilder exportText = new StringBuilder();
        Parser parser = new Parser(format);

        int pageCounter = 0;
        String tagName = "";

        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmss'Z'");
        String formattedDate = now.format(formatter);

        JSONObject header = new JSONObject().put("description", "definition file").put("created_on", formattedDate)
                .put("language", langue);
        exportText.append(header.toString() + "\n");
        IndexMaker.getIndexMaker().setOffset(exportText.length());

        try (BufferedWriter writer = openOutputFile(outputPath)) {
            writer.write(exportText.toString());
            while (streamReader.hasNext()) {
                int event = streamReader.next();
                // On cherche le début d'une page
                if (event == XMLEvent.START_ELEMENT) {

                    tagName = streamReader.getLocalName();
                    if (tagName.equals("page")) {
                        inPage = true;

                    }
                    // On sauvegarde le titre de la page
                    if (tagName.equals("title") && inPage) {
                        title = DictionaryNormalizer.lightNormalize(streamReader.getElementText());
                        // On ne le prends pas s'il contient un espace, un ou plusieurs chiffres ou s'il
                        // fait moins de 2

                        if (!isValidtitle(title) && !isFrenchWord(title) || title.length() < 2) {
                            title = "";
                            inPage = false;
                            continue;
                        }
                    }

                    // Au autorise la lecture de la page car elle est ns = 0
                    if (tagName.equals("ns")
                            && streamReader.getElementText().equals("0") && inPage) {
                        // System.out.println("ns = 0");
                        toParse = true;
                    }
                    // On sauvegarde le texte de la page
                    if (tagName.equals("text") && toParse) {
                        // La page n'est pas en français : pas intéressante
                        String text = streamReader.getElementText();
                        if (!text.contains("== {{langue|" + langue + "}} ==")) {
                            text = "";
                            toParse = false;
                            inPage = false;
                            continue;
                        }
                        // System.out.println(title);
                        textPage.append(text);

                        toParse = false;
                        // parser.formatExport(exportText, title, textPage.toString());
                        pageCounter++;
                        writer.write(parser.toJSON(exportText, title, textPage.toString(), langue));
                        exportText.setLength(0);
                        // if (pageCounter == 2_500) {
                        // writer.append(exportText.toString());
                        // exportText.setLength(0);
                        // }
                        // if (pageCounter == 5000) {
                        // break;
                        // }

                    }

                    // On a fini de lire la page
                } else if (event == XMLEvent.END_ELEMENT && streamReader.getLocalName().equals("page")) {
                    inPage = false;
                    textPage.setLength(0);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'écriture du fichier de sortie");
            System.exit(1);
        }

    }

}
