package tables;

import com.google.gson.JsonObject;
import file.FileWriterForCsv;

import java.util.*;

public class AllelleFrequency {
    static int alleleFrequencyId = 1;

    public static void getAlleleFrequencies(JsonObject response, int varId){
        //ALLELE FREQUENCY
        String af = response.has("gnomad_exome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af").toString() : "";
        String af_nfe_bgr = response.has("gnomad_genome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af_nfe_bgr") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af_nfe_bgr").toString() : "";
        String af_nfe_male = response.has("gnomad_genome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af_nfe_male") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af_nfe_male").toString() : "";
        String af_nfe_female = response.has("gnomad_genome") &&
                response.getAsJsonObject("gnomad_genome").has("af") &&
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").has("af_nfe_female") ?
                response.getAsJsonObject("gnomad_genome").getAsJsonObject("af").getAsJsonPrimitive("af_nfe_female").toString() : "";

        List<String[]> alleleFrequencyArray = new ArrayList<>();

        int genderId = 0;
        int populationId = 0;
        double frequency = 0.0;
        java.sql.Date updated = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        if(!af.isEmpty()) {
            populationId = 18;
            genderId = 3;
            frequency = Double.parseDouble(af);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(genderId), Integer.toString(populationId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
//            System.out.println(Integer.toString(alleleFrequencyId));
        }
        if(!af_nfe_bgr.isEmpty()) {
            populationId = 12;
            genderId = 3;
            frequency = Double.parseDouble(af_nfe_bgr);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(genderId), Integer.toString(populationId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
//            System.out.println(Integer.toString(alleleFrequencyId));
        }
        if(!af_nfe_male.isEmpty()) {
            populationId = 11;
            genderId = 1;
            frequency = Double.parseDouble(af_nfe_male);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(genderId), Integer.toString(populationId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
//            System.out.println(Integer.toString(alleleFrequencyId));
        }
        if(!af_nfe_female.isEmpty()) {
            populationId = 11;
            genderId = 2;
            frequency = Double.parseDouble(af_nfe_female);
            alleleFrequencyArray.add(new String[]{Integer.toString(alleleFrequencyId), Integer.toString(varId), Integer.toString(genderId), Integer.toString(populationId), Double.toString(frequency), updated.toString()});
            alleleFrequencyId++;
//            System.out.println(Integer.toString(alleleFrequencyId));
        }

        if(!alleleFrequencyArray.isEmpty())
            alleleFrequencyArray.forEach(AllelleFrequency::write);
    }
    private static void write(String[] data){
        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\alleleFrequency.csv",
                new String[]{"Id", "VariantId", "GenderId", "PopulationId", "Frequency", "Updated"}, data );
    }

}
