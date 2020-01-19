package org.firstinspires.ftc.teamcode.utils;

public enum ArmPosition {
    UP(0, 0), DOWN(0, 0.7);

    private double wrist, finger;

    public double getWrist() {
        return this.wrist;
    }
    public double getFinger() {
        return this.finger;
    }

    ArmPosition(double wrist, double finger) {
        this.wrist = wrist;
        this.finger = finger;
    }
}