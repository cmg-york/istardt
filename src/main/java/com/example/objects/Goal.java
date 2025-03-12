package com.example.objects;

/**
 * Extensions to the Goal class to add functionality needed for XML mapping
 */
public class Goal extends DecompositionElement {
    public int runs;

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }
}