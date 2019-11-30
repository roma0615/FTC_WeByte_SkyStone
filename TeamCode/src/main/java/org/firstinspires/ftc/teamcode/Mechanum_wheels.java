package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.ServoPosition;

/**

 * Currently used TeleOp by Roma and Nicholas 11/29/19

 */

@TeleOp(name="MechanumWheels")

public class Mechanum_wheels extends LinearOpMode {

    // Declare OpMode members.
    private ServoPosition servoPosition = ServoPosition.SIDE;


    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override

    public void runOpMode() throws InterruptedException {
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() { return opModeIsActive(); }
        });

        Robot.stopMoving();
        telemetry.addData("Robot", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
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
            Robot.setServos(servoPosition, 0, "");

            // Telemetry
            telemetry.addData("Wheel Power", "front left (%.2f), front right (%.2f), " +
                            "back left (%.2f), back right (%.2f)", Robot.forwardLeftDrive1.getPower(), Robot.forwardRightDrive1.getPower(),
                    Robot.backLeftDrive2.getPower(), Robot.backRightDrive2.getPower());
            telemetry.addData("Status", "Run Time: " + Robot.runtime.toString());
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

        Robot.forwardLeftDrive1.setPower(v1);
        Robot.forwardRightDrive1.setPower(v2);
        Robot.backLeftDrive2.setPower(v3);
        Robot.backRightDrive2.setPower(v4);
    }
}
