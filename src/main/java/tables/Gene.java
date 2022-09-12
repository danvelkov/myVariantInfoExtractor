package tables;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.*;
import file.FileWriterForCsv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Gene {
    static int geneId = 1;

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

        for (Map.Entry<String, String> gene: geneMap.entrySet()) {
//            System.out.println(Arrays.toString(new String[]{String.valueOf(geneIdMap.get(gene.getKey())), gene.getKey(), gene.getValue(), String.valueOf(isPseudo)}));
            write(new String[]{String.valueOf(geneIdMap.get(gene.getKey())), gene.getKey(), gene.getValue(), String.valueOf(isPseudo)});
        }

        return geneIdMap;
    }

    private static void write(String[] data){
        FileWriterForCsv.writeDataLineByLine("E:\\Quanterall\\myVariantInfoExtractor\\resources\\gene.csv",
                new String[]{"Id", "GeneId", "Name", "Pseudo"}, data );
    }
}
