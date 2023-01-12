package org.example;

import org.example.parser.CsvParser;
import org.example.parser.CsvWriter;
import org.example.parser.JsonParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CsvParser csvParser = new CsvParser();
        JsonParser jsonParser = new JsonParser();
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.writeResult(csvParser.csvToMap(),jsonParser.jsonToObject("{ \"col1\":\"a\", \"col2\":\"b\", \"col4\":\"d\", \"col5\":\"e\", \"col6\":\"z\", \"col7\":\"3\"}"));

    }
}
