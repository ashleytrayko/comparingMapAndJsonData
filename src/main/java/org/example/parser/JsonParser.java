package org.example.parser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JsonParser {
    public JSONObject jsonToObject(String jsonFile, String[] args){
        String jsonHalf = "";
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        String line = "";
        String jsonWhole = "";
        try(BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            while((line=br.readLine())!=null){
                jsonWhole += line;
            }
            jsonHalf = jsonWhole.substring(jsonWhole.indexOf(args[0])+args[0].length());
            jsonObject = (JSONObject) jsonParser.parse(jsonHalf);
            if(jsonObject.containsKey("reg_date")){
                String value = jsonObject.get("reg_date").toString();
                jsonObject.put("regdate", jsonObject.get("reg_date"));
                jsonObject.remove("reg_date");
            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }
}
