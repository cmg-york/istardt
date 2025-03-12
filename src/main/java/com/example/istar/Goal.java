package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Goal {
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlAttribute(name = "root")
    private boolean root;

    @XmlAttribute(name = "description")
    private String description;

    @XmlAttribute(name = "episodeLength")
    private String episodeLength;

    // For preconditions and negative preconditions (as simple string references)
    @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
    private List<String> pre;
    @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
    private List<String> npr;

    @XmlElement(name = "refinement", namespace = "https://example.org/istar-t")
    private Refinement refinement;

    // Getters and setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isRoot() {
        return root;
    }
    public void setRoot(boolean root) {
        this.root = root;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getEpisodeLength() {
        return episodeLength;
    }
    public void setEpisodeLength(String episodeLength) {
        this.episodeLength = episodeLength;
    }
    public List<String> getPre() {
        return pre;
    }
    public void setPre(List<String> pre) {
        this.pre = pre;
    }
    public List<String> getNpr() {
        return npr;
    }
    public void setNpr(List<String> npr) {
        this.npr = npr;
    }
    public Refinement getRefinement() {
        return refinement;
    }
    public void setRefinement(Refinement refinement) {
        this.refinement = refinement;
    }
}
