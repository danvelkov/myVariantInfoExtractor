package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HGVSBuilder {

    public static void selectFiles(List<String> files) throws InterruptedException{
        List<List<String>> allHGVS = new ArrayList<>();
        files.forEach(file -> {
            try {
                allHGVS.add(importFile(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

            allHGVS.forEach(hgvs -> {
                try {
                    MyVariantInfoIterator.iterateHGVS(hgvs);
                } catch (InterruptedException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

    }
    public static List<String> importFile(String fileName) throws IOException {

        List<String> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(":");
                if(elements[3].contains(",")){
                    String[] altSplit = elements[3].split(",");
                    System.out.println(elements[3]);
                    System.out.println(Arrays.toString(altSplit));
                    for (String alt : altSplit) {
                        String HGVS = "chr" + elements[0] + ":g." + elements[1] + elements[2] + "%3e" + alt;
                        result.add(HGVS);
                    }
                } else {
                    String HGVS = "chr" + elements[0] + ":g." + elements[1] + elements[2] + "%3e" + elements[3];
                    result.add(HGVS);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
