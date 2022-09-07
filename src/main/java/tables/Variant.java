package tables;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import file.FileWriterForCsv;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static tables.AllelleFrequency.getAlleleFrequencies;
import static tables.ClinicalSignificance.getClinicalSignificance;
import static tables.Gene.getGenes;

public class Variant {

    private static final Object lock = new Object();
    static int variantId = 1;

    public static void incrementVariantId(){
        ++variantId;
    }

    public static  int getVariantId(){
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

        //TODO what if there aren't any genes
        Map<String, Integer> genesId = getGenes(response);
        AtomicInteger geneMapElemIndex = new AtomicInteger();
        if(genesId.size() > 0){
            genesId.keySet().forEach(gene -> {
                String region = "";
                String regionNum = "";
                if (response.has("cadd") && response.getAsJsonObject("cadd").has("exon")) {
                    if(response.getAsJsonObject("cadd").get("exon").isJsonArray())
                        regionNum = response.getAsJsonObject("cadd").getAsJsonArray("exon").toString();
                    else
                        regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("exon") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("exon").getAsString() : "";

                    region = "exon";
                } else if (response.has("cadd") && response.getAsJsonObject("cadd").has("intron")){
                    if(response.getAsJsonObject("cadd").get("intron").isJsonArray())
                        regionNum = response.getAsJsonObject("cadd").getAsJsonArray("intron").toString();
                    else
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
                    if(response.getAsJsonObject("cadd").get("consdetail").isJsonArray()) {
                        JsonArray consequenceDetailsArray = response.getAsJsonObject("cadd").getAsJsonArray("consdetail");
                        for (JsonElement jsonElement : consequenceDetailsArray) {
                            consDetailsStrings.add(jsonElement.toString());
                        }
                    } else
                        consDetailsStrings.add(response.getAsJsonObject("cadd").getAsJsonPrimitive("consdetail").getAsString());

//                    if(!insertedHgvs.contains(hgvs)) {
                synchronized (lock) {
                    try {
                        lock.wait();
                        write(new String[]{String.valueOf(getVariantId()), hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative, String.valueOf(genesId.get(gene)), region, regionNum, String.valueOf(consStrings), String.valueOf(consDetailsStrings)});
                        getAlleleFrequencies(response, getVariantId());
                        getClinicalSignificance(response, getVariantId(), pathologies);
                        incrementVariantId();
                        Variant.class.notify();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                geneMapElemIndex.getAndIncrement();
                if(geneMapElemIndex.equals(genesId.size())){
                    System.out.println(hgvs);
                }
//                    }
            });}
        else {
            String region = "";
            String regionNum = "";
            if (response.has("cadd") && response.getAsJsonObject("cadd").has("exon")) {
                if(response.getAsJsonObject("cadd").get("exon").isJsonArray())
                    regionNum = response.getAsJsonObject("cadd").getAsJsonArray("exon").toString();
                else
                    regionNum = response.has("cadd") && response.getAsJsonObject("cadd").has("exon") ? response.getAsJsonObject("cadd").getAsJsonPrimitive("exon").getAsString() : "";

                region = "exon";
            } else if (response.has("cadd") && response.getAsJsonObject("cadd").has("intron")){
                if(response.getAsJsonObject("cadd").get("intron").isJsonArray())
                    regionNum = response.getAsJsonObject("cadd").getAsJsonArray("intron").toString();
                else
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

//                if(!insertedHgvs.contains(hgvs)) {
            synchronized (lock) {
                try {
                    lock.wait();
                    write(new String[]{String.valueOf(getVariantId()), hgvs, chromosome, Integer.toString(start), Integer.toString(end), rsid, reference, alternative, null, region, regionNum, String.valueOf(consStrings), String.valueOf(consDetailsStrings)});
                    getAlleleFrequencies(response, getVariantId());
                    getClinicalSignificance(response, getVariantId(), pathologies);
                    incrementVariantId();
                    lock.notify();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(hgvs);
//                    insertedHgvs.add(hgvs);
//                }
        }

//        } catch(ClassCastException e){
//            System.out.println(hgvs);
//        }


    }

    private static void write(String[] data){
        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\variant.csv",
                new String[]{"Id", "HGVS", "Chromosome", "Start", "End", "DBSNP", "Reference", "Alternative", "GeneId", "Region", "RegionNum", "Consequence", "ConsequenceDetails"},
                data);
    }
}
