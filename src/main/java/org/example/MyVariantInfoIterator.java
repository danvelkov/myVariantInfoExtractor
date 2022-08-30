package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;
import thread.CustomRecursiveAction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyVariantInfoIterator {

    private static int geneId = 1;
    private static int variantId = 1;
    private static int alleleFrequencyId = 1;
    private static int clinicalSignificanceId = 1;
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
                        "cadd.intron,dbsnp.hg19.end,dbsnp.hg19.start,clinvar.hg38.end,clinvar.hg38.start," +
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

//        System.out.println(fullJSONResponse);
    }

    public static void generateObjects(String json) throws JsonProcessingException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        JsonObject response = gson.fromJson(json, JsonObject.class);

        getVariant(response);

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

    private static void getVariant(JsonObject response){
        //VARIANT
        String hgvs = response.has("_id") ? response.getAsJsonPrimitive("_id").getAsString() : "";
        String chromosome = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("chrom") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("chrom").getAsString() : "";
        int start = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("hg19") && response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").has("start") ? Integer.parseInt(response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").getAsJsonPrimitive("start").getAsString()) : -1;
        int end = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("hg19") && response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").has("end") ? Integer.parseInt(response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").getAsJsonPrimitive("end").getAsString()) : -1;
        String rsid = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("rsid") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("rsid").getAsString() : "";
        String reference = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("ref") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("ref").getAsString() : "";
        String alternative = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("alt") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("alt").getAsString() : "";

        //TODO what if there aren't any genes
        Set<String> genesId = getGenes(response);
        if(genesId.size() > 0){
            genesId.forEach(gene -> {
                String region = "";
                String regionNum = "";
                if (response.has("cadd") && response.getAsJsonObject("cadd").has("exon")) {
                    regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("exon") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("exon").getAsString() : "";
                    region = "exon";
                } else if (response.has("cadd") && response.getAsJsonObject("cadd").has("intron")){
                    regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("intron") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("intron").getAsString() : "";
                    region = "intron";
                }
                List<String> consStrings = new ArrayList<>();
                if(response.has("cadd") && response.getAsJsonObject("cadd").has("consequence"))
                    if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                        JsonArray consequenceArray = response.getAsJsonObject("cadd").getAsJsonArray("consequence");
                        for (JsonElement jsonElement : consequenceArray) {
                            consStrings.add(jsonElement.toString());
                        }
                    } else
                        consStrings.add(response.getAsJsonObject("cadd").getAsJsonPrimitive("consequence").getAsString());

                List<String> consDetailsStrings = new ArrayList<>();
                if(response.has("cadd") && response.getAsJsonObject("cadd").has("consdetail"))
                    if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                        JsonArray consequenceDetailsArray = response.getAsJsonObject("cadd").getAsJsonArray("consdetail");
                        for (JsonElement jsonElement : consequenceDetailsArray) {
                            consDetailsStrings.add(jsonElement.toString());
                        }
                    } else
                        consDetailsStrings.add(response.getAsJsonObject("cadd").getAsJsonPrimitive("consdetail").getAsString());
//                System.out.println(gene);
                //TODO this section must insert in csv
//                System.out.println(Arrays.toString(new String[]{String.valueOf(variantId), hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative, gene, region, regionNum, String.valueOf(consStrings), String.valueOf(consDetailsStrings)}));
                getAlleleFrequencies(response, variantId);
                getClinicalSignificance(response, variantId);
                variantId++;
            });}
        else {
            String region = "";
            String regionNum = "";
            if (response.has("cadd") && response.getAsJsonObject("cadd").has("exon")) {
                regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("exon") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("exon").getAsString() : "";
                region = "exon";
            } else if (response.has("cadd") && response.getAsJsonObject("cadd").has("intron")){
                regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("intron") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("intron").getAsString() : "";
                region = "intron";
            }
            List<String> consStrings = new ArrayList<>();
            if(response.has("cadd") && response.getAsJsonObject("cadd").has("consequence"))
                if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                    JsonArray consequenceArray = response.getAsJsonObject("cadd").getAsJsonArray("consequence");
                    for (JsonElement jsonElement : consequenceArray) {
                        consStrings.add(jsonElement.toString());
                    }
                } else
                    consStrings.add(response.getAsJsonObject("cadd").getAsJsonPrimitive("consequence").getAsString());

            List<String> consDetailsStrings = new ArrayList<>();
            if(response.has("cadd") && response.getAsJsonObject("cadd").has("consdetail"))
                if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                    JsonArray consequenceDetailsArray = response.getAsJsonObject("cadd").getAsJsonArray("consdetail");
                    for (JsonElement jsonElement : consequenceDetailsArray) {
                        consDetailsStrings.add(jsonElement.toString());
                    }
                } else
                    consDetailsStrings.add(response.getAsJsonObject("cadd").getAsJsonPrimitive("consdetail").getAsString());

            //TODO this section must insert in csv
            getAlleleFrequencies(response, variantId);
            getClinicalSignificance(response, variantId);
