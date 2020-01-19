package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.utils.ArmPosition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;

import java.util.Locale;

/**

 * Currently used TeleOp by Roma and Nicholas 1/11/20
    TeleOp to reset the arm into a proper position with the limits off.
 */

@TeleOp(name="armReset")

public class armReset extends LinearOpMode {

    // Declare OpMode members.
    private FlipperPosition servoPosition = FlipperPosition.BOTTOM;
    //private ClawPosition clawPosition = ClawPosition.UP;
    //private ArmPosition armPosition = ArmPosition.UP;
    private double intakePower = 0;
    private boolean clawToggle = false;
    private boolean fingerToggle = false;
    private boolean wristToggle = false;
    private boolean forwardIntakeToggle = false;
    private boolean backwardIntakeToggle = false;
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
        telemetry.addData("Robot", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        while (opModeIsActive()) {
            // Controls movement of the robot
            drive();

            // Setting the position of the "flipper" servos
            if (gamepad2.dpad_up) {
                servoPosition = FlipperPosition.UP;
                //telemetry.addData("Servo:","UP");
            } else if (gamepad2.dpad_left || gamepad2.dpad_right) {
                servoPosition = FlipperPosition.BOTTOM;
                //telemetry.addData("Servo:","DOWN");
            } else if (gamepad2.dpad_down){
                servoPosition = FlipperPosition.DOWN;
            }
            Robot.setServos(servoPosition, 0, "");


            if (gamepad2.left_bumper){
                //clawPosition = ClawPosition.DOWN;
                clawToggle = !clawToggle;
                //sleep(200);
            }
            if(!clawToggle){
                Robot.setClawServo(ClawPosition.UP, 0, "");
            } else {
                Robot.setClawServo(ClawPosition.DOWN, 0, "");
            }


            if (gamepad2.y){
                backwardIntakeToggle = !backwardIntakeToggle;
                forwardIntakeToggle = false;
                //sleep(200);
            } else if(gamepad2.x) {
                forwardIntakeToggle = !forwardIntakeToggle;
                backwardIntakeToggle = false;
                //sleep(200);
            }
            if(forwardIntakeToggle){
                intakePower = -1.0;
            } else if (backwardIntakeToggle) {
                intakePower = 1.0;
            } else {
                intakePower = 0.0;
            }


            if(gamepad2.a){
                fingerToggle = !fingerToggle;
                //sleep(200);
                //armPosition = ArmPosition.UP;
                //Robot.setFingerServo(0,0,"");
            }
            if(!fingerToggle){
                Robot.setFingerServo(0,0,"");

            } else {
                Robot.setFingerServo(1.0,0,"");
            }

            if(gamepad2.left_stick_x > 0){
                wristToggle = !wristToggle;
                //sleep(200);
            }
            if (!wristToggle) {
                Robot.setWristServo(-0.2,0,"");
            } else {
                Robot.setWristServo(1,0,"");
            }
            if(gamepad2.right_stick_button){
                Robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                telemetry.addData("","BYPASS COMPLETE. ENCODER ROTATIONS RESET");
            } else {
                Robot.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                telemetry.addData("","Move to a position and press right stick button to fix it as 0.");
            }
            //Robot.setArmServos(armPosition, 0, "");
            // Telemetry
            telemetry.addData("Wheel Power", "front left (%.2f), front right (%.2f), " +
                            "back left (%.2f), back right (%.2f)", Robot.forwardLeftDrive1.getPower(), Robot.forwardRightDrive1.getPower(),
                    Robot.backLeftDrive2.getPower(), Robot.backRightDrive2.getPower());
            //telemetry.addData("range right", String.format(Locale.ENGLISH, "%.01f in", Robot.distanceSensor.getDistance(DistanceUnit.INCH)));
            //telemetry.addData("range front left", String.format(Locale.ENGLISH, "%.01f in", Robot.frontLeftSensor.getDistance(DistanceUnit.INCH)));
            //telemetry.addData("range front right", String.format(Locale.ENGLISH, "%.01f in", Robot.frontRightSensor.getDistance(DistanceUnit.INCH)));
            //telemetry.addData("range rear", String.format(Locale.ENGLISH, "%.01f in", Robot.rearSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("ArmMotor Rotations", Robot.armMotor.getCurrentPosition());
            telemetry.addData("Status", "Run Time: " + Robot.runtime.toString());
            telemetry.update();

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

        Robot.leftIntake.setPower(intakePower);
        Robot.rightIntake.setPower(intakePower);
        Robot.armMotor.setPower(gamepad2.right_stick_y * 0.5);


    }

}
