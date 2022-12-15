package fr.uge;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

class XMLManager {

    static XMLEventReader open(String path) {
        InputStream file;
        XMLEventReader eventReader;

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
            eventReader = factory.createXMLEventReader(gzippedOut);
        } catch (Exception e) {
            System.out.println("Le fichier a bien été trouvé mais il y a eu une erreur lors de sa lecture");
            return null;
        }
        return eventReader;

    }

    static void parse(XMLEventReader eventReader,String outputPath) throws XMLStreamException {
        String title = "";
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8))){
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
    
                if (event.isStartElement()) {
                    // On récupère le titre de la parsedPage
                    var startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals("title")) {
                        event = eventReader.nextEvent();
                        if (event.isCharacters()) {
                            title = event.asCharacters().getData();
                        }
                    }
                    // On vérifie que la parsedPage a comme namespace 0, sinon on passe à la
                    // parsedPage suivante
                    if (startElement.getName().getLocalPart().equals("ns")) {
                        event = eventReader.nextEvent();
                        if (event.isCharacters()) {
                            if (!event.asCharacters().getData().equals("0")) {
                                continue;
    
                            }
                        }
                    }
                }
    
                // Tant qu'il n'y a pas de balise ouvrante on continue à lire
                if (!event.isStartElement()) {
                    continue;
                }
                var startElement = event.asStartElement();
                StringBuilder parsedPage = new StringBuilder();
    
                // Si la balise n'est pas un texte, ça ne nous intéresse pas
                if (!startElement.getName().getLocalPart().equals("text")) {
                    continue;
                }
    
                // On récupère tout le texte entre les balises <text></text>
                while (eventReader.hasNext()) {
                    event = eventReader.nextEvent();
                    if (event.isEndElement()) {
                        var endElement = event.asEndElement();
                        if (endElement.getName().getLocalPart().equals("text")) {
                            break;
                        }
                    }
                    parsedPage.append(event.asCharacters().getData());
    
                }
    
                if (!parsedPage.toString().contains("== {{langue|fr}} ==")) {
                    continue;
                }
                // System.out.println("====================== TITRE : " + title + "
                // ======================");
                String parsedResult = parsePage(parsedPage.toString(), title);
                if(parsedResult != null){
                    writer.write(parsedResult);
                }else{
                    throw new Exception("Erreur lors de l'écriture du fichier");
                }
                // if(title.toUpperCase().equalsIgnoreCase("lire")){
                // dumpPageText(parsedPage.toString(), title);
                // return;
                // }
            }
        }catch(Exception e){
            System.out.println("Erreur lors de l'écriture du fichier");
            return;
        }


    
        
    }

    private static String parsePage(String parsedPage, String title)  {
        
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
                
                // Si la ligne ne contient pas le pattern === {{type|fr}} ===, on ne veut plus lire les lignes commencant par #
                } else if (ligne.startsWith("=== {{") && ligne.endsWith("}} ===") && !ligne.contains("|fr")) {
                    getHashtags = false;
                }
                // Si la ligne commence par # et qu'on a accepté de lire les lignes commencant par #, on ajoute la ligne à la liste des mots
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