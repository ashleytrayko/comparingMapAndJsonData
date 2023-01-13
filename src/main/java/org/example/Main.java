package org.example;

import org.example.parser.CsvParser;
import org.example.parser.CsvWriter;
import org.example.parser.FileIntegrator;
import org.example.parser.JsonParser;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        CsvParser csvParser = new CsvParser();
        JsonParser jsonParser = new JsonParser();
        CsvWriter csvWriter = new CsvWriter();

        // 트리거 타겟 디렉토리
        String targetDirectory = "C:/Users/User/Desktop/targetDirectory";


        // 파일 생성 트리거
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(targetDirectory);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        // CSV, JSON 파일 합치기
        FileIntegrator fileIntegrator = new FileIntegrator();
        fileIntegrator.integrateFile();
        while(true){

            // 읽어들일 파일
            String targetFile = "C:/Users/User/Desktop/targetDirectory/result.txt";

            // 파일 생성 이벤트 발생시 작동하는 트리거
            WatchKey watchKey = watchService.take();
            List<WatchEvent<?>> list = watchKey.pollEvents();
            for(WatchEvent<?> event : list){
                WatchEvent.Kind<?> kind = event.kind();
                Path path1 = (Path) event.context();
                if(kind.equals(StandardWatchEventKinds.ENTRY_CREATE)){

                    // CSV 파일 작성 메서드
                    csvWriter.writeResult(csvParser.csvToMap(targetFile),jsonParser.jsonToObject(targetFile));
                }
            }
            if(!watchKey.reset()) break;
        }
        watchService.close();
    }
}
