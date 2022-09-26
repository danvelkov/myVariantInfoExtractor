package tables;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import file.FileWriterForCsv;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static tables.AlleleFrequency.getAlleleFrequencies;
import static tables.ClinicalSignificance.getClinicalSignificance;
import static tables.Gene.getGenes;

public class Variant {
    private static final Object lock = new Object();
    static int variantId = 0;
//    static int geneId = 0;

    public synchronized static boolean incrementVariantId(){
        ++variantId;
        return true;
    }

//    public synchronized static boolean incrementGeneId(){
//        ++geneId;
//        return true;
//    }

    public static int getVariantId(){
        return variantId;
    }
    public static void getVariant(JsonObject response, Map<String, String> pathologies){
        String hgvs = response.has("_id") ? response.getAsJsonPrimitive("_id").getAsString() : "";

        //VARIANT
        String chromosome = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("chrom") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("chrom").getAsString() : "";
        int start = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("hg19") && response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").has("start") ? Integer.parseInt(response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").getAsJsonPrimitive("start").getAsString()) : -1;
        int end = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("hg19") && response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").has("end") ? Integer.parseInt(response.getAsJsonObject("dbsnp").getAsJsonObject("hg19").getAsJsonPrimitive("end").getAsString()) : -1;
        String rsid = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("rsid") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("rsid").getAsString() : "";
        String reference = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("ref") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("ref").getAsString() : "";
        String alternative = response.has("dbsnp") && response.getAsJsonObject("dbsnp").has("alt") ? response.getAsJsonObject("dbsnp").getAsJsonPrimitive("alt").getAsString() : "";

        Map<String, Integer> genesId = getGenes(response);
        AtomicInteger geneMapElemIndex = new AtomicInteger();
        if(genesId.size() > 0){
            genesId.keySet().forEach(gene -> {
                String region = "";
                String regionNum = "";
                String consequence = "";
                String consequenceDetails = "";
                if (response.has("cadd") && response.getAsJsonObject("cadd").has("exon")) {
                    if(response.getAsJsonObject("cadd").get("exon").isJsonArray()) {
                        JsonArray regionNumArray = response.getAsJsonObject("cadd").getAsJsonArray("exon");
                        StringBuilder finalRegionNum = new StringBuilder(regionNum);
                        regionNumArray.forEach(regionNumElem -> {
                            if(finalRegionNum.length() == 0)
                                finalRegionNum.append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                            else
                                finalRegionNum.append(",").append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                        });
                        regionNum = "[" + finalRegionNum + "]";
                    }
                    else
                        regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("exon") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("exon").getAsString() : "";

                    region = "exon";
                } else if (response.has("cadd") && response.getAsJsonObject("cadd").has("intron")){
                    if(response.getAsJsonObject("cadd").get("intron").isJsonArray()) {
                        JsonArray regionNumArray = response.getAsJsonObject("cadd").getAsJsonArray("intron");
                        StringBuilder finalRegionNum = new StringBuilder(regionNum);
                        regionNumArray.forEach(regionNumElem -> {
                            if(finalRegionNum.length() == 0)
                                finalRegionNum.append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                            else
                                finalRegionNum.append(",").append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                        });
                        regionNum = "[" + finalRegionNum.toString() + "]";
                    }
                    else
                        regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("intron") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("intron").getAsString() : "";

                    region = "intron";
                }
                if(response.has("cadd") && response.getAsJsonObject("cadd").has("consequence"))
                    if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                        JsonArray consequenceArray = response.getAsJsonObject("cadd").getAsJsonArray("consequence");
                        StringBuilder finalConsequenceArray = new StringBuilder(consequence);
                        for (JsonElement jsonElement : consequenceArray) {
                            if(finalConsequenceArray.length() == 0)
                                finalConsequenceArray.append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                            else
                                finalConsequenceArray.append(",").append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                        }

                        consequence = finalConsequenceArray.toString();
                    } else
                        consequence = response.getAsJsonObject("cadd").getAsJsonPrimitive("consequence").getAsString();

                if(response.has("cadd") && response.getAsJsonObject("cadd").has("consdetail"))
                    if(response.getAsJsonObject("cadd").get("consdetail").isJsonArray()) {
                        JsonArray consequenceDetailsArray = response.getAsJsonObject("cadd").getAsJsonArray("consdetail");

                        StringBuilder finalConsequenceDetailsArray = new StringBuilder(consequenceDetails);
                        for (JsonElement jsonElement : consequenceDetailsArray) {
                            if(finalConsequenceDetailsArray.length() == 0)
                                finalConsequenceDetailsArray.append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                            else
                                finalConsequenceDetailsArray.append(",").append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                        }

                        consequenceDetails = finalConsequenceDetailsArray.toString();
                    } else
                        consequenceDetails = response.getAsJsonObject("cadd").getAsJsonPrimitive("consdetail").getAsString();

                synchronized (lock) {
                    try {
                        while(!incrementVariantId())
                            lock.wait();

                        write(new String[]{String.valueOf(getVariantId()), hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative, String.valueOf(genesId.get(gene)), region, '"' + regionNum + '"', '"' + consequence + '"', '"' + consequenceDetails + '"'});

                        getAlleleFrequencies(response, getVariantId());
                        getClinicalSignificance(response, getVariantId(), pathologies);

                        lock.notifyAll();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                geneMapElemIndex.getAndIncrement();
            });}
        else {
            String region = "";
            String regionNum = "";
            String consequence = "";
            String consequenceDetails = "";
            if (response.has("cadd") && response.getAsJsonObject("cadd").has("exon")) {
                if(response.getAsJsonObject("cadd").get("exon").isJsonArray()) {
                    JsonArray regionNumArray = response.getAsJsonObject("cadd").getAsJsonArray("exon");
                    StringBuilder finalRegionNum = new StringBuilder(regionNum);
                    regionNumArray.forEach(regionNumElem -> {
                        if(finalRegionNum.length() == 0)
                            finalRegionNum.append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                        else
                            finalRegionNum.append(",").append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                    });
                    regionNum = "[" + finalRegionNum.toString() + "]";
                }

                else
                    regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("exon") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("exon").getAsString() : "";

                region = "exon";
            } else if (response.has("cadd") && response.getAsJsonObject("cadd").has("intron")){
                if(response.getAsJsonObject("cadd").get("intron").isJsonArray()) {
                    JsonArray regionNumArray = response.getAsJsonObject("cadd").getAsJsonArray("intron");
                    StringBuilder finalRegionNum = new StringBuilder(regionNum);
                    regionNumArray.forEach(regionNumElem -> {
                        if(finalRegionNum.length() == 0)
                            finalRegionNum.append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                        else
                            finalRegionNum.append(",").append(regionNumElem.toString().substring(1, regionNumElem.toString().length() - 1));
                    });
                    regionNum = "[" + finalRegionNum.toString() + "]";
                }
                else
                    regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("intron") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("intron").getAsString() : "";

                region = "intron";
            }
            if(response.has("cadd") && response.getAsJsonObject("cadd").has("consequence"))
                if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                    JsonArray consequenceArray = response.getAsJsonObject("cadd").getAsJsonArray("consequence");
                    StringBuilder finalConsequenceArray = new StringBuilder(consequence);
                    for (JsonElement jsonElement : consequenceArray) {
                        if(finalConsequenceArray.length() == 0)
                            finalConsequenceArray.append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                        else
                            finalConsequenceArray.append(",").append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                    }

                    consequence = finalConsequenceArray.toString();
                } else
                    consequence = response.getAsJsonObject("cadd").getAsJsonPrimitive("consequence").getAsString();

            if(response.has("cadd") && response.getAsJsonObject("cadd").has("consdetail"))
                if(response.getAsJsonObject("cadd").get("consequence").isJsonArray()) {
                    JsonArray consequenceDetailsArray = response.getAsJsonObject("cadd").getAsJsonArray("consdetail");
                    StringBuilder finalConsequenceDetailsArray = new StringBuilder(consequenceDetails);
                    for (JsonElement jsonElement : consequenceDetailsArray) {
                        if(finalConsequenceDetailsArray.length() == 0)
                            finalConsequenceDetailsArray.append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                        else
                            finalConsequenceDetailsArray.append(",").append(jsonElement.toString().substring(1, jsonElement.toString().length() - 1));
                    }

                    consequenceDetails = finalConsequenceDetailsArray.toString();
                } else
                    consequenceDetails = response.getAsJsonObject("cadd").getAsJsonPrimitive("consdetail").getAsString();

            synchronized (lock) {
                try {
                    while(!incrementVariantId())
                        lock.wait();

                    write(new String[]{String.valueOf(getVariantId()), hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative, "0", region, '"' + regionNum + '"', '"' + consequence + '"', '"' + consequenceDetails + '"'});

                    getAlleleFrequencies(response, getVariantId());
                    getClinicalSignificance(response, getVariantId(), pathologies);

                    lock.notifyAll();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private synchronized static boolean write(String[] data){
        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\variant.csv",
                new String[]{"Id", "HGVS", "Chromosome", "Start", "End", "DBSNP", "Reference", "Alternative", "GeneId", "Region", "RegionNum", "Consequence", "ConsequenceDetails"},
                data);

        return true;
    }
}
