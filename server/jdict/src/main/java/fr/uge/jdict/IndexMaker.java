package fr.uge.jdict;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

public record IndexMaker() {

    private record Entry(int start,int end, String line){
        

    }


    public static void main(String[] args) throws IOException {
        IndexMaker indexMaker = new IndexMaker();
        String output_PATH = "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\dump-test.json";
        indexMaker.createIndex(output_PATH, "E:\\Emplacements\\Bureau\\boogle\\real\\server\\files\\dumps\\index.txt");
    }




    public void createIndex(String in, String out) throws IOException {
        // HashMap<String, Integer[]> map = new HashMap<>();
        Map<String, Integer[]> map = new TreeMap<String, Integer[]>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DictionaryNormalized.compareNormalized(o1, o2);
            }
          });
        try (FileInputStream reader = new FileInputStream(in)) {
            Entry entry = null;
                String line;
                    while((entry = readLine(reader)) != null){
                        line = entry.line();
                        if(!line.contains("title")){
                            continue;
                        }
                        JSONObject json = new JSONObject(line);
                        String title = json.getString("title");
                        // convert title to utf-8
                        title = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        int startPOS = entry.start();
                        int endPOS = entry.end();

                        Integer[] array = new Integer[]{startPOS, endPOS};
                        map.put(title, array);
                        // System.out.println(title + " : "+ startPOS + " - " + endPOS);
                    }

                    // // On trie la map par ordre alphabétique
                    // map.entrySet()
                    // .stream()
                    // .sorted((e1,e2) -> normalize(e1.getKey()).compareTo(normalize(e2.getKey())))
                    // .forEach(e -> sortedMap.put(e.getKey(), e.getValue()));
                    // // .forEach(e -> System.out.println("Z : "+e.getKey() + " : "+ e.getValue()[0] + " - " + e.getValue()[1]));


            
        }
        writeIndex(out, map);

    }

    private void writeIndex(String out, Map<String, Integer[]> map) throws IOException {
        System.out.println("Writing index");
        try (FileOutputStream writer = new FileOutputStream(out)) {
            writer.write("DICTINDX".getBytes());
            for (String word : map.keySet()) {
                // Convertion en grand enboutiste
                writer.write((map.get(word)[0] >> 24) & 0xFF);
                writer.write((map.get(word)[0] >> 16) & 0xFF);
                writer.write((map.get(word)[0] >> 8) & 0xFF);
                writer.write(map.get(word)[0] & 0xFF);
                writer.write((map.get(word)[1] >> 24) & 0xFF);
                writer.write((map.get(word)[1] >> 16) & 0xFF);
                writer.write((map.get(word)[1] >> 8) & 0xFF);
                writer.write(map.get(word)[1] & 0xFF);
                System.out.println(word + " : "+ map.get(word)[0] + " - " + map.get(word)[1]);
            }
        }

    }
    

    // Méthode pour lire une ligne du fichier (en ignorant les retours à la ligne)
    private Entry readLine(FileInputStream file) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        int startPOS = (int) file.getChannel().position();
        int endPOS = 0;
        while ((c = file.read()) != -1) {
            if (c == '\n') {
                endPOS = (int) file.getChannel().position();
                break;
            }

            line.append((char) c);
        }
        if (c == -1 && line.toString().length() == 0) {
            return null;
        }
        return new Entry(startPOS,endPOS,line.toString());
    }

}