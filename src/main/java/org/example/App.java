package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class App
{
    public static void main( String[] args ) throws InterruptedException {
        List<String> files = new ArrayList<>(Arrays.asList(
                "E:\\Quanterall\\myVariantInfoExtractor\\resources\\output_new_chrY_accession.bak"));
        HGVSBuilder.selectFiles(files);
    }
}
