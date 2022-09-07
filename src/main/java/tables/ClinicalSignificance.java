package tables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file.FileReaderFromCsv;
import file.FileWriterForCsv;

import java.util.*;

public class ClinicalSignificance {

    static int clinicalSignificanceId = 1;

    public static Map<String, String> getPathologies(){
        List<String[]> result = FileReaderFromCsv.readFile("C:\\Users\\Dan\\Desktop\\GENETYLLIS_PATHOLOGY.csv");
        Map<String, String> pathologies = new HashMap<>();

        result.forEach(line -> {
            pathologies.put(line[1], line[0]);
        });

        return pathologies;
    }

    public static void getClinicalSignificance(JsonObject response, int varId, Map<String, String> pathologies){

        //CLINICAL SIGNIFICANCE
        Gson gson = new GsonBuilder().serializeNulls().create();
        try{
            if(response.has("clinvar") &&
                    response.getAsJsonObject("clinvar").has("rcv")) {
                if(response.getAsJsonObject("clinvar").getAsJsonArray("rcv").size() > 0) {
                    JsonArray rcvArray = response.getAsJsonObject("clinvar").getAsJsonArray("rcv");
                    rcvArray.forEach(rcv -> {
                        if(rcv.getAsJsonObject().has("conditions")){
                            if(rcv.getAsJsonObject().get("conditions").isJsonArray()) {
                                JsonArray conditionsArray = rcv.getAsJsonObject().getAsJsonArray("conditions");
                                conditionsArray.forEach(condition -> {
                                    if (condition.getAsJsonObject().has("identifiers")) {
                                        String accession = rcv.getAsJsonObject().has("accession") ? rcv.getAsJsonObject().getAsJsonPrimitive("accession").getAsString() : "";
                                        String pathologyId = pathologies.get(condition.getAsJsonObject().getAsJsonObject("identifiers").getAsJsonPrimitive("medgen").toString());
                                        String significance = rcv.getAsJsonObject().has("clinical_significance") ? rcv.getAsJsonObject().getAsJsonPrimitive("clinical_significance").toString() : "";
                                        String significanceId = getSignificance(significance);
                                        String evaluated = rcv.getAsJsonObject().has("last_evaluated") ? rcv.getAsJsonObject().getAsJsonPrimitive("last_evaluated").toString() : "";
                                        String updated = rcv.getAsJsonObject().has("review_status") ? rcv.getAsJsonObject().getAsJsonPrimitive("review_status").toString() : "";

//                                        System.out.println(Arrays.toString(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), String.valueOf(pathologyId), significanceId, evaluated, updated}));
                                        write(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), String.valueOf(pathologyId), significanceId, evaluated, updated});

                                        clinicalSignificanceId++;
                                    } else {
                                        String accession = rcv.getAsJsonObject().has("accession") ? rcv.getAsJsonObject().getAsJsonPrimitive("accession").getAsString() : "";
                                        String significance = rcv.getAsJsonObject().has("clinical_significance") ? rcv.getAsJsonObject().getAsJsonPrimitive("clinical_significance").toString() : "";
                                        String significanceId = getSignificance(significance);
                                        System.out.println(significanceId);
                                        String evaluated = rcv.getAsJsonObject().has("last_evaluated") ? rcv.getAsJsonObject().getAsJsonPrimitive("last_evaluated").toString() : "";
                                        String updated = rcv.getAsJsonObject().has("review_status") ? rcv.getAsJsonObject().getAsJsonPrimitive("review_status").toString() : "";

//                                        System.out.println(Arrays.toString(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), "", significanceId, evaluated, updated}));
                                        write(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), "", significanceId, evaluated, updated});

                                        clinicalSignificanceId++;
                                    }
                                });
                            }
                            else {
                                if(rcv.getAsJsonObject().getAsJsonObject("conditions").has("identifiers")) {
                                    String accession = rcv.getAsJsonObject().has("accession") ? rcv.getAsJsonObject().getAsJsonPrimitive("accession").getAsString() : "";
                                    String pathologyId = pathologies.get(rcv.getAsJsonObject().getAsJsonObject("conditions").getAsJsonObject("identifiers").getAsJsonPrimitive("medgen").toString());
                                    String significance = rcv.getAsJsonObject().has("clinical_significance") ? rcv.getAsJsonObject().getAsJsonPrimitive("clinical_significance").toString() : "";
                                    String significanceId = getSignificance(significance);
                                    String evaluated = rcv.getAsJsonObject().has("last_evaluated") ? rcv.getAsJsonObject().getAsJsonPrimitive("last_evaluated").toString() : "";
                                    String updated = rcv.getAsJsonObject().has("review_status") ? rcv.getAsJsonObject().getAsJsonPrimitive("review_status").toString() : "";

//                                    System.out.println(Arrays.toString(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), String.valueOf(pathologyId), significanceId, evaluated, updated}));
                                    write(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), String.valueOf(pathologyId), significanceId, evaluated, updated});

                                    clinicalSignificanceId++;
                                } else {
                                    String accession = rcv.getAsJsonObject().has("accession") ? rcv.getAsJsonObject().getAsJsonPrimitive("accession").getAsString() : "";
                                    String significance = rcv.getAsJsonObject().has("clinical_significance") ? rcv.getAsJsonObject().getAsJsonPrimitive("clinical_significance").toString() : "";
                                    String significanceId = getSignificance(significance);
                                    String evaluated = rcv.getAsJsonObject().has("last_evaluated") ? rcv.getAsJsonObject().getAsJsonPrimitive("last_evaluated").toString() : "";
                                    String updated = rcv.getAsJsonObject().has("review_status") ? rcv.getAsJsonObject().getAsJsonPrimitive("review_status").toString() : "";

//                                    System.out.println(Arrays.toString(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), "", significanceId, evaluated, updated}));
                                    write(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), "", significanceId, evaluated, updated});

                                    clinicalSignificanceId++;
                                }
                            }
                        }
                    });

                }else {
                    String accession = response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("accession") ? response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("accession").getAsString() : "";
                    String significance = response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("clinical_significance") ? response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("clinical_significance").toString() : "";
                    String significanceId = getSignificance(significance);
                    String evaluated = response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("last_evaluated") ? response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("last_evaluated").toString() : "";
                    String updated = response.getAsJsonObject("clinvar").getAsJsonObject("rcv").has("review_status") ? response.getAsJsonObject("clinvar").getAsJsonObject("rcv").getAsJsonPrimitive("review_status").toString() : "";

//                    System.out.println(Arrays.toString(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), "", significanceId, evaluated, updated}));
                   write(new String[]{String.valueOf(clinicalSignificanceId), accession, String.valueOf(varId), "", significanceId, evaluated, updated});

                    clinicalSignificanceId++;
                }
            }


