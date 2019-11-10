package org.firstinspires.ftc.teamcode.utils;

public enum ServoPosition {
    UP(0, 0), SIDE(1, 0.15), DOWN(0.5, 0.65);

    private double left, right;

    public double getLeft() {
        return this.left;
    }
    public double getRight() {
        return this.right;
    }

    ServoPosition(double l, double r) {
        this.left = l;
        this.right = r;
    }
}
