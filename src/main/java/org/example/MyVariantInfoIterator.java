package org.example;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import file.LineBuilder;
import org.json.JSONObject;
import tables.AllelleFrequency;
import tables.ClinicalSignificance;
import tables.Gene;
import tables.Variant;
import thread.CustomRecursiveAction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyVariantInfoIterator {
    public static void iterateHGVS(List<String> chrWithHGVS) throws InterruptedException, JsonProcessingException {
        System.out.println("myvarinfo");
        System.out.println(chrWithHGVS.size());

        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction(chrWithHGVS);
    }

    public static void getMyVariantInfo(List<String> subSetHgvs) throws FileNotFoundException {
        Queue<String> fullJSONResponse = new ConcurrentLinkedQueue<>();
        subSetHgvs.forEach(hgvs -> {
            try {
                URL url = new URL("https://myvariant.info/v1/variant/" + hgvs +
                        "?fields=_id,clinvar.rcv.conditions.identifiers.medgen,clinvar.rcv.conditions.name," +
                        "cadd.consequence,cadd.consdetail,cadd.gene.gene_id,cadd.gene.genename,cadd.exon," +
                        "cadd.intron,clinvar.hg19.end,clinvar.hg19.start,clinvar.hg38.end,clinvar.hg38.start," +
                        "clinvar.alt,clinvar.ref,clinvar.chrom,clinvar.rcv.accession,clinvar.rcv.clinical_significance," +
                        "clinvar.rcv.last_evaluated,clinvar.rcv.review_status,dbsnp.rsid,dbsnp.alt,dbsnp.ref,dbsnp.chrom," +
                        "gnomad_genome.af.af,gnomad_genome.af.af_nfe_bgr,gnomad_genome.af.af_nfe_male,gnomad_genome.af.af_nfe_female\n");
//                        System.out.println(url);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                generateObjects(content.toString().replaceAll("\"", "\\\""));

                fullJSONResponse.add(content.toString());
                in.close();

                con.disconnect();
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        });

        System.out.println(fullJSONResponse);
    }

    public static void generateObjects(String json) throws JsonProcessingException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        JsonObject response = gson.fromJson(json, JsonObject.class);


        String hgvs = response.has("_id") ? response.getAsJsonPrimitive("_id").getAsString() : "";
        String chromosome = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("chrom") ? response.getAsJsonPrimitive("_id").getAsString() : "";
        int start = response.has("clinvar") && response.getAsJsonObject("clinvar").has("hg19") && response.has("start") ? Integer.parseInt(response.getAsJsonObject("clinvar").getAsJsonObject("hg19").getAsJsonPrimitive("start").getAsString()) : -1;
        int end = response.has("clinvar") && response.getAsJsonObject("clinvar").has("hg19") && response.has("end") ? Integer.parseInt(response.getAsJsonObject("clinvar").getAsJsonObject("hg19").getAsJsonPrimitive("end").getAsString()) : -1;
        String rsid = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("rsid") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("rsid").getAsString() : "";
        String reference = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("ref") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("ref").getAsString() : "";
        String alternative = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("alt") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("alt").getAsString() : "";

//        String chromosome = response.getAsJsonObject("dbsnp").getAsJsonPrimitive("chrom").getAsString();
//        int start = Integer.parseInt(response.getAsJsonObject("clinvar").getAsJsonObject("hg19").getAsJsonPrimitive("start").getAsString());
//        int end = Integer.parseInt(response.getAsJsonObject("clinvar").getAsJsonObject("hg19").getAsJsonPrimitive("end").getAsString());
//        String rsid = response.getAsJsonObject("dbsnp").getAsJsonPrimitive("rsid").getAsString();
//        String reference = response.getAsJsonObject("dbsnp").getAsJsonPrimitive("ref").getAsString();
//        String alternative = response.getAsJsonObject("dbsnp").getAsJsonPrimitive("alt").getAsString();
////        Integer geneId;
//
//        //TODO if exon else intron
//        String region ;
//        String regionNum = response.getAsJsonObject("cadd").getAsJsonObject("exon").toString();
//
//        String consequence = response.getAsJsonObject("cadd").getAsJsonPrimitive("consequence").getAsString();
//        String consequenceDetails = response.getAsJsonObject("cadd").getAsJsonPrimitive("consdetail").getAsString();

//
        System.out.println(Arrays.toString(new String[]{hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative}));
//        Variant var = new Variant(hgvs, chromosome, start, end, dbsnp, reference, alternative, null, null, null, consequence, consequenceDetails);
//        System.out.println(var);

//        String result = entry.toString();
//        System.out.println(entry.toString());
//        System.out.println("in generate objects");
//        ObjectMapper mapper = new ObjectMapper();
//        Variant variant = mapper.readValue(json, Variant.class);
//        System.out.println(json);
//
//        System.out.println("variant" + variant);
//
//        Gene gene = new ObjectMapper().readValue(json, Gene.class);
////        AllelleFrequency alleleFrequency= new ObjectMapper().readValue(json, AllelleFrequency.class);
//        ClinicalSignificance clinicalSignificance = new ObjectMapper().readValue(json, ClinicalSignificance.class);
//
//        System.out.println(variant);
//        System.out.println(gene);
////        System.out.println(alleleFrequency);
//        System.out.println(clinicalSignificance);
//
//        System.out.println(Arrays.toString(LineBuilder.getVariantLine(variant)));
//        System.out.println(Arrays.toString(LineBuilder.getGeneLine(gene)));
////        System.out.println(Arrays.toString(LineBuilder.getAlleleFrequencyLine(alleleFrequency)));
//        System.out.println(Arrays.toString(LineBuilder.getClinicalSignificanceLine(clinicalSignificance)));

//        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\variant.csv",
//                new String[]{"Id", "HGVS", "Chromosome", "Start", "End", "DBSNP", "Reference", "Alternative", "GeneId", "Region", "RegionNum", "Consequence", "ConsequenceDetails"},
//                LineBuilder.getVariantLine(variant));
//
//        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\gene.csv",
//                new String[]{"Id", "GeneId", "Name", "Pseudo"},
//                LineBuilder.getGeneLine(gene));
//
//        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\alleleFrequency.csv",
//                new String[]{"Id", "VariantId", "GenderId", "PopulationId", "Frequency", "Updated"},
//                LineBuilder.getAlleleFrequencyLine(alleleFrequency));
//
//        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\clinicalSignificance.csv",
//                new String[]{"Id", "Accession", "VariantId", "PathologyId", "SignificanceId", "Evaluated", "ReviewStatus", "Updated"},
//                LineBuilder.getClinicalSignificanceLine(clinicalSignificance));
    }
}
