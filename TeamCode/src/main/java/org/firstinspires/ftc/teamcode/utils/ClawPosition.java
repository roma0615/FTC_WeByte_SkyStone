package org.firstinspires.ftc.teamcode.utils;

public enum ClawPosition {
    UP(0.75), DOWN(0.15);

    private double pos;

    public double getPos() {
        return this.pos;
    }

    ClawPosition(double p) {
        this.pos = p;
    }
}
