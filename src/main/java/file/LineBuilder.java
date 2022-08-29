package file;

import tables.AllelleFrequency;
import tables.ClinicalSignificance;
import tables.Gene;
import tables.Variant;

public class LineBuilder {
    public static String[] getVariantLine(Variant variant){
        return new String[]{variant.getHGVS(), variant.getChromosome(), variant.getStart().toString(), variant.getEnd().toString(), variant.getDbSNP(), variant.getReference(), variant.getAlternative(),
                variant.getGeneId().toString(), variant.getRegion(), variant.getRegionNum(), variant.getConsequence()};
    }

    public static String[] getGeneLine(Gene gene){
        return new String[]{gene.getGeneId(), gene.getName(), gene.getPseudo().toString()};
    }

    public static String[] getAlleleFrequencyLine(AllelleFrequency alleleFrequency){
        return new String[]{alleleFrequency.getVariantId().toString(),  alleleFrequency.getGenderId().toString(), alleleFrequency.getPopulationId().toString(),
                alleleFrequency.getFrequency().toString(), alleleFrequency.getUpdated().toString()};
    }

    public static String[] getClinicalSignificanceLine(ClinicalSignificance clinicalSignificance){
        return new String[]{clinicalSignificance.getAccession(),clinicalSignificance.getVariantId().toString(),  clinicalSignificance.getPathologyId().toString(),
                clinicalSignificance.getEvaluated().toString(), clinicalSignificance.getReviewStatus(), clinicalSignificance.getUpdated().toString()};
    }
}
