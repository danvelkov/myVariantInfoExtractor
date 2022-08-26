package org.example;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonObject;
public class FileWriter {
    public void JSONtoArray(String jsonString){
//        JsonObject jsonObject = new JsonObject(jsonString);
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}
