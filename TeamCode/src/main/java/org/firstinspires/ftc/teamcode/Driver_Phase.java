package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;

import java.util.Locale;

/**

 * Currently used TeleOp by Roma and Nicholas 1/19/20
    TO DO:
    MUST FIX ALL TOGGLES
 */

@TeleOp(name="Driver_Phase")

public class Driver_Phase extends LinearOpMode {

    // Declare OpMode members.
    private FlipperPosition servoPosition = FlipperPosition.BOTTOM;
    //private ClawPosition clawPosition = ClawPosition.UP;
    //private ArmPosition armPosition = ArmPosition.UP;
    private double intakePower = 0;
    private boolean clawToggle = false;
    private boolean clawOn = false;
    private boolean fingerToggle = false;
    private boolean wristToggle = false;
    private boolean forwardIntakeToggle = false;
    private boolean backwardIntakeToggle = false;
    private boolean slowForwardIntakeToggle = false;
    private boolean intakeOn = false;
    private boolean backwardIntakeOn = false;
    private boolean fingerOn = false;
    private boolean wristOn = false;
    private boolean limitsOn = true;
    private boolean limitToggle = false;
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
        Robot.setWristServo(-0.2,0,"");
        Robot.setClawServo(ClawPosition.UP,0,"");
        Robot.setFingerServo(0,0,"");
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

            if(!clawToggle && gamepad2.y){
                Robot.capstoneServo.setPosition(clawOn ? 0 : 1);
                clawOn = !clawOn;
                clawToggle = true;
            } else if (!gamepad2.y) {
                clawToggle = false;
            }
            /*
            if(!clawToggle && gamepad2.left_bumper){
                Robot.setClawServo(clawOn ? ClawPosition.UP : ClawPosition.DOWN, 0, "");
                clawOn = !clawOn;
                clawToggle = true;
            } else if(!gamepad2.left_bumper) {
                clawToggle = false;
            }

             */
            /*
            if(!clawToggle && gamepad2.left_bumper){
                Robot.setClawServo(clawOn ? ClawPosition.UP : ClawPosition.DOWN, 0, "");
                clawOn = !clawOn;
                clawToggle = true;
            } else if(!gamepad2.left_bumper) {
                clawToggle = false;
            }

             */

            //Intake inward on x
            //Intake outward on y
            //Intake slow inward on b
            if(!forwardIntakeToggle && gamepad1.x){
                intakePower = (intakePower == -1 ? 0 : -1);
                forwardIntakeToggle = true;
            } else if(!gamepad1.x) {
                forwardIntakeToggle = false;
            }
            if(!backwardIntakeToggle && gamepad1.y){
                intakePower = (intakePower == 1 ? 0 : 1);
                backwardIntakeToggle = true;
            } else if(!gamepad1.y) {
                backwardIntakeToggle = false;
            }
            if(!slowForwardIntakeToggle && gamepad1.b){
                intakePower = (intakePower == -0.5 ? 0 : -0.5);
                slowForwardIntakeToggle = true;
            } else if(!gamepad1.b) {
                slowForwardIntakeToggle = false;
            }


            if(!fingerToggle && gamepad2.a){
                Robot.setFingerServo(fingerOn ? 0 : 1.0, 0, "");
                fingerOn = !fingerOn;
                fingerToggle = true;
            } else if(!gamepad2.a) {
                fingerToggle = false;
            }
            //Need limits on when the wrist can turn based on arm's current position
            if(!wristToggle && gamepad2.right_stick_button){
                Robot.setWristServo(wristOn ? -0.2 : 1.0, 0, "");
                wristOn = !wristOn;
                wristToggle = true;
            } else if(!gamepad2.right_stick_button) {
                wristToggle = false;
            }
            if(gamepad2.back){
                Robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                telemetry.addData("","BYPASS COMPLETE. ENCODER ROTATIONS RESET");
            } else {
                Robot.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            if(!limitToggle && gamepad2.right_bumper){
                limitsOn = (limitsOn == true ? false : true);
                limitToggle = true;
            } else if(!gamepad2.right_bumper) {
                limitToggle = false;
            }
            if(gamepad2.right_trigger > 0){
                Robot.superServoClaw.setPower(1);
            } else if (gamepad2.left_trigger > 0) {
                Robot.superServoClaw.setPower(-1);
            }

            //Robot.setArmServos(armPosition, 0, "");
            // Telemetry
            /*
            telemetry.addData("range right", String.format(Locale.ENGLISH, "%.01f in", Robot.rightSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range left", String.format(Locale.ENGLISH, "%.01f in", Robot.leftSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range front left", String.format(Locale.ENGLISH, "%.01f in", Robot.frontLeftSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range front right", String.format(Locale.ENGLISH, "%.01f in", Robot.frontRightSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("rear right", String.format(Locale.ENGLISH, "%.01f in", Robot.rearLeftSensor.getDistance(DistanceUnit.INCH)));
            telemetry.addData("rear left", String.format(Locale.ENGLISH, "%.01f in", Robot.rearRightSensor.getDistance(DistanceUnit.INCH)));
            */
            if(!limitsOn){
                telemetry.addData("WARNING", "LIMIT OVERRIDE ENGAGED. MOVE ARM WITH CAUTION");
            } else {
                //telemetry.addData("All clear", "Limits are online");
            }
            //telemetry.addData("Current Arm Position",Robot.armMotor.getCurrentPosition());
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



        Robot.forwardLeftDrive1.setPower(v1);
        Robot.forwardRightDrive1.setPower(v2);
        Robot.backLeftDrive2.setPower(v3);
        Robot.backRightDrive2.setPower(v4);

        Robot.leftIntake.setPower(intakePower);
        Robot.rightIntake.setPower(intakePower);

        if(Robot.armMotor.getCurrentPosition() < -8800 && limitsOn) {
            telemetry.addData("Arm Motor is too high!", " Lower it!");
            Robot.armMotor.setPower(0.1);
        } else if(Robot.armMotor.getCurrentPosition() > -20 && limitsOn){
            telemetry.addData("Arm Motor is too low!", " Raise it!");
            Robot.armMotor.setPower(-0.2);
        } else {
            Robot.armMotor.setPower(gamepad2.left_stick_y);
        }


    }
}