//            if(rcv is defined) {
//                if(rcv is array) {
//                    if (conditions is array) {
//                        loop conditions {
//                            if(conditions has identifiers) {
//                                create object with cui
//                            } else {
//                                create object without cui
//                            }
//                        }
//                    } else {
//                        if(conditions has identifiers) {
//                            create object with cui
//                        } else {
//                            create object without cui
//                        }
//                    }
//                } else {
//                    create object with cui
//                }
//            }
        }
        catch(ClassCastException e){
//            System.out.println(response.getAsJsonObject("clinvar").getAsJsonArray("rcv").toString());
        }
    }

    private static String getSignificance(String significance){
        String significanceId;
        switch(significance.substring(1, significance.length() - 1)) {
            case "Pathogenic":
                significanceId = "1";
                break;
            case "Likely pathogenic":
                significanceId =  "2";
                break;
            case "Uncertain significance":
                significanceId =  "3";
                break;
            case "Likely benign":
                significanceId =  "4";
                break;
            case "Benign":
                significanceId =  "5";
                break;
            default: significanceId =  "";
                break;
        }

        return significanceId;
    }

    private static void write(String[] data){
        FileWriterForCsv.writeDataLineByLine("C:\\Users\\Dan\\Desktop\\output\\clinical_significance.csv",
                new String[]{"Id", "Accession", "VariantId", "PathologyId", "SignificanceId", "Evaluated", "ReviewStatus", "Updated"},
                data );
    }
}
