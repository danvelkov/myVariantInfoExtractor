package tables;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ClinicalSignificance {
    private Integer id;
    @JsonProperty("clinvar.rcv.accession") private String accession;
    private Integer variantId;
    private Integer pathologyId;
    private Integer significanceId;

    @JsonProperty("clinvar.rcv.last_evaluated") private Date evaluated;

    @JsonProperty("clinvar.rcv.review_status") private String reviewStatus;
    private Date updated;

    public ClinicalSignificance() {
    }

    public ClinicalSignificance(Integer id, String accession, Integer variantId, Integer pathologyId, Integer significanceId, Date evaluated, String reviewStatus, Date updated) {
        this.id = id;
        this.accession = accession;
        this.variantId = variantId;
        this.pathologyId = pathologyId;
        this.significanceId = significanceId;
        this.evaluated = evaluated;
        this.reviewStatus = reviewStatus;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public Integer getPathologyId() {
        return pathologyId;
    }

    public void setPathologyId(Integer pathologyId) {
        this.pathologyId = pathologyId;
    }

    public Integer getSignificanceId() {
        return significanceId;
    }

    public void setSignificanceId(Integer significanceId) {
        this.significanceId = significanceId;
    }

    public Date getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(Date evaluated) {
        this.evaluated = evaluated;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "ClinicalSignificance{" +
                "id=" + id +
                ", accession='" + accession + '\'' +
                ", variantId=" + variantId +
                ", pathologyId=" + pathologyId +
                ", significanceId=" + significanceId +
                ", evaluated=" + evaluated +
                ", reviewStatus='" + reviewStatus + '\'' +
                ", updated=" + updated +
                '}';
    }
}
