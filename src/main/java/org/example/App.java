package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        List<String> files = new ArrayList<>(Arrays.asList(
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr1_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr2_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr3_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr4_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr5_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr6_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr7_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr8_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr9_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr10_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr11_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr12_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr13_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr14_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr15_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr16_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr17_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr18_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr19_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr20_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr21_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chr22_accession",
//                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chrX_accession",
                "C:\\Users\\Dan\\Desktop\\chromosome_accessions\\output_new_chrY_accession"));
        HGVSBuilder.selectFiles(files);
    }
}
