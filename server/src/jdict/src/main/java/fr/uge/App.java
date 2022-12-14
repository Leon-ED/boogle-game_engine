package fr.uge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
        int a = 0;
        String title = "";
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            a++;
            if (a == 1000000) {
                break;
            }

            if (event.isStartElement()) {
                // On récupère le titre de la page
                var startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("title")) {
                    event = eventReader.nextEvent();
                    if (event.isCharacters()) {
                        title = event.asCharacters().getData();
                    }
                }
                // On vérifie que la page a comme namespace 0, sinon on passe à la page suivante
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
            StringBuilder sb = new StringBuilder();

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
                sb.append(event.asCharacters().getData());

            }

            if (!sb.toString().contains("== {{langue|fr}} ==")) {
                continue;
            }
            System.out.println("title == " + title);
            System.out.println(sb.toString());
            return;

        }
    }

    static String isNs0(XMLEventReader eventReader) throws XMLStreamException {
        var title = "";
        while (eventReader.hasNext()) {
            var event = eventReader.nextEvent();

            // get title of the page
            if (event.isStartElement()) {
                var startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("title")) {
                    event = eventReader.nextEvent();
                    if (event.isCharacters()) {
                        title = event.asCharacters().getData();
                    }
                }
            }
            if (event.isStartElement()) {
                var startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("ns")) {
                    event = eventReader.nextEvent();
                    if (event.isCharacters()) {
                        if (event.asCharacters().getData().equals("0")) {
                            // System.out.println("ns is 0 "+event.asCharacters().getData());
                            return title;

                        }
                    }
                }
            }

        }
        return "";

    }

    static void parsePage(XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            var event = eventReader.nextEvent();
            if (event.isEndElement()) {
                var endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals("page")) {
                    return;
                }
            }
            if (event.isStartElement()) {
                var startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("title")) {
                    event = eventReader.nextEvent();
                    if (event.isCharacters()) {
                        System.out.println("title " + event.asCharacters().getData());
                    }
                }
                if (startElement.getName().getLocalPart().equals("revision")) {
                    parseRevision(eventReader);
                }
            }

        }
    }

    static void parseRevision(XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            var event = eventReader.nextEvent();
            if (event.isEndElement()) {
                var endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals("revision")) {
                    return;
                }
            }
            if (event.isStartElement()) {
                var startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("text")) {
                    event = eventReader.nextEvent();
                    // if (event.isCharacters()) {
                    // System.out.println("text "+event.asCharacters().getData());
                    // }
                }
            }

        }
    }

    static void FilesINDirectory(String path) {
        File folder = new File(path);
        System.out.println(folder.getName());
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

}
