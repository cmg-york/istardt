package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Single goal DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GoalDto {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private Boolean root;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private String episodeLength;

    @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
    private List<String> pre;

    @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
    private List<String> npr;

    @XmlElement(name = "refinement", namespace = "https://example.org/istar-t")
    private RefinementDto refinement;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getRoot() { return root; }
    public void setRoot(Boolean root) { this.root = root; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEpisodeLength() { return episodeLength; }
    public void setEpisodeLength(String episodeLength) { this.episodeLength = episodeLength; }

    public List<String> getPre() { return pre; }
    public void setPre(List<String> pre) { this.pre = pre; }

    public List<String> getNpr() { return npr; }
    public void setNpr(List<String> npr) { this.npr = npr; }

    public RefinementDto getRefinement() { return refinement; }
    public void setRefinement(RefinementDto refinement) { this.refinement = refinement; }
}
