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
@Autonomous(name="Foundation_Wall_Blue")

public class Foundation_Wall_Blue extends LinearOpMode {
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
        // Step 0: strafe right
        Robot.setServos(FlipperPosition.UP, 0, "Lifting servos");
        Robot.strafeRight(0.4, "Strafe right");


        // Step 1:  Drive forward and then turn around to expose the back flippers
        Robot.goBack(0.4, "Drive back");
        Robot.setForwardSpeed(0.5);
        Robot.goBack(0.7, "Drive back");
        Robot.setForwardSpeed(1.0);


        // Step 2:  Servo grab
        Robot.stopMoving();
        Robot.setServos(FlipperPosition.DOWN, 1, "Grabbing the foundation");

        // Step 3:  Drive Forward for 1 Second
        //Robot.turnRight(0.1, "Turn and get ready");
        Robot.goForward(1.2, "Driving forward");

        // Move back
        Robot.goBack(0.1, "Driving Backward");

        // Step 4:  Turn right to move the foundation
        Robot.turnLeft(1.5, "Turning Left");

        // Step 4:  Strafe left for 1 Second
        Robot.setServos(FlipperPosition.UP, 0.7, "Lifting servos");
        Robot.strafeLeft(0.8, "Strafing right");

        // Move out of foundation
        Robot.goForward(0.15, "Driving forward");
        /*
        // Step 5: turn left
        Robot.turnLeft(0.85, "Turning left");

        // Step 6: strafe left
        Robot.strafeRight(0.7, "Strafing right");

        Robot.turnRight(0.4,"Turning right");

        Robot.strafeRight(0.4, "heading to midline!");
        */
        Robot.turnRight(1.72, "Turning left");
        Robot.goBack(0.5, "Moving Back");
        //Robot.turnRight(0.1, "Turning left");
        //Robot.goBack(0.1, "Heading to Midline");
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}