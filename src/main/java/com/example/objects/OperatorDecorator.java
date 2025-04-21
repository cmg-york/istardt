package com.example.objects;

public abstract class OperatorDecorator extends Formula {
    protected Formula left;
    protected Formula right;

    /**
     * Returns a string representation of this operator.
     *
     * @return A string representation of this operator
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{formula='" + getFormula() + "'}";
    }
}
