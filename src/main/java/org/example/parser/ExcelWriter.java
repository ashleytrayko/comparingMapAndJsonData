package org.example.parser;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.*;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelWriter {

    public void dataWriter(Map<String, String> csvData, JSONObject jsonData){

        int rowNum = 0;
        int columnNum = 0;
        List<String> checkList = new ArrayList<>();

        // 파일 이름 형식 rowkey+현재시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss");
        String fileName = csvData.get("rowkey")+sdf.format(new Date(System.currentTimeMillis()))+".xlsx";
        File rootPath = new File(System.getProperty("user.dir"));
        File finalPath = new File(rootPath.getPath() + "/resultData");
        if(!finalPath.exists()){
            finalPath.mkdir();
        }


        // Workbook 생성
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Sheet 생성
        XSSFSheet sheet = workbook.createSheet("CsvAndJsonData");
        sheet.setColumnWidth(0,10000);
        sheet.setColumnWidth(1,10000);
        sheet.setColumnWidth(2,10000);
        sheet.setColumnWidth(3,10000);

        // 행 생성
        XSSFRow row = sheet.createRow(rowNum++);


        // 폰트 스타일 설정
        XSSFFont defaultFont = workbook.createFont();
        defaultFont.setBold(false);

        XSSFFont font = workbook.createFont();
        font.setBold(true);

        XSSFCellStyle defaultStyle = workbook.createCellStyle();
        defaultStyle.setBorderTop(BorderStyle.THIN);
        defaultStyle.setBorderBottom(BorderStyle.THIN);
        defaultStyle.setBorderLeft(BorderStyle.THIN);
        defaultStyle.setBorderRight(BorderStyle.THIN);

        XSSFCellStyle differStyle = workbook.createCellStyle();
        differStyle.setBorderTop(BorderStyle.THIN);
        differStyle.setBorderBottom(BorderStyle.THIN);
        differStyle.setBorderLeft(BorderStyle.THIN);
        differStyle.setBorderRight(BorderStyle.THIN);
        differStyle.setFont(font);

        // 헤더생성
        XSSFCell header = row.createCell(0);
        header.setCellStyle(differStyle);
        header.setCellValue("column");

        header = row.createCell(1);
        header.setCellStyle(differStyle);
        header.setCellValue("CSV Data");

        header = row.createCell(2);
        header.setCellStyle(differStyle);
        header.setCellValue("JSON Data");

        header = row.createCell(3);
        header.setCellStyle(differStyle);
        header.setCellValue("compare");



        Iterator<String> jsonKey = jsonData.keySet().iterator();

        for(Map.Entry<String, String> entry : csvData.entrySet()){

            row = sheet.createRow(rowNum);
            XSSFCell cell = null;
            String key = entry.getKey().toUpperCase();
            String value = entry.getValue().toUpperCase();

            // 무시할 키 설정
            if(key.equalsIgnoreCase("rowkey") || key.equalsIgnoreCase("extract_date") || key.equalsIgnoreCase("extract_date_time")
                    || key.equalsIgnoreCase("kafka_topic") || key.equalsIgnoreCase("kafka_partition") || key.equalsIgnoreCase("kafka_offset")){
                checkList.add(key);
                continue;
            }

            // 같은 키가 있는 경우
            if(jsonData.containsKey(key)){

                // 같은 키이고 값도 같은 경우
                if(jsonData.get(key).toString().equalsIgnoreCase(value)){
                    cell = row.createCell(0);
                    cell.setCellStyle(defaultStyle);
                    cell.setCellValue(key);

                    cell = row.createCell(1);
                    cell.setCellStyle(defaultStyle);
                    cell.setCellValue(value);

                    cell = row.createCell(2);
                    cell.setCellStyle(defaultStyle);
                    cell.setCellValue(jsonData.get(key).toString());

                    cell = row.createCell(3);
                    cell.setCellStyle(defaultStyle);
                    cell.setCellValue("Equal");

                    checkList.add(key);
                    rowNum++;
                    // 같은 키이나 다른 값인 경우
                }else{
                    cell = row.createCell(0);
                    cell.setCellStyle(differStyle);
                    cell.setCellValue(key);

                    cell = row.createCell(1);
                    cell.setCellStyle(differStyle);
                    cell.setCellValue(value);

                    cell = row.createCell(2);
                    cell.setCellStyle(differStyle);
                    cell.setCellValue(jsonData.get(key).toString());

                    cell = row.createCell(3);
                    cell.setCellStyle(differStyle);
                    cell.setCellValue("Not Equal");

                    checkList.add(key);
                    rowNum++;
                }

                // Json파일에서 찾지 못한 경우
            }else{
                cell = row.createCell(0);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue(key);

                cell = row.createCell(1);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue(value);

                cell = row.createCell(2);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue("-");

                cell = row.createCell(3);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue("json Not Found");

//                row.createCell(0).setCellValue(key);
//                row.createCell(1).setCellValue(value);
//                row.createCell(2).setCellValue("-");
//                row.createCell(3).setCellValue("json Not Found");
                rowNum++;
            }
        }

        // CSV파일에는 없고 JSON 파일에만 있는 키와 값을 추가
        while (jsonKey.hasNext()){
            row = sheet.createRow(rowNum);
            XSSFCell cell = null;
            String key = jsonKey.next();
            String value = jsonData.get(key).toString();

            // 이미 입력된 데이터는 넘기기
            if(checkList.contains(key)){
                continue;
            }else{
                cell = row.createCell(0);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue(key);

                cell = row.createCell(1);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue("-");

                cell = row.createCell(2);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue(value);

                cell = row.createCell(3);
                cell.setCellStyle(defaultStyle);
                cell.setCellValue("csv Not Found");

//                row.createCell(0).setCellValue(key);
//                row.createCell(1).setCellValue("-");
//                row.createCell(2).setCellValue(value);
//                row.createCell(3).setCellValue("csv Not Found");
                rowNum++;
            }
        }


        try(FileOutputStream fos = new FileOutputStream(new File(finalPath,fileName))) {
            workbook.write(fos);
            workbook.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
