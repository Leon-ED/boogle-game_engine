package fr.uge.jdict;

import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

public enum OutputFormat {

    JSON, YAML, YML;

    public static String convert(String elem, OutputFormat format) {
        switch (format) {
            case JSON:
                return new JSONObject(elem).toString(4);
            case YAML:
                return new Yaml().dump(new JSONObject(elem).toMap());
            case YML:
                return new Yaml().dump(new JSONObject(elem).toMap());
            default:
                return elem;
        }
    }

}