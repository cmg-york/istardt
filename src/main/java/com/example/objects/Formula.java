package com.example.objects;

public abstract class Formula {
    public abstract String getFormula();

    // Factory methods to create different formula types
    public static Formula createConstantFormula(final String value) {
        return new Formula() {
            @Override
            public String getFormula() {
                return value;
            }
        };
    }

    public static Formula createBooleanFormula(final boolean value) {
        return new Formula() {
            @Override
            public String getFormula() {
                return String.valueOf(value);
            }
        };
    }
}