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

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class App {

    public static void main(String[] args) throws CompressorException, FileNotFoundException, XMLStreamException {
        // list all files in the directory from the path

        String path = "./server/files/dump-wikipedia.xml.bz2";
        // FilesINDirectory(path);
        InputStream file;
        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier n'existe pas, vérifiez le chemin");
            return;
        }

        CompressorInputStream gzippedOut = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, file);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(gzippedOut);

        String title = "";
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
                // On vérifie que la parsedPage a comme namespace 0, sinon on passe à la parsedPage suivante
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

            if(!parsedPage.toString().contains("== {{langue|fr}} ==")){
                continue;
            }
            //System.out.println("====================== TITRE : " + title + " ======================");
            parsePage(parsedPage.toString(), title);
            // if(title.toUpperCase().equalsIgnoreCase("lire")){
            //     dumpPageText(parsedPage.toString(), title);
            //     return;
            // }

        }
    }

   static void dumpPageText(String parsedPage, String title) throws XMLStreamException {
        BufferedWriter writer;
        try {
            // Pour écrirer dans le fichier en utf8
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./server/files/dumpText.txt", false), StandardCharsets.UTF_8));
            writer.write(title + "\n");
            for (String ligne : parsedPage.split("\n")) {
                System.out.println(ligne);
                writer.write(ligne + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la fermeture du fichier");
            return;
        }
    }

    static void parsePage(String parsedPage,String title) throws XMLStreamException {
        BufferedWriter writer;
        Boolean getHashtags = false;
        
        try{
        // Pour écrirer dans le fichier en utf8
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./server/files/dump-wikipedia.txt", true), StandardCharsets.UTF_8));
       
        writer.write(title+"\n");
        for (String ligne : parsedPage.split("\n")) {
            System.out.println(ligne);
            if(ligne.startsWith("=== {{") && ligne.endsWith("}} ===") && ligne.contains("|fr}}")){
                if(ligne.contains("verbe")){
                    writer.write("\tverbe\n");
                    getHashtags = true;
                }else if(ligne.contains("nom")){
                    writer.write("\tnom\n");
                    getHashtags = true;
                }
            }else if (ligne.startsWith("=== {{") && ligne.endsWith("}} ===") && !ligne.contains("|fr")){
                getHashtags = false;
            }
            // Si on est dans une section et que la ligne commence exclusivement par # on l'écrit dans le fichier
            if(getHashtags && ligne.startsWith("#") && !ligne.startsWith("#*")){
                ligne = ligne.substring(1);
                writer.write("\t\t-"+ligne + "\n");
            }
        }

            writer.close();
        }catch(Exception e){
            System.out.println("Erreur lors de la fermeture du fichier");
            return;
        }
            

            
        


    
    
   
    }
}
