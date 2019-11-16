/* The very first teleOp demo by Nicholas Pietraszek.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TESTING MOTOR", group="Linear Opmode")
public class BasicTeleOp_Trial1 extends LinearOpMode {
    @Override
    public void runOpMode() {
//        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
//            @Override
//            public boolean get() { return opModeIsActive(); }
//        });
        DcMotor motor;
        motor = hardwareMap.get(DcMotor.class, "motor1");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double power = gamepad1.left_stick_y;
            motor.setPower(power);

//            // Setup a variable for each drive wheel to save power level for telemetry
//            double leftPower;
//            double rightPower;
//
//            // Choose to drive using either Tank Mode, or POV Mode
//            // Comment out the method that's not used.  The default below is POV.
//
//            // POV Mode uses left stick to go forward, and right stick to turn.
//            // - This uses basic math to combine motions and is easier to drive straight.
//            double drive = -gamepad1.left_stick_y;
//            double turn  =  gamepad1.right_stick_x;
//            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
//            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
//
//            // Tank Mode uses one stick to control each wheel.
//            // - This requires no math, but it is hard to drive forward slowly and keep straight.
//            // leftPower  = -gamepad1.left_stick_y ;
//            // rightPower = -gamepad1.right_stick_y ;
//
//            // Send calculated power to wheels
//            Robot.forwardLeftDrive1.setPower(leftPower);
//            Robot.backLeftDrive2.setPower(leftPower);
//            Robot.forwardRightDrive1.setPower(rightPower);
//            Robot.backRightDrive2.setPower(rightPower);
//
//            // Show the elapsed game time and wheel power.
//            telemetry.addData("Status", "Run Time: " + Robot.runtime.toString());
//            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
//            telemetry.update();
        }
    }
}
