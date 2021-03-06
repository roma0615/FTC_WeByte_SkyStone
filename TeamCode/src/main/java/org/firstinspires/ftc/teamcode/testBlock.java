package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;

import java.util.Locale;

/**

 * Currently used TeleOp by Roma and Nicholas 2/14/20

 */

@TeleOp(name="testBlock")

public class testBlock extends LinearOpMode {

    // Declare OpMode members.
    private FlipperPosition servoPosition = FlipperPosition.UP;
    private ClawPosition clawPosition = ClawPosition.UP;
    private double intakePower = 0;
    //private double armPower = 0;

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
        TensorFlowDetection.init(hardwareMap, telemetry);
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        while (opModeIsActive()) {
            // Controls movement of the robot
            drive();
            //Robot.superServoClaw.setPower(-1);
            /*
            // Setting the position of the "flipper" servos
            if (gamepad2.dpad_up) {
                servoPosition = FlipperPosition.UP;
                //telemetry.addData("Servo:","UP");
            } else if ((gamepad2.dpad_right || gamepad2.dpad_left)) {
                servoPosition = FlipperPosition.SIDE;
                //telemetry.addData("Servo:","SIDE");
            } else if (gamepad2.dpad_down) {
                servoPosition = FlipperPosition.DOWN;
                //telemetry.addData("Servo:","DOWN");
            }
            Robot.setServos(servoPosition, 0, "");
            if (gamepad2.left_bumper){
                clawPosition = ClawPosition.DOWN;
            } else if (gamepad2.right_bumper) {
                clawPosition = ClawPosition.UP;
            }
            if (gamepad2.y){
                intakePower = 1.0;
            } else if(gamepad2.x){
                intakePower = -1.0;
            } else {
                intakePower = 0.0;
            }

             */


            telemetry.addData("range right side front", String.format(Locale.ENGLISH, "%.01f in", Robot.rightSideFrontSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range right side rear", String.format(Locale.ENGLISH, "%.01f in", Robot.rightSideRearSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range left side front", String.format(Locale.ENGLISH, "%.01f in", Robot.leftSideFrontSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range left side rear", String.format(Locale.ENGLISH, "%.01f in", Robot.leftSideRearSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range front right", String.format(Locale.ENGLISH, "%.01f in", Robot.frontRightSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range rear right", String.format(Locale.ENGLISH, "%.01f in", Robot.rearRightSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("avg right front", String.format(Locale.ENGLISH, "%.01f in", Robot.getAverageRightFrontSensor()));
            telemetry.addData("avg right rear", String.format(Locale.ENGLISH, "%.01f in", Robot.getAverageRightRearSensor()));
            telemetry.addData("avg left front", String.format(Locale.ENGLISH, "%.01f in", Robot.getAverageLeftFrontSensor()));
            telemetry.addData("avg left rear", String.format(Locale.ENGLISH, "%.01f in", Robot.getAverageLeftRearSensor()));
            telemetry.addData("avg front right", String.format(Locale.ENGLISH, "%.01f in", Robot.getAverageFrontRightSensor()));
            telemetry.addData("avg rear right", String.format(Locale.ENGLISH, "%.01f in", Robot.getAverageRearRightSensor()));

          telemetry.update();
            //Robot.massTelemetryDump(10000000);

        }

    }



    private void drive() {
        // Do some ~~mathematics~~ to figure out how to power the mechanum wheels
        // Explained here: https://www.roboteq.com/index.php/applications/applications-blog/entry/driving-mecanum-wheels-omnidirectional-robots
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

        double rightX = gamepad1.right_stick_x;
        double v1 = r * Math.cos(robotAngle) + rightX;
        double v2 = r * Math.sin(robotAngle) - rightX;
        double v3 = r * Math.sin(robotAngle) + rightX;
        double v4 = r * Math.cos(robotAngle) - rightX;

        // FULL POWER BUTTONS
        if(gamepad1.left_bumper){
            v1 *= 1/0.7;
            v2 *= 1/0.7;
            v3 *= 1/0.7;
            v4 *= 1/0.7;
        }
        /*
        if(gamepad1.dpad_down){
            v1 = -1;
            v2 = -1;
            v3 = -1;
            v4 = -1;
        }
        */

        // Precise movement
        if(gamepad1.right_bumper) {
            v1 *= 0.5;
            v2 *= 0.5;
            v3 *= 0.5;
            v4 *= 0.5;
        }


        /*
        Robot.forwardLeftDrive1.setPower(v1);
        Robot.forwardRightDrive1.setPower(v2);
        Robot.backLeftDrive2.setPower(v3);
        Robot.backRightDrive2.setPower(v4);

         */
        /*
        Robot.leftIntake.setPower(intakePower);
        Robot.rightIntake.setPower(intakePower);
        if(Robot.armMotor.getCurrentPosition() < -5300) {
            telemetry.addData("Arm Motor is too high!", " Lower it!");
            Robot.armMotor.setPower(0.05);
        } else if(Robot.armMotor.getCurrentPosition() > -20){
            telemetry.addData("Arm Motor is too low!", " Raise it!");
            Robot.armMotor.setPower(-0.1);
        } else {
            Robot.armMotor.setPower(gamepad2.right_stick_y);
        }

         */

    }
}
