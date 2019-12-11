package org.firstinspires.ftc.teamcode.utils;

public enum ClawPosition {
    UP(1), DOWN(0.3);

    private double pos;

    public double getPos() {
        return this.pos;
    }

    ClawPosition(double p) {
        this.pos = p;
    }
}
