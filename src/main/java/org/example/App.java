package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class App
{
    public static void main( String[] args ) throws InterruptedException {
        List<String> files = new ArrayList<>(Arrays.asList(
                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr22_accession"));
        HGVSBuilder.selectFiles(files);
    }
}
