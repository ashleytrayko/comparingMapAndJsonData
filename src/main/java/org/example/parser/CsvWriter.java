package org.example.parser;

import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CsvWriter {
    public void writeResult(Map<String, String> mapData, JSONObject jsonData){
        File csv = new File("C:/Users/User/Desktop/result.csv");
        List<String> checkList = new ArrayList<>();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true))){
            Iterator<String> csvKey = mapData.keySet().iterator();
            Iterator<String> jsonKey = jsonData.keySet().iterator();
            String header = "column\tinput1\tinput2\tcompare";
            bw.write(header);
            bw.newLine();
            // CSV데이터가 더 길거나 같은 경우
            if(mapData.size() >= jsonData.size()){
                for(Map.Entry<String, String> entry : mapData.entrySet()){
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    String line = "";
                    if(jsonData.containsKey(key)){
                        if(jsonData.get(key).toString().equalsIgnoreCase(value)){
                            line = key + "\t" + value + "\t" + jsonData.get(key) + "\t" + "Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();
                        }else{
                            line = key + "\t" + value + "\t" + jsonData.get(key) + "\t" + "Not Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();
                        }
                    }else{
                        line = key + "\t" + value + "\t" + "-" + "\t" + "json Not Found";
                        bw.write(line);
                        bw.newLine();
                    }
                }
                while (jsonKey.hasNext()){
                    String key = jsonKey.next();
                    String value = jsonData.get(key).toString();
                    String line = "";
                    if(checkList.contains(key)){
                        continue;
                    }else{
                        line = key + "\t" + "-" + "\t" + value + "\t" + "csv Not Found";
                        bw.write(line);
                        bw.newLine();
                    }
                }
                // JSON 데이터가 더 길경우
            }else{
                while (jsonKey.hasNext()){
                    String key = jsonKey.next();
                    String value = jsonData.get(key).toString();
                    String line = "";
                    if(mapData.containsKey(key)){
                        if(mapData.get(key).equalsIgnoreCase(value)){
                            line = key + "\t" + value + "\t" + mapData.get(key) + "\t" + "Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();
                        }else{
                            line = key + "\t" + value + "\t" + mapData.get(key) + "\t" + "Not Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();
                        }
                    }else{
                        line = key + "\t" + "-" + "\t" + value + "\t" + "csv Not Found";
                        bw.write(line);
                        bw.newLine();
                    }
                }
                for(Map.Entry<String, String> entry : mapData.entrySet()){
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    String line = "";
                    if(checkList.contains(key)){
                        continue;
                    }else{
                        line = key + "\t" + value + "\t" + "-" + "\t" + "json Not Found";
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}