package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Goals {
    @XmlElement(name = "goal", namespace = "https://example.org/istar-t")
    private List<Goal> goalList;

    public List<Goal> getGoalList() {
        return goalList;
    }
    public void setGoalList(List<Goal> goalList) {
        this.goalList = goalList;
    }
}