//            System.out.println(Arrays.toString(new String[]{String.valueOf(variantId), hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative, null, region, regionNum, String.valueOf(consStrings), String.valueOf(consDetailsStrings)}));
            variantId++;
        }
    }

    private static Set<String> getGenes(JsonObject response){
        Gson gson = new GsonBuilder().serializeNulls().create();
        //GENE
        Map<String, Integer> geneIdMap = new HashMap<>();
        Map<String, String> geneMap = new HashMap<>();
        if(response.has("cadd") && response.getAsJsonObject("cadd").has("gene"))
            if(response.getAsJsonObject("cadd").get("gene").isJsonArray()) {
                JsonArray genesArray = response.getAsJsonObject("cadd").getAsJsonArray("gene");
                for (JsonElement jsonElement : genesArray) {
                    JsonObject geneInsideArray = gson.fromJson(jsonElement, JsonObject.class);
                    if(geneInsideArray.has("gene_id") && geneInsideArray.has("genename")) {
                        geneMap.put(geneInsideArray.getAsJsonPrimitive("gene_id").getAsString(),
                                geneInsideArray.getAsJsonPrimitive("genename").getAsString());

                        geneIdMap.put(geneInsideArray.getAsJsonPrimitive("gene_id").getAsString(), geneId);
                        geneId++;
                    }
                }
            } else
            if(response.getAsJsonObject("cadd").getAsJsonObject("gene").has("gene_id")
                    && response.getAsJsonObject("cadd").getAsJsonObject("gene").has("genename")) {
                geneMap.put(response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("gene_id").toString(),
                        response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("genename").toString());

                geneIdMap.put(response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("gene_id").toString(), geneId);
                geneId++;
            }
        boolean isPseudo = false;

        ////////
        //TODO this section must insert in csv

        for (Map.Entry<String, String> gene: geneMap.entrySet()) {
//            System.out.println(Arrays.toString(new String[]{String.valueOf(geneIdMap.get(gene.getKey())), gene.getKey(), gene.getValue(), String.valueOf(isPseudo)}));
        }

        /////
        return geneIdMap.keySet();
    }

    private static void getAlleleFrequencies(JsonObject response, int varId){
        //ALLELE FREQUENCY
        String af = response.has("gnomad_exome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af").getAsString() : "";
        String af_nfe_bgr = response.has("gnomad_genome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af_nfe_bgr") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af_nfe_bgr").getAsString() : "";
        String af_nfe_male = response.has("gnomad_genome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af_nfe_male") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af_nfe_male").getAsString() : "";
        String af_nfe_female = response.has("gnomad_genome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af_nfe_female") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af_nfe_female").getAsString() : "";

        List<String[]> alleleFrequencyArray = new ArrayList<>();

        int genderId = 0;
        int populationId = 0;
        double frequency = 0.0;
        java.sql.Date updated = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        if(!af.isEmpty()) {
            populationId = 18;
            genderId = 3;
            frequency = Double.parseDouble(af);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(populationId), Integer.toString(genderId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
        }
        if(!af_nfe_bgr.isEmpty()) {
            populationId = 12;
            genderId = 3;
            frequency = Double.parseDouble(af);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(populationId), Integer.toString(genderId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
        }
        if(!af_nfe_male.isEmpty()) {
            populationId = 11;
            genderId = 1;
            frequency = Double.parseDouble(af);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(populationId), Integer.toString(genderId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
        }
        if(!af_nfe_female.isEmpty()) {
            populationId = 11;
            genderId = 2;
            frequency = Double.parseDouble(af);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(populationId), Integer.toString(genderId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
        }

        //TODO this section must insert in csv
        alleleFrequencyArray.forEach(alleleFreq -> {
//            System.out.println(Arrays.toString(alleleFreq));
        });
    }

    private static void getClinicalSignificance(JsonObject response, int varId){
        //CLINICAL SIGNIFICANCE
        Gson gson = new GsonBuilder().serializeNulls().create();

        if(response.has("clinvar") &&
                response.getAsJsonObject("clinvar").has("rcv")) {
            if(response.getAsJsonObject("clinvar").get("rcv").isJsonArray()) {
                JsonArray rcvArray = response.getAsJsonObject("clinvar").getAsJsonArray("rcv");
                rcvArray.forEach(rcv -> {
                    JsonObject rcvInsideArray = gson.fromJson(rcv, JsonObject.class);
                    if(rcvInsideArray.has("conditions") && rcvInsideArray.getAsJsonObject("conditions").has("identifiers")){
                        JsonObject identifiers = gson.fromJson(rcvInsideArray.getAsJsonObject("conditions").getAsJsonObject("identifiers"), JsonObject.class);
                        if(identifiers.has("medgen")) {
                            //TODO find the cui from csv file
                            //TODO create csv line or sout
                        } else {
                            //TODO create csv line or sout without cui
                        }
                    }
                });
            } else {
                if(response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("conditions") &&
                        response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonObject("conditions").has("identifiers")) {
                    //TODO find the cui from csv file
                    //TODO create csv line or sout
                } else {
                    //TODO create csv line or sout without cui
                }
            }

        } else {
            if(response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("conditions") &&
                    response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonObject("conditions").has("identifiers")) {
                if(response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonObject("conditions").getAsJsonObject("identifiers").has("medgen")) {
                    //TODO find the cui from csv file
                    //TODO create csv line or sout
                }
            } else {
                //TODO create csv line or sout without cui
            }
        }
//
//        String accession = response.has("clinvar") &&
//                response.getAsJsonObject("clinvar").has("rcv") &&
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("accession") ?
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("accession").getAsString() : "";
//
//        Integer pathologyId = -1;
//
//        String significanceId = response.has("clinvar") &&
//                response.getAsJsonObject("clinvar").has("rcv") &&
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("clinical_significance") ?
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("clinical_significance").getAsString() : "";
//
//
//        String evaluated = response.has("clinvar") &&
//                response.getAsJsonObject("clinvar").has("rcv") &&
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("last_evaluated") ?
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("last_evaluated").getAsString() : "";
//
//        String updated = response.has("clinvar") &&
//                response.getAsJsonObject("clinvar").has("rcv") &&
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("review_status") ?
//                response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("review_status").getAsString() : "";
//
//        System.out.println(Arrays.toString(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), String.valueOf(pathologyId), significanceId, evaluated, updated}));
//        clinicalSignificanceId++;
    }

}
