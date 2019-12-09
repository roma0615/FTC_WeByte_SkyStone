package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
//12/8/19
    //REVERSE ALL OF THE MOVEMENTS AND REDUCE TIME BECAUSE ROBOT IS FASTER
@Autonomous(name="BuildingSiteMoveFoundationBLUE(CHANGED)")

public class BuildingSiteMoveFoundationBLUE extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() { return opModeIsActive(); }
        });
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way
        // Step 0: strafe left
        Robot.strafeLeft(0.35, "Strafe left");

        // Step 1:  Drive forward and then turn around to expose the back flippers
        Robot.goBack(4.25, "Drive back");
        Robot.setServos(FlipperPosition.UP, 0, "Lifting servos");

        // Step 2:  Servo grab
        Robot.stopMoving();
        Robot.setServos(FlipperPosition.DOWN, 1, "Grabbing the foundation");

        // Step 3:  Drive Forward for 1 Second
        Robot.goForward(1.1,"Driving forward");

        // Move back
        Robot.goBack(0.1, "Driving Backward");

        // Step 4:  Turn right to move the foundation
        Robot.turnRight(1.1, "Turning Right");

        // Step 4:  Strafe left for 1 Second
        Robot.setServos(FlipperPosition.UP, 0, "Lifting servos");
        Robot.strafeLeft(0.6, "Strafing right");

        // Move out of foundation
        Robot.goForward(0.15, "Driving forward");

        // Step 5: turn left
        Robot.turnLeft(0.55, "Turning left");

        // Step 6: strafe left
        Robot.strafeLeft(0.25, "Strafing right");

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}