package org.example.parser;

import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvWriter {
    public void writeResult(Map<String, String> csvData, JSONObject jsonData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss");

        // 파일 이름 형식 rowkey+현재시간
        String fileName = csvData.get("rowkey")+sdf.format(new Date(System.currentTimeMillis()))+".csv";

        // 저장위치
        File csv = new File("C:/Users/User/Desktop/"+fileName);

        // 중복데이터를 거르기위한 체크리스트 생성
        List<String> checkList = new ArrayList<>();

        // try with resource
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true))){
            Iterator<String> csvKey = csvData.keySet().iterator();
            Iterator<String> jsonKey = jsonData.keySet().iterator();

            // 헤더 입력해주기
            String header = "column\tinput1\tinput2\tcompare";
            bw.write(header);
            bw.newLine();

            // CSV데이터가 더 길거나 같은 경우
            if(csvData.size() >= jsonData.size()){
                for(Map.Entry<String, String> entry : csvData.entrySet()){
                    String key = entry.getKey().toUpperCase();
                    String value = entry.getValue().toUpperCase();
                    String line = "";

                    // 무시할 키 설정
                    if(key.equalsIgnoreCase("rowkey") || key.equalsIgnoreCase("extract_date") || key.equalsIgnoreCase("extract_date_time")
                    || key.equalsIgnoreCase("kafka_topic") || key.equalsIgnoreCase("kafka_partition") || key.equalsIgnoreCase("kafka_offset")){
                        continue;
                    }

                    // 같은 키가 있는 경우
                    if(jsonData.containsKey(key)){

                        // 같은 키이고 값도 같은 경우
                        if(jsonData.get(key).toString().equalsIgnoreCase(value)){
                            line = key + "\t" + value + "\t" + jsonData.get(key) + "\t" + "Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();

                        // 같은 키이나 다른 값인 경우
                        }else{
                            line = key + "\t" + value + "\t" + jsonData.get(key) + "\t" + "Not Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();
                        }

                        // Json파일에서 찾지 못한 경우
                    }else{
                        line = key + "\t" + value + "\t" + "-" + "\t" + "json Not Found";
                        bw.write(line);
                        bw.newLine();
                    }
                }

                // CSV파일에는 없고 JSON 파일에만 있는 키와 값을 추가
                while (jsonKey.hasNext()){
                    String key = jsonKey.next();
                    String value = jsonData.get(key).toString();
                    String line = "";

                    // 이미 입력된 데이터는 넘기기
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

                    // CSV에 해당 키가 있는지 확인
                    if(csvData.containsKey(key)){

                        // 해당 키가 있고 값도 같은 경우
                        if(csvData.get(key).equalsIgnoreCase(value)){
                            line = key + "\t" + value + "\t" + csvData.get(key) + "\t" + "Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();

                            // 해당 키가 있으나 값은 다른 경우
                        }else{
                            line = key + "\t" + value + "\t" + csvData.get(key) + "\t" + "Not Equal";
                            checkList.add(key);
                            bw.write(line);
                            bw.newLine();
                        }

                        // CSV파일에서 찾지 못한 경우
                    }else{
                        line = key + "\t" + "-" + "\t" + value + "\t" + "csv Not Found";
                        bw.write(line);
                        bw.newLine();
                    }
                }

                // CSV 파일에만 있는 값을 출력
                for(Map.Entry<String, String> entry : csvData.entrySet()){
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    String line = "";

                    // 이미 기입된 데이터 스킵
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