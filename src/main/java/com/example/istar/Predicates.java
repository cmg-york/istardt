package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Predicates {
    @XmlElement(name = "predicate", namespace = "https://example.org/istar-t")
    private List<Predicate> predicateList;

    public List<Predicate> getPredicateList() {
        return predicateList;
    }
    public void setPredicateList(List<Predicate> predicateList) {
        this.predicateList = predicateList;
    }
}
