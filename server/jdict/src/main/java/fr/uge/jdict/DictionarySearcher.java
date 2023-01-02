package fr.uge.jdict;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

import org.json.JSONObject;

public class DictionarySearcher {


    public static void main(String[] args) throws Exception {
        DictionarySearcher searcher = new DictionarySearcher();
        String index = "/Applications/XAMPP/xamppfiles/htdocs/boogle/server/files/definitions.index";
        String json = "/Applications/XAMPP/xamppfiles/htdocs/boogle/server/files/dumps/dump-wikipedia.json";  
        String word = "";
        OutputFormat format = OutputFormat.JSON;
        if(!args[0].equals("definitions")){
            System.out.println(args[0]);
            throw new IllegalArgumentException("Le premier argument doit être 'definitions'");
        }
        word = args[1];
        if(word.contains(":")){
            String[] split = word.split(":");
            word = split[1];
            format = OutputFormat.valueOf(split[0].toUpperCase());
        }


        searcher.search(json, index, word,format);
        
    }



    private String normalize(String word) {
        String string = Normalizer.normalize(word, Normalizer.Form.NFD);
        string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return string.toUpperCase();
    }

    public void search(String definitions_PATH, String index_PATH, String word,OutputFormat format) throws Exception{
        RandomAccessFile indexFile = new RandomAccessFile(index_PATH, "r");
        boolean searchSiblings = false;
        // On vérifie que le fichier d'index est valide
        byte[] magic = new byte[8];
        indexFile.read(magic);
        if (!new String(magic).equals("DICTINDX")) {
            indexFile.close();
            throw new IllegalStateException("Le fichier INDEX est invalide (aucun en-tête trouvé)");
        }
        RandomAccessFile definitionsFile = new RandomAccessFile(definitions_PATH, "r");
        // Si le mot est en majuscule, on le considère comme normalisé, on retournera alors
        // tous les mots qui correspondent à la normalisation du mot
        if(word.toUpperCase() == word){
            searchSiblings = true;
        }
       getDefinition(indexFile,definitionsFile,word,searchSiblings,format);



        indexFile.close();
        definitionsFile.close();
    }

    public void getDefinition(RandomAccessFile index, RandomAccessFile definitions, String word, boolean searchSiblings,OutputFormat format) {
        try {
            int startPos = 0;
            int endPos = (int) (index.length() / 8) - 1; 
    
            while (startPos <= endPos) {
                int midPos = startPos + (endPos - startPos) / 2;
                index.seek(midPos * 8); 
                int start = index.readInt();
                int end = index.readInt(); 
                if(start >= definitions.length() || end >= definitions.length()){
                    break;
                }
                definitions.seek(start); 
                // byte[] b = new byte[end - start + 1]; 
                // definitions.read(b);
                String definition = new String(definitions.readLine().getBytes(), StandardCharsets.UTF_8);
                // System.out.println("definition : " + definition);
                JSONObject json = new JSONObject(definition);
                String title = json.getString("title"); 
                
                // Si on veut chercher les mêmes mots normalisés
                if(searchSiblings){
                     word = normalize(word);
                     title = normalize(title);
                }


                if (title.equals(word)) {
                    System.out.println("Word found : ");
                    // System.out.println(json.toString());
                    System.out.println(OutputFormat.convert(json.toString(),format));
                    
                    // On a trouvé notre élément et on ne veut pas de frères
                    if(!searchSiblings){
                        return;
                    }
                }
    
                if (title.compareTo(word) < 0) {
                    startPos = midPos + 1;
                } else {
                    endPos = midPos - 1;
                }
            }
    
            System.out.println("Aucune définition trouvée pour le mot " + word + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}