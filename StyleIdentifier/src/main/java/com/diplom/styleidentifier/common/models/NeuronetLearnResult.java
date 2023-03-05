package com.diplom.styleidentifier.common.models;

public class NeuronetLearnResult {
    int right;
    double errorSum;

    public NeuronetLearnResult(int right, double errorSum) {
        this.right = right;
        this.errorSum = errorSum;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public double getErrorSum() {
        return errorSum;
    }

    public void setErrorSum(double errorSum) {
        this.errorSum = errorSum;
    }
}
