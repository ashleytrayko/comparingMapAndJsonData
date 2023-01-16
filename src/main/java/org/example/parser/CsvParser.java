package org.example.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CsvParser {
    public Map<String, String> csvToMap(String csvFile, String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line =  null;
        HashMap<String,String> map = new HashMap<String, String>();
        while((line=br.readLine())!=null){
            if(line.equals(args[0])){
                break;
            }
            String str[] = line.split("\t");
            map.put(str[0].substring(str[0].indexOf(".")+1,str[0].length()), str[1]);
        }
        return map;
    }
}