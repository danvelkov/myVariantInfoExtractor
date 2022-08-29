package tables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gene {
    private Integer id;
    @JsonProperty("cadd.gene.gene_id") private String geneId;
    @JsonProperty("cadd.gene.genename") private String name;
    private Boolean pseudo;

    public Gene() {
    }

    public Gene(Integer id, String geneId, String name, Boolean pseudo) {
        this.id = id;
        this.geneId = geneId;
        this.name = name;
        this.pseudo = pseudo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPseudo() {
        return pseudo;
    }

    public void setPseudo(Boolean pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return "Gene{" +
                "id=" + id +
                ", geneId='" + geneId + '\'' +
                ", name='" + name + '\'' +
                ", pseudo=" + pseudo +
                '}';
    }
}
