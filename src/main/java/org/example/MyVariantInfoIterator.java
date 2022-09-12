package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;
import tables.ClinicalSignificance;
import thread.CustomRecursiveAction;
import thread.IterateList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

import static tables.Variant.getVariant;
import static tables.Variant.getVariantId;
import static thread.CustomRecursiveAction.batches;

public class MyVariantInfoIterator {

//    private static Set<String> insertedHgvs = new HashSet<>();

    public static void iterateHGVS(List<String> chrWithHGVS) throws InterruptedException, JsonProcessingException {
        System.out.println(chrWithHGVS.size());

        Map<String, String> pathologies = ClinicalSignificance.getPathologies();
//        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction(chrWithHGVS, pathologies);

//        List<Thread> threads = new ArrayList<>();
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        batches(chrWithHGVS, 100).forEach(batch -> {
//            Thread newThread = new Thread(new IterateList(batch, pathologies));
//            threads.add(newThread);
////            executorService.execute(new IterateList(batch, pathologies));
//            newThread.start();
//        });
//
////        executorService.shutdown();
//
//        for (Thread thread : threads) {
//            thread.join();
//        }
//
//        System.out.println(getVariantId());

        chrWithHGVS.stream().parallel().forEach(element -> {
            try {
                getMyVariantInfo(element, pathologies);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });


//        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction(batches(chrWithHGVS, 100).collect(), pathologies);
    }

    public static void getMyVariantInfo(String hgvs, Map<String, String> pathologies) throws FileNotFoundException {
//        if(insertedHgvs.size() > 400){
//            insertedHgvs = split(insertedHgvs, 200).get(1);
//        }

        try {
            System.out.println(hgvs);
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

            generateObjects(content.toString().replaceAll("\"", "\\\""), pathologies);

            in.close();

            con.disconnect();

        } catch (IOException e) {
//                throw new RuntimeException(e);
        }

    }

    public static void generateObjects(String json, Map<String, String> pathologies) throws JsonProcessingException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        JsonObject response = gson.fromJson(json, JsonObject.class);
        getVariant(response, pathologies);
    }

    public static <T> List<Set<T>> split(Set<T> original, int count) {
        // Create a list of sets to return.
        ArrayList<Set<T>> result = new ArrayList<Set<T>>(count);

        // Create an iterator for the original set.
        Iterator<T> it = original.iterator();

        // Calculate the required number of elements for each set.
        int each = original.size() / count;

        // Create each new set.
        for (int i = 0; i < count; i++) {
            HashSet<T> s = new HashSet<T>(original.size() / count + 1);
            result.add(s);
            for (int j = 0; j < each && it.hasNext(); j++) {
                s.add(it.next());
            }
        }
        return result;
    }


}
