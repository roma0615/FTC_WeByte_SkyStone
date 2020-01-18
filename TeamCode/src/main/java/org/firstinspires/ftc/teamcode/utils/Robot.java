package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
    public static DcMotor leftIntake = null;
    public static DcMotor rightIntake = null;
    public static DcMotor armMotor = null;
    public static Servo rightServo = null;
    public static Servo leftServo = null;
    public static Servo clawServo = null;
    public static Servo wristServo = null;
    public static Servo fingerServo = null;
    //TESTING THE DISTANCE SENSOR. MIGHT WANT TO REMOVE THIS LATER
    public static DistanceSensor distanceSensor = null;
    public static DistanceSensor frontLeftSensor = null;
    public static DistanceSensor frontRightSensor = null;
    public static DistanceSensor rearSensor = null;

    private static HardwareMap hwMap;
    private static Telemetry telemetry;

    public static void init(HardwareMap hardwareMap, Telemetry tel, BooleanFunction opModeFunction) {
        hwMap = hardwareMap;
        telemetry = tel;
        // this is a way of passing the opModeIsActive() method from an OpMode class to a different class
        opModeIsActive = opModeFunction;

        /*
        Expansion hub #1 mappings (address=2, the one with both a 1 and a 3 written on it):
          Motors:
            0 = back right (backRight_drive)
            1 = back left (backLeft_drive)
            2 = front right (forwardRight_drive)
            3 = front left (forwardLeft_drive)
          Servos:
            0 = right flipper (servoRight)
            1 = left flipper (servoLeft)
            2 = claw servo (servoClaw)

        Expansion hub #2 mappings (address=3, the one with no writing on the front):
          Motors:
            0 = stone intake left (stoneIntake_left)
            1 = stone intake right  (stoneIntake_right)
          Servos:

         */
        forwardLeftDrive1 = hwMap.get(DcMotor.class, "forwardLeft_drive");
        forwardRightDrive1 = hwMap.get(DcMotor.class, "forwardRight_drive");
        backLeftDrive2 = hwMap.get(DcMotor.class, "backLeft_drive");
        backRightDrive2 = hwMap.get(DcMotor.class, "backRight_drive");
        armMotor = hwMap.get(DcMotor.class, "armMotor");
        rightIntake = hwMap.get(DcMotor.class, "intakeRight");
        leftIntake = hwMap.get(DcMotor.class, "intakeLeft");
        rightServo = hwMap.get(Servo.class, "servoRight");
        leftServo = hwMap.get(Servo.class, "servoLeft");
        clawServo = hwMap.get(Servo.class, "servoClaw");
        fingerServo = hwMap.get(Servo.class, "fingerServo");
        wristServo = hwMap.get(Servo.class, "wristServo");
        //TESTING THE DISTANCE SENSOR. MIGHT WANT TO REMOVE THIS LATER.
        distanceSensor = hwMap.get(DistanceSensor.class, "distanceSensor");
        frontLeftSensor = hwMap.get(DistanceSensor.class, "frontLeftSensor");
        frontRightSensor = hwMap.get(DistanceSensor.class, "frontRightSensor");
        rearSensor = hwMap.get(DistanceSensor.class, "rearSensor");
        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);
        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive2.setDirection(DcMotor.Direction.FORWARD);
        leftIntake.setDirection(DcMotor.Direction.FORWARD);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);

        runtime.reset();
    }

    private static void waitForSeconds(double time, String msg) {
        runtime.reset();
        while (opModeIsActive.get() && (runtime.seconds() < time)) {
            telemetry.addData("Path", msg, runtime.seconds());
            //telemetry.update();
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
        stopMoving();
    }
    public static void turnLeft(double time, String msg){
        forwardLeftDrive1.setPower(-turnSpeed);
        forwardRightDrive1.setPower(turnSpeed);
        backLeftDrive2.setPower(-turnSpeed);
        backRightDrive2.setPower(turnSpeed);
        waitForSeconds(time, msg);
        stopMoving();
    }
    public static void goForward(double time, String msg){
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
        waitForSeconds(time, msg);
        stopMoving();
    }
    public static void goForwardContinuous(){
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
    }
    public static void goBackContinuous(){
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
    }
    public static void strafeLeftContinuous(){
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
    }
    public static void strafeRightContinuous(){
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
    }
    public static void goBack(double time, String msg){
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
        waitForSeconds(time, msg);
        stopMoving();
    }

    public static void strafeLeft(double time, String msg){
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
        waitForSeconds(time, msg);
        stopMoving();
    }
    public static void strafeRight(double time, String msg){
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
        waitForSeconds(time, msg);
        stopMoving();
    }
    public static void activateIntake(double intakePower,double time, String msg){
        rightIntake.setPower(intakePower);
        leftIntake.setPower(intakePower);
        waitForSeconds(time, msg);
        stopMoving();
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

    public static void checkDistanceSensors(double duration) {
        double difference = frontLeftSensor.getDistance(DistanceUnit.INCH) -
            frontRightSensor.getDistance(DistanceUnit.INCH);
        if(difference > 1.25){
            turnRight(duration, "adjusting for drift");
            //checkDistanceSensors(duration);
        } else if (difference < -1.25){
            turnLeft(duration, "adjusting for drift");
            //checkDistanceSensors(duration);
        }

    }

    //@Deprecated
    public static void setClawServo(ClawPosition pos, double time, String msg){
        clawServo.setPosition(pos.getPos());
        waitForSeconds(time, msg);
    }
}
