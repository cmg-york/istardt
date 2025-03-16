package com.example.objects;

import java.util.Objects;

public abstract class OperatorDecorator extends Formula {
    protected Formula left;
    protected Formula right;

    /**
     * Compares this operator to the specified object.
     * Operators are considered equal if they are of the same class and have equal operands.
     *
     * @param obj The object to compare this operator to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OperatorDecorator that = (OperatorDecorator) obj;

        // Compare operand formulas by their string representation to avoid potential cycles
        String thisLeftFormula = left != null ? left.getFormula() : null;
        String thatLeftFormula = that.left != null ? that.left.getFormula() : null;

        String thisRightFormula = right != null ? right.getFormula() : null;
        String thatRightFormula = that.right != null ? that.right.getFormula() : null;

        return Objects.equals(thisLeftFormula, thatLeftFormula) &&
                Objects.equals(thisRightFormula, thatRightFormula);
    }

    /**
     * Returns a hash code value for this operator.
     * The hash code is computed based on the class and operand formulas
     * to ensure it satisfies the contract with equals().
     *
     * @return A hash code value for this operator
     */
    @Override
    public int hashCode() {
        String leftFormula = left != null ? left.getFormula() : null;
        String rightFormula = right != null ? right.getFormula() : null;

        return Objects.hash(getClass(), leftFormula, rightFormula);
    }

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
