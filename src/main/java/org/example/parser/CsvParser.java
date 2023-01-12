package org.example.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CsvParser {
    public Map<String, String> csvToMap() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/User/Desktop/compare.csv"));
        String line =  null;
        HashMap<String,String> map = new HashMap<String, String>();
        while((line=br.readLine())!=null){
        String str[] = line.split("\t");
        map.put(str[0].substring(str[0].indexOf(".")+1,str[0].length()), str[1]);
        }
        return map;
    }
}