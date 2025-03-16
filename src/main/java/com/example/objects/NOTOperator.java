package com.example.objects;


import java.util.Objects;

/**
 * NOT operator implementation
 */
public class NOTOperator extends OperatorDecorator {

    public NOTOperator(Formula formula) {
        this.left = formula;
        this.right = null;
    }

    @Override
    public String getFormula() {
        return "NOT(" + left.getFormula() + ")";
    }

    /**
     * Compares this NOT operator to the specified object.
     * NOT operators are considered equal if they are of the same class and have equal operands.
     * This overrides the parent method because NOT only uses the left operand.
     *
     * @param obj The object to compare this operator to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        NOTOperator that = (NOTOperator) obj;

        // Compare operand formulas by their string representation to avoid potential cycles
        String thisLeftFormula = left != null ? left.getFormula() : null;
        String thatLeftFormula = that.left != null ? that.left.getFormula() : null;

        return Objects.equals(thisLeftFormula, thatLeftFormula);
    }

    /**
     * Returns a hash code value for this NOT operator.
     * The hash code is computed based on the class and left operand formula
     * to ensure it satisfies the contract with equals().
     *
     * @return A hash code value for this operator
     */
    @Override
    public int hashCode() {
        String leftFormula = left != null ? left.getFormula() : null;

        return Objects.hash(getClass(), leftFormula);
    }
}
