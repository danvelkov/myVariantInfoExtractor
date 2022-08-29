package tables;

import java.util.Date;

public class AllelleFrequency {
    private Integer id;
    private Integer variantId;
    private Integer genderId;
    private Integer populationId;
    private Double frequency;
    private Date updated;

    public AllelleFrequency() {
    }

    public AllelleFrequency(Integer id, Integer variantId, Integer genderId, Integer populationId, Double frequency, Date updated) {
        this.id = id;
        this.variantId = variantId;
        this.genderId = genderId;
        this.populationId = populationId;
        this.frequency = frequency;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Integer populationId) {
        this.populationId = populationId;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "AllelleFrequency{" +
                "id=" + id +
                ", variantId=" + variantId +
                ", genderId=" + genderId +
                ", populationId=" + populationId +
                ", frequency=" + frequency +
                ", updated=" + updated +
                '}';
    }
}
