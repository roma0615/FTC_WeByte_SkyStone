package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * The code is structured as a LinearOpMode
 *
 *   The desired path in this example is:
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="BuildingSiteMoveFoundationBLUE")

public class BuildingSiteMoveFoundationBLUE extends LinearOpMode {

    /* Declare OpMode members. */
    // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1;
    static final double     TURN_SPEED    = 0.5;
    private DcMotor forwardLeftDrive1 = null;
    private DcMotor backLeftDrive2 = null;
    private DcMotor forwardRightDrive1 = null;
    private DcMotor backRightDrive2 = null;
    private Servo rightServo = null;
    private Servo leftServo = null;
    @Override
    public void runOpMode() {
        forwardLeftDrive1  = hardwareMap.get(DcMotor.class, "forwardLeft_drive");
        forwardRightDrive1 = hardwareMap.get(DcMotor.class, "forwardRight_drive");
        backLeftDrive2  = hardwareMap.get(DcMotor.class, "backLeft_drive");
        backRightDrive2 = hardwareMap.get(DcMotor.class, "backRight_drive");
        rightServo = hardwareMap.get(Servo.class, "servoRight");
        leftServo = hardwareMap.get(Servo.class, "servoLeft");
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);
        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive2.setDirection(DcMotor.Direction.FORWARD);

        // Step 1: strafe left
        strafeLeft(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.7)) {
            telemetry.addData("Path", "Drive forward", runtime.seconds());
            telemetry.update();
        }

        // Step 1:  Drive forward for 3 seconds
        goForward(FORWARD_SPEED);
        rightServo.setPosition(0.15);
        leftServo.setPosition(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.8)) {
            telemetry.addData("Path", "Drive forward", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Servo grab
        stopMoving();
        rightServo.setPosition(0.65);
        leftServo.setPosition(0.5);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Grabbing the foundation", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backwards for 1 Second
        goBack(FORWARD_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 2.2)) {
            telemetry.addData("Path", "Driving Backward", runtime.seconds());
            telemetry.update();
        }
        // Move forward
        goForward(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.2)) {
            telemetry.addData("Path", "Driving Backward", runtime.seconds());
            telemetry.update();
        }
        // Step 4:  Turn left to move the foundation
        turnLeft(TURN_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 2.2)) {
            telemetry.addData("Path", "Turning left", runtime.seconds());
            telemetry.update();
        }
        // Step 4:  Strafe right for 1 Second
        rightServo.setPosition(0.15);
        leftServo.setPosition(1);
        strafeRight(FORWARD_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 1.2)) {
            telemetry.addData("Path", "Strafing right", runtime.seconds());
            telemetry.update();
        }
        // Move out of foundation
        goBack(FORWARD_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 0.3)) {
            telemetry.addData("Path", "Turning right", runtime.seconds());
            telemetry.update();
        }
        // Step 5: turn right
        turnRight(TURN_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 1.1)) {
            telemetry.addData("Path", "Turning right", runtime.seconds());
            telemetry.update();
        }
        // Step 6: strafe right
        strafeRight(FORWARD_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Strafing right", runtime.seconds());
            telemetry.update();
        }
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
    public void turnRight(double speed){
        forwardLeftDrive1.setPower(speed);
        forwardRightDrive1.setPower(-speed);
        backLeftDrive2.setPower(speed);
        backRightDrive2.setPower(-speed);
    }
    public void turnLeft(double speed){
        forwardLeftDrive1.setPower(-speed);
        forwardRightDrive1.setPower(speed);
        backLeftDrive2.setPower(-speed);
        backRightDrive2.setPower(speed);
    }
    public void goForward(double speed){
        forwardLeftDrive1.setPower(speed);
        forwardRightDrive1.setPower(speed);
        backLeftDrive2.setPower(speed);
        backRightDrive2.setPower(speed);
    }
    public void goBack(double speed){
        forwardLeftDrive1.setPower(-speed);
        forwardRightDrive1.setPower(-speed);
        backLeftDrive2.setPower(-speed);
        backRightDrive2.setPower(-speed);
    }
    public void strafeLeft(double speed){
        forwardLeftDrive1.setPower(-speed);
        forwardRightDrive1.setPower(speed);
        backLeftDrive2.setPower(speed);
        backRightDrive2.setPower(-speed);
    }
    public void strafeRight(double speed){
        forwardLeftDrive1.setPower(speed);
        forwardRightDrive1.setPower(-speed);
        backLeftDrive2.setPower(-speed);
        backRightDrive2.setPower(speed);
    }
    public void stopMoving(){
        forwardLeftDrive1.setPower(0);
        forwardRightDrive1.setPower(0);
        backLeftDrive2.setPower(0);
        backRightDrive2.setPower(0);
    }
}