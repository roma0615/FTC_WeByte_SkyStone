package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="BuildingSiteMoveFoundation")

public class BuildingSiteMoveFoundation extends LinearOpMode {

    /* Declare OpMode members. */
    // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 0.6;
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

        // Step 1:  Drive forward for 3 seconds

        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);
        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive2.setDirection(DcMotor.Direction.FORWARD);

        goForward(FORWARD_SPEED);
        rightServo.setPosition(0.1);
        leftServo.setPosition(0.1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Servo grab
        rightServo.setPosition(0.55);
        leftServo.setPosition(0.55);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 2.0)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backwards for 1 Second
        goBack(FORWARD_SPEED);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
    public void turnRight(double speed){
        forwardLeftDrive1.setPower(-speed);
        forwardRightDrive1.setPower(speed);
        backLeftDrive2.setPower(-speed);
        backRightDrive2.setPower(speed);
    }
    public void turnLeft(double speed){
        forwardLeftDrive1.setPower(speed);
        forwardRightDrive1.setPower(-speed);
        backLeftDrive2.setPower(speed);
        backRightDrive2.setPower(-speed);
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
}