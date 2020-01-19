package org.firstinspires.ftc.teamcode.utils;

public enum FlipperPosition {
    UP(0.85, 0), BOTTOM(0.13, 0.84), DOWN(0.36, 0.62);

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
