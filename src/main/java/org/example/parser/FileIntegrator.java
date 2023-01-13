package org.example.parser;

import java.io.*;

public class FileIntegrator {
    public void integrateFile(){
        String csvFilePath = "C:/Users/User/Desktop/csvFolder";
        String jsonFilePath = "C:/Users/User/Desktop/jsonFolder";
        String resultFilePath = "C:/Users/User/Desktop/targetDirectory";

        File csvFile = new File(csvFilePath);
        File csvfiles[] = csvFile.listFiles();


        File jsonFile = new File(jsonFilePath);
        File jsonfiles[] = jsonFile.listFiles();

        // csv파일과 json 파일 읽어 경로 저장
        if(csvfiles.length != 0){
            for(File fileName : csvfiles){
                if(fileName.toString().endsWith(".csv")){
                    csvFilePath = fileName.toString();
                }
            }
        }

        if(jsonfiles.length != 0){
            for(File fileName : jsonfiles){
                if(fileName.toString().endsWith(".json")){
                    jsonFilePath = fileName.toString();
                }
            }
        }

        if(csvFilePath != "" || jsonFilePath !=""){

        }

        try (BufferedReader csvReader = new BufferedReader(new FileReader(csvFilePath)); BufferedReader jsonReader = new BufferedReader(new FileReader(jsonFilePath)); BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/User/Desktop/targetDirectory/result.txt"))){
            String line = "";
            while((line=csvReader.readLine()) != null){
                bw.write(line);
                bw.newLine();
            }
            bw.write("###");
            bw.newLine();
            while ((line=jsonReader.readLine()) != null){
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
