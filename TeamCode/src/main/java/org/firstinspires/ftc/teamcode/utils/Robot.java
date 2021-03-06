package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.Locale;

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
    public static Servo capstoneServo = null;
    public static CRServo superServoClaw = null;

    //Distance sensors
    public static DistanceSensor leftSideRearSensor = null;
    public static DistanceSensor leftSideFrontSensor = null;
    public static DistanceSensor rightSideRearSensor = null;
    public static DistanceSensor rightSideFrontSensor = null;
    //public static DistanceSensor rearLeftSensor = null;
    public static DistanceSensor rearRightSensor = null;
    public static DistanceSensor frontRightSensor = null;


    //  public static DistanceSensor rearRightSensor = null;
  //  public static DistanceSensor rearLeftSensor = null;
  //  public static DistanceSensor leftSensor = null;
    //public static
    //
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
        capstoneServo = hwMap.get(Servo.class, "capstoneServo");
        superServoClaw = hwMap.get(CRServo.class, "superServoClaw");

        //TESTING THE DISTANCE SENSOR. MIGHT WANT TO REMOVE THIS LATER.

        leftSideFrontSensor = hwMap.get(DistanceSensor.class, "leftSideFrontSensor");
        leftSideRearSensor = hwMap.get(DistanceSensor.class, "leftSideRearSensor");
        rightSideFrontSensor = hwMap.get(DistanceSensor.class, "rightSideFrontSensor");
        rightSideRearSensor = hwMap.get(DistanceSensor.class, "rightSideRearSensor");
        //rearLeftSensor = hwMap.get(ModernRoboticsI2cRangeSensor.class, "rearLeftSensor");
        rearRightSensor = hwMap.get(DistanceSensor.class, "rearRightSensor");
        frontRightSensor = hwMap.get(DistanceSensor.class, "frontRightSensor");

        superServoClaw.setDirection(DcMotor.Direction.FORWARD);
        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);
        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive2.setDirection(DcMotor.Direction.FORWARD);
        leftIntake.setDirection(DcMotor.Direction.FORWARD);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);

        runtime.reset();
    }

    public static void waitForSeconds(double time, String msg) {
        runtime.reset();
        while (opModeIsActive.get() && (runtime.seconds() < time)) {
            //telemetry.addData("Path", msg, runtime.seconds());
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

    public static void turnLeft(double time, String msg) {
        if(opModeIsActive.get()) {
            forwardLeftDrive1.setPower(-turnSpeed);
            forwardRightDrive1.setPower(turnSpeed);
            backLeftDrive2.setPower(-turnSpeed);
            backRightDrive2.setPower(turnSpeed);
            waitForSeconds(time, msg);
            stopMoving();
        }
    }

    public static void goForward(double time, String msg) {
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
        waitForSeconds(time, msg);
        stopMoving();
    }

    public static void goForwardContinuous() {
        forwardLeftDrive1.setPower(forwardSpeed);
        forwardRightDrive1.setPower(forwardSpeed);
        backLeftDrive2.setPower(forwardSpeed);
        backRightDrive2.setPower(forwardSpeed);
    }

    public static void goBackContinuous() {
        forwardLeftDrive1.setPower(-forwardSpeed);
        forwardRightDrive1.setPower(-forwardSpeed);
        backLeftDrive2.setPower(-forwardSpeed);
        backRightDrive2.setPower(-forwardSpeed);
    }

    public static void strafeLeftContinuous() {
        if (opModeIsActive.get()) {
            forwardLeftDrive1.setPower(-forwardSpeed);
            forwardRightDrive1.setPower(forwardSpeed);
            backLeftDrive2.setPower(forwardSpeed);
            backRightDrive2.setPower(-forwardSpeed);
        }
    }

    public static void strafeRightContinuous() {
        if (opModeIsActive.get()) {
            forwardLeftDrive1.setPower(forwardSpeed);
            forwardRightDrive1.setPower(-forwardSpeed);
            backLeftDrive2.setPower(-forwardSpeed);
            backRightDrive2.setPower(forwardSpeed);
        }
    }

    public static void goBack(double time, String msg) {
        if (opModeIsActive.get()) {
            forwardLeftDrive1.setPower(-forwardSpeed);
            forwardRightDrive1.setPower(-forwardSpeed);
            backLeftDrive2.setPower(-forwardSpeed);
            backRightDrive2.setPower(-forwardSpeed);
            waitForSeconds(time, msg);
            stopMoving();
        }
    }

    public static void strafeLeft(double time, String msg) {
        if (opModeIsActive.get()) {
            forwardLeftDrive1.setPower(-forwardSpeed);
            forwardRightDrive1.setPower(forwardSpeed);
            backLeftDrive2.setPower(forwardSpeed);
            backRightDrive2.setPower(-forwardSpeed);
            waitForSeconds(time, msg);
            stopMoving();
        }
    }

    public static void strafeRight(double time, String msg) {
        if (opModeIsActive.get()) {
            forwardLeftDrive1.setPower(forwardSpeed);
            forwardRightDrive1.setPower(-forwardSpeed);
            backLeftDrive2.setPower(-forwardSpeed);
            backRightDrive2.setPower(forwardSpeed);
            waitForSeconds(time, msg);
            stopMoving();
        }
    }

    public static void activateIntake(double intakePower, double time, String msg) {
        if (opModeIsActive.get()) {
            rightIntake.setPower(intakePower);
            leftIntake.setPower(intakePower);
            waitForSeconds(time, msg);
            stopMoving();
        }
    }

    public static void stopMoving() {
        forwardLeftDrive1.setPower(0);
        forwardRightDrive1.setPower(0);
        backLeftDrive2.setPower(0);
        backRightDrive2.setPower(0);
    }

    public static void setServos(FlipperPosition pos, double time, String msg) {
        if (opModeIsActive.get()) {
            rightServo.setPosition(pos.getRight());
            leftServo.setPosition(pos.getLeft());
            waitForSeconds(time, msg);
        }
    }

    public static void setServos(double left, double right, double time, String msg) {
        if (opModeIsActive.get()) {
            rightServo.setPosition(right);
            leftServo.setPosition(left);
            waitForSeconds(time, msg);
        }
    }

    public static void setArmServos(ArmPosition pos, double time, String msg) {
        if (opModeIsActive.get()) {
            fingerServo.setPosition(pos.getFinger());
            wristServo.setPosition(pos.getWrist());
            waitForSeconds(time, msg);
        }
    }

    public static void setFingerServo(double finger, double time, String msg) {
        if (opModeIsActive.get()) {
            fingerServo.setPosition(finger);
            waitForSeconds(time, msg);
        }
    }

    public static void setWristServo(double wrist, double time, String msg) {
        if (opModeIsActive.get()) {
            wristServo.setPosition(wrist);
            waitForSeconds(time, msg);
        }
    }

    @Deprecated
    public static void checkDistanceSensors(double duration) {
        if (opModeIsActive.get()) {
            //double difference = averageLeft - averageRight;
            double averageRight = getAverageFrontRightSensor();
            double averageLeft = getAverageFrontLeftSensor() - 1;
            if (averageLeft + averageRight < 200) {
                while (averageLeft - averageRight > 1
                        || averageLeft - averageRight < -1) {
                    if (averageLeft - averageRight > 1) {
                        Robot.turnRight(duration, "adjusting for drift");
                        //checkDistanceSensors(duration);
                        //Robot.massTelemetryDump(30);
                    }
                    if (averageLeft - averageRight < -1) {
                        Robot.turnLeft(duration, "adjusting for drift");
                        //checkDistanceSensors(duration);
                        //Robot.massTelemetryDump(30);
                    }
                    averageRight = getAverageFrontRightSensor();
                    averageLeft = getAverageFrontLeftSensor();
                }
            }
        }
    }
    @Deprecated
    public static void checkDistanceRearSensors(double duration) {
        if (opModeIsActive.get()) {
            //double difference = averageLeft - averageRight;
            double averageRight = getAverageRearRightSensor();
            double averageLeft = getAverageRearLeftSensor() - 2;
            if (averageLeft + averageRight < 200) {
                while (averageLeft - averageRight > 1
                        || averageLeft - averageRight < -1) {
                    if (averageLeft - averageRight > 1) {
                        Robot.turnLeft(duration, "adjusting for drift");
                        //checkDistanceSensors(duration);
                        //Robot.massTelemetryDump(30);
                    }
                    if (averageLeft - averageRight < -1) {
                        Robot.turnRight(duration, "adjusting for drift");
                        //checkDistanceSensors(duration);
                        //Robot.massTelemetryDump(30);
                    }
                    averageRight = getAverageRearRightSensor();
                    averageLeft = getAverageRearLeftSensor() - 2;
                }
            }
        }
    }




    public static void massTelemetryDump(double time) {
        runtime.reset();
        while (opModeIsActive.get() && (runtime.seconds() < time)) {
            //telemetry.addData("Path", msg, runtime.seconds());
            //telemetry.update();
            telemetry.addData("range right front", String.format(Locale.ENGLISH, "%.01f in", Robot.rightSideFrontSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range right rear", String.format(Locale.ENGLISH, "%.01f in", Robot.rightSideRearSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range left front", String.format(Locale.ENGLISH, "%.01f in", Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range left rear", String.format(Locale.ENGLISH, "%.01f in", Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range front right", String.format(Locale.ENGLISH, "%.01f in", Robot.frontRightSensor.getDistance(DistanceUnit.INCH)));
            //telemetry.addData("range rear right", String.format(Locale.ENGLISH, "%.01f in", Robot.rearLeftSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range rear left", String.format(Locale.ENGLISH, "%.01f in", Robot.rearRightSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("average range rear left", String.format(Locale.ENGLISH, "%.01f in", getAverageRearLeftSensor()));

            int i = 0;
            if (TensorFlowDetection.getRecognitions() != null) {
                for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                    telemetry.addData(String.format(Locale.ENGLISH, "label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format(Locale.ENGLISH, "  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format(Locale.ENGLISH, "  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                }
            }
            telemetry.update();
        }
    }

    public static void setArmPartsDown() {
        if (opModeIsActive.get()) {
            setFingerServo(0.0, 0, "");
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setTargetPosition(-60);
            setFingerServo(1.0, 0, "");
            armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public static void setArmPartsUp() {
        if (opModeIsActive.get()) {


        }

    }
    @Deprecated
    public static double getAverageRightSensor() {
        double averageRight = 0;
        for (int i = 0; i < 5; i++) {
            averageRight += Robot.rightSideFrontSensor.getDistance(DistanceUnit.INCH);
        }
        averageRight = averageRight / 5;
        return averageRight;
    }
    @Deprecated
    public static double getAverageLeftSensor() {
        double averageRight = 0;
        for (int i = 0; i < 5; i++) {
            averageRight += Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH);
        }
        averageRight = averageRight / 5;
        return averageRight;
    }

    public static double getAverageRightFrontSensor() {
        double averageRight = 0;
        double right = 0;
        int i = 0;
        int j = 0;

        while (i < 5) {
            right = (Robot.rightSideFrontSensor.getDistance(DistanceUnit.INCH));
            if (right < 300) {
                averageRight += right;
            i = i + 1;}
            j = j + 1;
            if (j > 10) {
            averageRight = 322 * 5;
            break;
            }
        }
        averageRight = averageRight / 5;
        return averageRight;
    }

    public static double getAverageRightRearSensor() {
        double averageRight = 0;
        double right = 0;
        int i = 0;
        int j = 0;

        while (i < 5) {
            right = (Robot.rightSideRearSensor.getDistance(DistanceUnit.INCH));
            if (right < 300) {
                averageRight += right;
            i = i + 1;}
            j = j + 1;
            if (j > 10) {
                averageRight = 322 * 5;
                break;
        }
            }
        averageRight = averageRight / 5;
        return averageRight;

    }

    public static double getAverageLeftRearSensor() {
        double averageRight = 0;
        double right = 0;
        int i = 0;
        int j = 0;

        while (i < 5) {
            right = (Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH));
            if (right < 300) {
                averageRight += right;
            i = i + 1;}
            j = j + 1;
            if (j > 10){
                averageRight = 322 * 5;
                break;
            }
    }
        averageRight = averageRight / 5;
        return averageRight;
    }

    public static double getAverageLeftFrontSensor() {
        double averageLeft = 0;
        double right = 0;
        int i = 0;
        int j = 0;

        while (i < 5) {
            right = (Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH));
            if (right < 300) {
                averageLeft += right;
            i = i + 1;}
            j = j + 1;
            if (j > 10) {
                averageLeft = 322 * 5;
                break;
        }
    }
        averageLeft = averageLeft / 5;
        return averageLeft;
    }

    public static double getAverageRearRightSensor() {
        double averageRight = 0;
        double right = 0;
        int i = 0;
        int j = 0;

        while (i < 5) {
            right = (Robot.rearRightSensor.getDistance(DistanceUnit.INCH));
            if (right < 300) {
                averageRight += right;
            i = i + 1;}
            j = j + 1;
            if (j > 10){
                averageRight = 322 * 5;
                break;
        }
    }
        averageRight = averageRight / 5;
        return averageRight;
    }

    public static double getAverageFrontRightSensor() {
        double averageRight = 0;
        double right = 0;
        int i = 0;
        int j = 0;

        while (i < 5) {
            right = (Robot.frontRightSensor.getDistance(DistanceUnit.INCH));
            if (right < 300) {
                averageRight += right;
            i = i + 1;}
            j = j + 1;
            if (j > 10){
                averageRight = 322 * 5;
                break;
            }

    }
        averageRight = averageRight / 5;
        return averageRight;
    }

    @Deprecated
    public static double getAverageRearLeftSensor() {
        double averageLeft = 0;
        for (int i = 0; i < 5; i++) {
            //averageLeft += Robot.rearLeftSensor.getDistance(DistanceUnit.INCH);
        }
        averageLeft = averageLeft / 5;
        return averageLeft;
    }


    @Deprecated
    public static double getAverageFrontLeftSensor() {
        double averageRight = 0;
        for (int i = 0; i < 5; i++) {
            averageRight += Robot.frontRightSensor.getDistance(DistanceUnit.INCH);
        }
        averageRight = averageRight / 5;
        return averageRight;
    }

    @Deprecated
    public static void centerRobot(double distanceFromWall) {
        double averageRight = getAverageRightRearSensor();
        double tolerance = 0;
        double jump = 0;
        while (averageRight < (distanceFromWall - tolerance) || averageRight
                > (distanceFromWall + tolerance) && averageRight < 100  && opModeIsActive.get())
        {
            if (averageRight < (distanceFromWall - tolerance)) {
                if (averageRight < (distanceFromWall - (tolerance * 2))) {
                    strafeLeft(((distanceFromWall - averageRight) / 30), "");
                } else {
                    strafeLeft(jump, "");
                }
            }

            if (averageRight > (distanceFromWall + tolerance)) {
                if (averageRight > (distanceFromWall + (tolerance * 2))) {
                    strafeRight(((averageRight - distanceFromWall) / 30), "");
                } else {
                    strafeRight(jump, "");
                }
            }
            averageRight = getAverageRightRearSensor();
        }
    }

    public static void centerRobotForward(double distanceFromWall, double tolerance, double jump) {
        double averageRight = getAverageFrontRightSensor();
        while (averageRight < (distanceFromWall - tolerance) || averageRight
                > (distanceFromWall + tolerance) && averageRight < 100  && opModeIsActive.get())
        {
            if (averageRight < (distanceFromWall - tolerance)) {
                if (averageRight < (distanceFromWall - (tolerance * 2))) {
                    goBack(((distanceFromWall - averageRight) / 50), "");
                } else {
                    goBack(jump, "");
                }
            }

            if (averageRight > (distanceFromWall + tolerance)) {
                if (averageRight > (distanceFromWall + (tolerance * 2))) {
                    goForward(((averageRight - distanceFromWall) / 50), "");
                } else {
                    goForward(jump, "");
                }
            }
            averageRight = getAverageFrontRightSensor();
        }
    }


    //Centers robot on a distance away from the wall.
    public static void centerRobotRear(double distanceFromWall) {
            double averageRear = getAverageRearRightSensor();
                while (averageRear < (distanceFromWall - 0.4) || averageRear
                        > (distanceFromWall + 0.4) && averageRear < 100  && opModeIsActive.get())
                {
                    if (averageRear > (distanceFromWall + 0.4)) {
                        if (averageRear > (distanceFromWall + 0.8)) {
                            goBack(((averageRear - distanceFromWall) / 35), "jump back");
                        } else {
                            goBack(0.01, "inch back");
                        }
                    }

                    if (averageRear < (distanceFromWall - 0.4)) {
                        if (averageRear < (distanceFromWall - 0.8)) {
                            goForward(((distanceFromWall - averageRear) / 35), "jump forward");
                        } else {
                            goForward(0.01, "inch forward");
                        }
                    }
                    averageRear = getAverageRearRightSensor();
                }
    }




    public static void centerRearRobot(double distanceFromWall) {
        if (opModeIsActive.get()) {
            double averageRear = getAverageRearRightSensor();
            if (averageRear < 100) {
                while (averageRear < (distanceFromWall - 0.4) || averageRear
                        > (distanceFromWall + 0.4)) {
                    if (averageRear < (distanceFromWall - 0.4)) {
                        goForward(0.01, "adjusting for drift");
                    }
                    if (averageRear > (distanceFromWall + 0.4)) {
                        goBack(0.01, "adjusting for drift");
                                          }
                    averageRear = getAverageRearRightSensor();
                }
            }
        }
    }


    public static void centerRobotRight(double distanceFromWall, double tolerance, double jump) {
            double averageRight = getAverageRightRearSensor();
                while (averageRight < (distanceFromWall - tolerance) || averageRight
                        > (distanceFromWall + tolerance) && averageRight < 100  && opModeIsActive.get())
                {
                    if (averageRight < (distanceFromWall - tolerance)) {
                        if (averageRight < (distanceFromWall - (tolerance * 2))) {
                            strafeLeft(((distanceFromWall - averageRight) / 30), "");
                        } else {
                            strafeLeft(jump, "");
                        }
                    }

                    if (averageRight > (distanceFromWall + tolerance)) {
                        if (averageRight > (distanceFromWall + (tolerance * 2))) {
                            strafeRight(((averageRight - distanceFromWall) / 30), "");
                        } else {
                            strafeRight(jump, "");
                        }
                    }
                    averageRight = getAverageRightRearSensor();
                }
        }

    public static void centerRobotLeft(double distanceFromWall, double tolerance, double jump) {
        double averageLeft = getAverageLeftRearSensor();
        while (averageLeft < (distanceFromWall - tolerance) || averageLeft
                > (distanceFromWall + tolerance) && averageLeft < 100  && opModeIsActive.get())
        {
            if (averageLeft < (distanceFromWall - tolerance)) {
                if (averageLeft < (distanceFromWall - (tolerance * 2))) {
                    strafeRight(((distanceFromWall - averageLeft) / 30), "");
                } else {
                    strafeRight(jump, "");
                }
            }

            if (averageLeft > (distanceFromWall + tolerance)) {
                if (averageLeft > (distanceFromWall + (tolerance * 2))) {
                    strafeLeft(((averageLeft - distanceFromWall) / 30), "");
                } else {
                    strafeLeft(jump, "");
                }
            }
            averageLeft = getAverageLeftRearSensor();
        }
    }



    public static void BackwardStraightLeft(double distance, double time) {
        runtime.reset();
        while (opModeIsActive.get() && (runtime.seconds() < time)) {
            if (Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH) > distance) {
                forwardLeftDrive1.setPower(-0.9);
                backLeftDrive2.setPower(-0.9);
                forwardRightDrive1.setPower(-1.0);
                backRightDrive2.setPower(-1.0);
            } else if (Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH) < distance) {
                forwardLeftDrive1.setPower(-1.0);
                backLeftDrive2.setPower(-1.0);
                forwardRightDrive1.setPower(-0.9);
                backRightDrive2.setPower(-0.9);
            } else {
                forwardLeftDrive1.setPower(-1.0);
                backLeftDrive2.setPower(-1.0);
                forwardRightDrive1.setPower(-1.0);
                backRightDrive2.setPower(-1.0);
            }
        }
        Robot.stopMoving();
    }

    public static void ForwardStraightTimeLeft(double distance, double time) {
        runtime.reset();
        while (opModeIsActive.get() && (runtime.seconds() < time)) {
            if (Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH) > distance) {
                forwardLeftDrive1.setPower(0.9);
                backLeftDrive2.setPower(0.9);
                forwardRightDrive1.setPower(1.0);
                backRightDrive2.setPower(1.0);
            } else if (Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH) < distance) {
                forwardLeftDrive1.setPower(1.0);
                backLeftDrive2.setPower(1.0);
                forwardRightDrive1.setPower(0.9);
                backRightDrive2.setPower(0.9);
            } else {
                forwardLeftDrive1.setPower(1.0);
                backLeftDrive2.setPower(1.0);
                forwardRightDrive1.setPower(1.0);
                backRightDrive2.setPower(1.0);
            }
        }
        Robot.stopMoving();
    }
    @Deprecated
    public static void ForwardStraightTime(int distance, double skystoneDistance){}
    @Deprecated
    public static void BackwardStraightDistance(double distance, double skystoneDistance){}
    @Deprecated
    public static void BackwardStraight(int distance, double skystoneDistance){}
    @Deprecated
    public static void ForwardStraight(int distance, double skystoneDistance){}
    public static void ForwardStraightLeft(double distance, double skystoneDistance) {
        runtime.reset();
        while (opModeIsActive.get() && Robot.frontRightSensor.getDistance(DistanceUnit.INCH) > skystoneDistance) {
            if (Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH) > distance) {
                forwardLeftDrive1.setPower(0.9);
                backLeftDrive2.setPower(0.9);
                forwardRightDrive1.setPower(1.0);
                backRightDrive2.setPower(1.0);
            } else if (Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH) < distance) {
                forwardLeftDrive1.setPower(1.0);
                backLeftDrive2.setPower(1.0);
                forwardRightDrive1.setPower(0.9);
                backRightDrive2.setPower(0.9);
            } else {
                forwardLeftDrive1.setPower(1.0);
                backLeftDrive2.setPower(1.0);
                forwardRightDrive1.setPower(1.0);
                backRightDrive2.setPower(1.0);
            }
        }
        Robot.stopMoving();
    }

    public static void BackwardStraightDistanceLeft(double distance, double skystoneDistance) {
        runtime.reset();
        while (opModeIsActive.get() && Robot.rearRightSensor.getDistance(DistanceUnit.INCH) > skystoneDistance) {
            if (Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH) > distance) {
                forwardLeftDrive1.setPower(-0.9);
                backLeftDrive2.setPower(-0.9);
                forwardRightDrive1.setPower(-1.0);
                backRightDrive2.setPower(-1.0);
            } else if (Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH) < distance) {
                forwardLeftDrive1.setPower(-1.0);
                backLeftDrive2.setPower(-1.0);
                forwardRightDrive1.setPower(-0.9);
                backRightDrive2.setPower(-0.9);
            } else {
                forwardLeftDrive1.setPower(-1.0);
                backLeftDrive2.setPower(-1.0);
                forwardRightDrive1.setPower(-1.0);
                backRightDrive2.setPower(-1.0);
            }
        }
        Robot.stopMoving();
    }


    //@Deprecated
    public static void setClawServo(ClawPosition pos, double time, String msg) {
        if (opModeIsActive.get()) {
            clawServo.setPosition(pos.getPos());
            waitForSeconds(time, msg);
        }
    }

    public static void moveIn() {
        double averageRight = getAverageRearRightSensor();
        double tolerance = 3;
        while (averageRight > tolerance && opModeIsActive.get()
                && averageRight < 100) {
            if (averageRight > (tolerance + 0.8)) {
                goBack(((averageRight - tolerance) / 70), "");
            } else {
                goBack(0.04, "");
            }
            averageRight = getAverageRearRightSensor();
        }
    }

    @Deprecated
    public static void moveOver() {
        if (opModeIsActive.get()) {
            double right = getAverageRightSensor() / 15;
            if (right < 10) {
                Robot.strafeRight(right, "");
            }
        }
    }
    @Deprecated
    public static void moveOverLeft(double distancefromleftwall) {
        if (opModeIsActive.get()) {
            double left = getAverageLeftSensor();
            if (left < 100) {
                double distance = left - distancefromleftwall;
                double time = distance / 13.6;
                if (time > 0) {
                    Robot.strafeLeft(time, "");
                } else {
                    time = -time;
                    Robot.strafeRight(time, "");
                }
            }
        }
    }




    public static boolean alignLeft () {
            double duration = 0.02;
            double tolerance = 1.5;
            boolean status = true;
            double averageLeftFront = getAverageLeftFrontSensor();
            double averageLeftRear = getAverageLeftRearSensor() - 1;

            if ((averageLeftFront + averageLeftRear) > 200) {status = false;}
            while ((averageLeftFront - averageLeftRear > tolerance
                        || averageLeftFront - averageLeftRear < -tolerance) && (averageLeftFront + averageLeftRear < 200)) {

                    if (averageLeftFront - averageLeftRear > tolerance) {
                        Robot.turnLeft(duration, "");
                    }
                    if (averageLeftFront - averageLeftRear < -tolerance) {
                        Robot.turnRight(duration, "");
                    }
                    averageLeftRear = getAverageLeftRearSensor();
                    averageLeftFront = getAverageLeftFrontSensor();
                }
            return status;
            }

    public static boolean alignRight () {
        boolean status = true;
        double duration = 0.02;
        double tolerance = 1.5;
        double averageRightFront = getAverageRightFrontSensor();
        double averageRightRear = getAverageRightRearSensor() - 0.5;

        if ((averageRightFront + averageRightRear) > 200) {status = false;}
        while ((averageRightFront - averageRightRear > tolerance
                || averageRightFront - averageRightRear < -tolerance) && (averageRightFront + averageRightRear < 200)) {

            if (averageRightFront - averageRightRear > tolerance) {
                Robot.turnLeft(duration, "");
            }
            if (averageRightFront - averageRightRear < -tolerance) {
                Robot.turnRight(duration, "");
            }
            averageRightRear = getAverageLeftRearSensor();
            averageRightFront = getAverageLeftFrontSensor();
        }
        return status;
    }



}