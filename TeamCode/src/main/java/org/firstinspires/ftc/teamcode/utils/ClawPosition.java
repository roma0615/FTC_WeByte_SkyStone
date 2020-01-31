package org.firstinspires.ftc.teamcode.utils;

public enum ClawPosition {
    UP(0), MEASURING(0.75), DOWN(1);

    private double pos;

    public double getPos() {
        return this.pos;
    }

    ClawPosition(double p) {
        this.pos = p;
    }
}
