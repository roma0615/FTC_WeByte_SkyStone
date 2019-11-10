package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.ServoPosition;

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

@Autonomous(name="DepotSideMoveFoundationRED")

public class DepotSideMoveFoundationRED extends LinearOpMode {
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

        // Step 1:  Strafe Right
        Robot.setServos(ServoPosition.SIDE, 0, "Lifting servos");
        Robot.strafeRight(5, "Strafing right");

        // Step 1:  Drive forward for 3 seconds
        Robot.goForward(1.8, "Drive forward");

        // Step 2:  Servo grab
        Robot.stopMoving();
        Robot.setServos(ServoPosition.DOWN, 1, "Grabbing the foundation");

        // Step 3:  Drive Backwards for 1 Second
        Robot.goBack(2.2, "Driving backward");

        // Move forward
        Robot.goForward(0.2, "Driving forward");

        // Step 4:  Turn right to move the foundation
        Robot.turnRight(2.2, "Turning right");

        // Step 4:  Strafe left for 1 Second
        Robot.setServos(ServoPosition.SIDE, 0, "Lifting servos");
        Robot.strafeLeft(1.2, "Strafing left");

        // Move out of foundation
        Robot.goBack(0.3, "Driving backward");

        // Step 5: turn left
        Robot.turnLeft(1.1, "Turning left");

        // Step 6: strafe left
        Robot.strafeLeft(0.5, "Strafing left");

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}

