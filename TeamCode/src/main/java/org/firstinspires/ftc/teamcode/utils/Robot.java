package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    /* Declare OpMode members. */
    public static ElapsedTime runtime = new ElapsedTime();
    private static BooleanFunction opModeIsActive;

    private static double forwardSpeed = 1;
    private static double turnSpeed = 0.5;

    // Make them public in case we ever need to do something special with them
    public static DcMotor forwardLeftDrive1 = null;
    public static DcMotor backLeftDrive2 = null;
    public static DcMotor forwardRightDrive1 = null;
    public static DcMotor backRightDrive2 = null;
    public static Servo rightServo = null;
    public static Servo leftServo = null;
    public static Servo clawServo = null;

    private static HardwareMap hwMap;
    private static Telemetry telemetry;

    public static void init(HardwareMap hardwareMap, Telemetry tel, BooleanFunction opModeFunction) {
        hwMap = hardwareMap;
        telemetry = tel;
        // this is a way of passing the opModeIsActive() method from an OpMode class to a different class
        opModeIsActive = opModeFunction;

        /*
        Expansion hub mappings:
        Motors:
            0 = back right
            1 = back left
            2 = front right
            3 = front left
        Servos:
            0 = servoLeft
            1 = servoRight
            2 = servoClaw

         The bottom expansion hub is Hub 3 in the config
         The top expansion hub is Hub 2
         */
        forwardLeftDrive1 = hwMap.get(DcMotor.class, "forwardLeft_drive");
        forwardRightDrive1 = hwMap.get(DcMotor.class, "forwardRight_drive");
        backLeftDrive2 = hwMap.get(DcMotor.class, "backLeft_drive");
        backRightDrive2 = hwMap.get(DcMotor.class, "backRight_drive");
        rightServo = hwMap.get(Servo.class, "servoRight");
        leftServo = hwMap.get(Servo.class, "servoLeft");
        clawServo = hwMap.get(Servo.class, "servoClaw");

        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);
        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive2.setDirection(DcMotor.Direction.FORWARD);

        runtime.reset();
    }

    private static void waitForSeconds(double time, String msg) {
        runtime.reset();
        while (opModeIsActive.get() && (runtime.seconds() < time)) {
            telemetry.addData("Path", msg, runtime.seconds());
            telemetry.update();
        }
    }

    // These probably won't be used, but they're here if we need them
    public static void setForwardSpeed(double newForwardSpeed) {
        forwardSpeed = newForwardSpeed;
    }
    public static void setTurnSpeed(double newTurnSpeed) {
        turnSpeed = newTurnSpeed;
    }

    public static void turnRight(double time, String msg) {
        forwardLeftDrive1.setPower(turnSpeed);
        forwardRightDrive1.setPower(-turnSpeed);
        backLeftDrive2.setPower(turnSpeed);
        backRightDrive2.setPower(-turnSpeed);
        waitForSeconds(time, msg);
    }
    public static void turnLeft(double time, String msg){
        forwardLeftDrive1.setPower(-turnSpeed);
        forwardRightDrive1.setPower(turnSpeed);
        backLeftDrive2.setPower(-turnSpeed);
        backRightDrive2.setPower(turnSpeed);
        waitForSeconds(time, msg);
    }
    public static void goForward(double time, String msg){
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
        waitForSeconds(time, msg);
    }
    public static void goBack(double time, String msg){
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
        waitForSeconds(time, msg);
    }
    public static void strafeLeft(double time, String msg){
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
        waitForSeconds(time, msg);
    }
    public static void strafeRight(double time, String msg){
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
        waitForSeconds(time, msg);
    }
    public static void stopMoving(){
        forwardLeftDrive1.setPower(0);
        forwardRightDrive1.setPower(0);
        backLeftDrive2.setPower(0);
        backRightDrive2.setPower(0);
    }

    public static void setServos(FlipperPosition pos, double time, String msg) {
        rightServo.setPosition(pos.getRight());
        leftServo.setPosition(pos.getLeft());
        waitForSeconds(time, msg);
    }
    public static void setServos(double left, double right, double time, String msg) {
        rightServo.setPosition(right);
        leftServo.setPosition(left);
        waitForSeconds(time, msg);
    }
    public static void setClawServo(ClawPosition pos, double time, String msg){
        clawServo.setPosition(pos.getPos());
        waitForSeconds(time, msg);
    }
}
