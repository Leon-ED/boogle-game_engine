package fr.uge.jdict;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.json.JSONObject;

public class IndexMaker {
    private final String index_PATH;
    private final String json_PATH;
    private final Map<String, Integer[]> map;
    private int positionOffset = 0;

    private static IndexMaker instance;

    private IndexMaker(String index_PATH, String json_PATH) {
        this.index_PATH = Objects.requireNonNull(index_PATH);
        this.json_PATH = Objects.requireNonNull(json_PATH);
        this.map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DictionaryNormalizer.compareNormalized(o1, o2);
            }
        });
    }

    public static IndexMaker getIndexMaker(String index_PATH, String json_PATH) {
        if (instance == null) {
            instance = new IndexMaker(index_PATH, json_PATH);
        }
        return instance;
    }

    public static IndexMaker getIndexMaker() {
        if (instance == null)
            throw new IllegalStateException("IndexMaker not initialized");
        return instance;
    }

    public void setOffset(int offset) {
        this.positionOffset = offset;
    }

    private record Entry(int start, int end, String line) {

    }

    public void addTitle(String title, int length) {
        Integer[] array = new Integer[] { positionOffset, positionOffset + length };
        if(map.containsKey(title)){
            title = title + "a";
        }
        map.put(title, array);
        positionOffset += length;

    }

    public void createIndex() throws IOException {
        try (FileInputStream reader = new FileInputStream(json_PATH)) {
            Entry entry = null;
            String line;
            while ((entry = readLine(reader)) != null) {
                line = entry.line();
                if (!line.contains("title")) {
                    continue;
                }
                JSONObject json = new JSONObject(line);
                String title = json.getString("title");
                // convert title to utf-8
                title = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                int startPOS = entry.start();
                int endPOS = entry.end();

                Integer[] array = new Integer[] { startPOS, endPOS };
                map.put(title, array);
            }
        }
        writeIndex(index_PATH, map);

    }

    public void writeIndex() throws IOException {
        writeIndex(index_PATH, map);
    }

    private void writeIndex(String out, Map<String, Integer[]> map) throws IOException {
        FileOutputStream writer = new FileOutputStream(out);
        writer.write("DICTINDX".getBytes());
        map.values().stream().forEach(liste -> {
            try {
                writer.write(ByteBuffer.allocate(4).putInt(liste[0]).array());
                writer.write(ByteBuffer.allocate(4).putInt(liste[1]).array());
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        writer.close();
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
        return new Entry(startPOS, endPOS, line.toString());
    }

}