package ca.yorku.cmg.istardt.xmlparser.objects;

public class Options {
    private boolean continuous;
    private float infActionPenalty;

    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public float getInfActionPenalty() {
        return infActionPenalty;
    }

    public void setInfActionPenalty(float infActionPenalty) {
        this.infActionPenalty = infActionPenalty;
    }
}
