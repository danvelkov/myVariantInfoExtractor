package tables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Variant {
    private String HGVS;
    private String chromosome;
    private Integer start;
    private Integer end;
    private String dbSNP;
    private String reference;
    private String alternative;
    private Integer geneId;
    private String region;
    private String regionNum;
    private String consequence;
    private String consequenceDetails;

    public Variant() {
    }

    public Variant(String HGVS, String chromosome, Integer start, Integer end, String dbSNP, String reference, String alternative, Integer geneId, String region, String regionNum, String consequence, String consequenceDetails) {
        this.HGVS = HGVS;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.dbSNP = dbSNP;
        this.reference = reference;
        this.alternative = alternative;
        this.geneId = geneId;
        this.region = region;
        this.regionNum = regionNum;
        this.consequence = consequence;
        this.consequenceDetails = consequenceDetails;
    }

    public String getHGVS() {
        return HGVS;
    }

    public void setHGVS(String HGVS) {
        this.HGVS = HGVS;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getDbSNP() {
        return dbSNP;
    }

    public void setDbSNP(String dbSNP) {
        this.dbSNP = dbSNP;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAlternative() {
        return alternative;
    }

    public void setAlternative(String alternative) {
        this.alternative = alternative;
    }

    public Integer getGeneId() {
        return geneId;
    }

    public void setGeneId(Integer geneId) {
        this.geneId = geneId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionNum() {
        return regionNum;
    }

    public void setRegionNum(String regionNum) {
        this.regionNum = regionNum;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getConsequenceDetails() {
        return consequenceDetails;
    }

    public void setConsequenceDetails(String consequenceDetails) {
        this.consequenceDetails = consequenceDetails;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "HGVS='" + HGVS + '\'' +
                ", chromosome='" + chromosome + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", dbSNP='" + dbSNP + '\'' +
                ", reference='" + reference + '\'' +
                ", alternative='" + alternative + '\'' +
                ", geneId=" + geneId +
                ", region='" + region + '\'' +
                ", regionNum='" + regionNum + '\'' +
                ", consequence='" + consequence + '\'' +
                ", consequenceDetails='" + consequenceDetails + '\'' +
                '}';
    }
}
