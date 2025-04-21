package com.example.objects;

import java.util.Objects;

public abstract class OperatorDecorator extends Formula {
    protected Formula left;
    protected Formula right;

    /**
     * Returns a string representation of this operator.
     * Includes the class name and the formula.
     *
     * @return A string representation of this operator
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{formula='" + getFormula() + "'}";
    }
}
