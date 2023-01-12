package org.example.parser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
    public JSONObject jsonToObject(String jsonInput){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonInput);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }
}
