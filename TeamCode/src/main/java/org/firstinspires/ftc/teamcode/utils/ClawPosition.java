package org.firstinspires.ftc.teamcode.utils;

public enum ClawPosition {
    UP(0.8), MEASURING(0.2), DOWN(0);

    private double pos;

    public double getPos() {
        return this.pos;
    }

    ClawPosition(double p) {
        this.pos = p;
    }
}
