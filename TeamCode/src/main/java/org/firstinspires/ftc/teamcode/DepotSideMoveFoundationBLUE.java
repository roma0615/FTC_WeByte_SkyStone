package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;

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

@Disabled
@Autonomous(name="DepotSideMoveFoundationBLUE")

public class DepotSideMoveFoundationBLUE extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() {
                return opModeIsActive();
            }
        });
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Strafe Left
        Robot.setServos(FlipperPosition.UP, 0, "Lifting servos");
        Robot.strafeLeft(5, "Strafing left");


        // Step 1:  Drive forward for 3 seconds
        Robot.goForward(1.8, "Drive forward");

        // Step 2:  Servo grab
        Robot.stopMoving();
        Robot.setServos(FlipperPosition.DOWN, 1, "Grabbing the foundation");

        // Step 3:  Drive Backwards for 1 Second
        Robot.goBack(2.2, "Driving Backward");

        // Move forward
        Robot.goForward(0.2, "Driving forward");

        // Step 4:  Turn left to move the foundation
        Robot.turnLeft(2.2, "Turning left");

        // Step 4:  Strafe right for 1 Second
        Robot.setServos(FlipperPosition.UP, 0, "Lifting servos");
        Robot.strafeRight(1.2, "Strafing right");

        // Move out of foundation
        Robot.goBack(0.3, "Driving backwards");

        // Step 5: turn right
        Robot.turnRight(1.1, "Turning right");

        // Step 6: strafe right
        Robot.strafeRight(0.5, "Strafing right");

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}