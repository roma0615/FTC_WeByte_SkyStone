package org.firstinspires.ftc.teamcode.utils;

public enum ClawPosition {
    UP(0.25), DOWN(0.75);

    private double pos;

    public double getPos() {
        return this.pos;
    }

    ClawPosition(double p) {
        this.pos = p;
    }
}
