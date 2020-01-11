package org.firstinspires.ftc.teamcode.utils;

public enum ArmPosition {
    UP(1), MEASURING(0.2), DOWN(0);

    private double pos;

    public double getPos() {
        return this.pos;
    }

    ArmPosition(double p) {
        this.pos = p;
    }
}