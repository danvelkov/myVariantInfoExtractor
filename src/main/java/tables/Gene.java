package tables;

import com.google.gson.*;
import file.FileWriterForCsv;

import java.util.HashMap;
import java.util.Map;

public class Gene {
    private static final Object lock = new Object();
    static int geneId = 0;

    public synchronized static boolean incrementGeneId(){
        ++geneId;
        return true;
    }

    public static Map<String, Integer> getGenes(JsonObject response){
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

                        synchronized (lock) {
                            try {
                                while(!incrementGeneId())
                                    lock.wait();

                                geneMap.put(geneInsideArray.getAsJsonPrimitive("gene_id").getAsString(),
                                        geneInsideArray.getAsJsonPrimitive("genename").getAsString());

                                geneIdMap.put(geneInsideArray.getAsJsonPrimitive("gene_id").getAsString(), geneId);
//                                incrementGeneId();

                                lock.notifyAll();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

//
//                        geneMap.put(geneInsideArray.getAsJsonPrimitive("gene_id").getAsString(),
//                                geneInsideArray.getAsJsonPrimitive("genename").getAsString());
//
//                        geneIdMap.put(geneInsideArray.getAsJsonPrimitive("gene_id").getAsString(), geneId);
//                        incrementGeneId();
                    }
                }

               geneMap.entrySet().forEach(entry -> {
                   write(new String[]{String.valueOf(geneIdMap.get(entry.getKey())), entry.getKey(), entry.getValue(), String.valueOf(false)});
               });
            } else
            if(response.getAsJsonObject("cadd").getAsJsonObject("gene").has("gene_id")
                    && response.getAsJsonObject("cadd").getAsJsonObject("gene").has("genename")) {

                boolean isPseudo = false;

                synchronized (lock) {
                    try {
                        while(!incrementGeneId())
                            lock.wait();

                        geneMap.put(response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("gene_id").toString(),
                                response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("genename").toString());
                        geneIdMap.put(response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("gene_id").toString(), geneId);

                        String geneIdMyVariantInfo = response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("gene_id").toString();
                        String geneNameMyVariantInfo = response.getAsJsonObject("cadd").getAsJsonObject("gene").getAsJsonPrimitive("genename").toString();
                        write(new String[]{String.valueOf(geneId), geneIdMyVariantInfo.substring(1, geneIdMyVariantInfo.length() - 1), geneNameMyVariantInfo.substring(1, geneNameMyVariantInfo.length() -1), String.valueOf(isPseudo)});

                        lock.notifyAll();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        System.out.println(geneIdMap);
        return geneIdMap;
    }

    private static void write(String[] data){
        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\gene.csv",
                new String[]{"Id", "GeneId", "Name", "Pseudo"}, data );
    }
}
