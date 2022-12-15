package fr.uge;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class App {

    public static void main(String[] args){
        // list all files in the directory from the path
        String input_PATH = "./server/files/dump-wikipedia.xml.bz2";
        String output_PATH = "./server/files/dumps/dump-wikipedia.txt";
        if(args.length == 2){
            input_PATH = args[0];
            output_PATH = args[1];
        }
        XMLEventReader eventReader = XMLManager.open(input_PATH);
        if(eventReader == null){
            System.out.println("L'eventReader est null");
            return;
        }
        try{
        XMLManager.parse(eventReader,output_PATH);
        }catch(XMLStreamException e){
            System.out.println("Erreur lors du parsing du fichier XML"); 
            return;
        }
    }




}
