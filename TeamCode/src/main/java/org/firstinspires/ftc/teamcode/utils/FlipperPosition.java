package org.firstinspires.ftc.teamcode.utils;

public enum FlipperPosition {
    UP(0, 0), SIDE(1, 0.5), DOWN(0.5, 1);

    private double left, right;

    public double getLeft() {
        return this.left;
    }
    public double getRight() {
        return this.right;
    }

    FlipperPosition(double l, double r) {
        this.left = l;
        this.right = r;
    }
}
