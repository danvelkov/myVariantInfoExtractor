package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;


public class MyVariantInfoIterator {
    public static void iterateHGVS(List<String> chrWithHGVS) throws InterruptedException {
        System.out.println("myvarinfo");
        System.out.println(chrWithHGVS.size());
        Queue<String> fullJSONResponse = new ConcurrentLinkedQueue<>();

        chrWithHGVS.forEach(hgvs -> {
            try {
                Singleton.getInstance().threadPool.execute(() -> {
                    try {
//                        System.out.println("url for " + hgvs);
                        URL url = new URL("https://myvariant.info/v1/variant/" + hgvs + "?fields=hg19.start,hg19.end,dbsnp.alt,dbsnp.ref,dbsnp.rsid,clinvar.rcv.conditions.identifiers.medgen,clinvar.rcv.conditions.name,cadd.consequence,cadd.consdetail,cadd.gene.gene_id,cadd.gene.genename,cadd.exon,cadd.intron,clinvar.rcv.clinical_significance,clinvar.rcv.last_evaluated,clinvar.rcv.review_status,gnomad_genome.af.af,gnomad_genome.af.af_nfe_bgr,gnomad_genome.af.af_nfe_male,gnomad_genome.af.af_nfe_female");
//                        System.out.println(url);

                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        Thread.sleep(1);
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }

                        fullJSONResponse.add(content.toString());
                        in.close();

                        con.disconnect();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Singleton.getInstance().threadPool.waitUntilAllTasksFinished();
        Singleton.getInstance().threadPool.stop();
//        System.out.println("full json:\n " + fullJSONResponse);
        System.out.println(chrWithHGVS.size());
        System.out.println(fullJSONResponse.size());
    }

}
