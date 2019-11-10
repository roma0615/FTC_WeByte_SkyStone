package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.ServoPosition;

/**

 * Created by isong on 10/17/18.

 */

@TeleOp(name="MechanumWheels")

public class Mechanum_wheels extends LinearOpMode {

    // Declare OpMode members.

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor forwardLeftDrive1 = null;
    private DcMotor backLeftDrive2 = null;
    private DcMotor forwardRightDrive1 = null;
    private DcMotor backRightDrive2 = null;

    private Servo rightServo = null;
    private Servo leftServo = null;

    private ServoPosition servoPosition = ServoPosition.SIDE;


    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override

    public void runOpMode() throws InterruptedException {

        forwardLeftDrive1  = hardwareMap.get(DcMotor.class, "forwardLeft_drive");
        forwardRightDrive1 = hardwareMap.get(DcMotor.class, "forwardRight_drive");
        backLeftDrive2  = hardwareMap.get(DcMotor.class, "backLeft_drive");
        backRightDrive2 = hardwareMap.get(DcMotor.class, "backRight_drive");
        rightServo = hardwareMap.get(Servo.class, "servoRight");
        leftServo = hardwareMap.get(Servo.class, "servoLeft");

        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);

        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive2.setDirection(DcMotor.Direction.FORWARD);

        forwardLeftDrive1.setPower(0);
        forwardRightDrive1.setPower(0);
        backLeftDrive2.setPower(0);
        backRightDrive2.setPower(0);

        telemetry.addData("Robot", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Controls movement of the robot
            drive();

            // Setting the position of the "flipper" servos
            if (gamepad2.dpad_up) {
                servoPosition = ServoPosition.UP;
            } else if ((gamepad2.dpad_right || gamepad2.dpad_left)) {
                servoPosition = ServoPosition.SIDE;
            } else if (gamepad2.dpad_down) {
                servoPosition = ServoPosition.DOWN;
            }
            rightServo.setPosition(servoPosition.getRight());
            leftServo.setPosition(servoPosition.getLeft());

            // Telemetry
            telemetry.addData("Wheel Power", "front left (%.2f), front right (%.2f), " +
                            "back left (%.2f), back right (%.2f)", forwardLeftDrive1.getPower(), forwardRightDrive1.getPower(),
                    backLeftDrive2.getPower(), backRightDrive2.getPower());
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

        }

    }

    private void drive() {
        // Do some ~~mathematics~~ to figure out how to power the mecanum wheels
        // Explained here: https://www.roboteq.com/index.php/applications/applications-blog/entry/driving-mecanum-wheels-omnidirectional-robots
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

        double rightX = gamepad1.right_stick_x;
        double v1 = r * Math.cos(robotAngle) + rightX;
        double v2 = r * Math.sin(robotAngle) - rightX;
        double v3 = r * Math.sin(robotAngle) + rightX;
        double v4 = r * Math.cos(robotAngle) - rightX;

        // Precise movement
        if(gamepad1.right_bumper) {
            v1 *= 0.5;
            v2 *= 0.5;
            v3 *= 0.5;
            v4 *= 0.5;
        }

        forwardLeftDrive1.setPower(v1);
        forwardRightDrive1.setPower(v2);
        backLeftDrive2.setPower(v3);
        backRightDrive2.setPower(v4);
    }
}
