package ca.yorku.cmg.istardt.xmlparser.objects;

public class Export {
    private String ref;
    private float minVal;
    private float maxVal;
    private boolean continuous;
    public boolean isContinuous() {
        return continuous;
    }
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public float getMinVal() {
        return minVal;
    }
    public void setMinVal(float minVal) {
        this.minVal = minVal;
    }
    public float getMaxVal() {
        return maxVal;
    }
    public void setMaxVal(float maxVal) {
        this.maxVal = maxVal;
    }
}
