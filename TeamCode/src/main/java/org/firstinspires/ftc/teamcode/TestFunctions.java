package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="TestAllFunctions")

public class TestFunctions extends LinearOpMode {
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
        // Step 1:  Turn Left
        Robot.setServos(FlipperPosition.SIDE, 1, "Servos should be in SIDE position");
        Robot.strafeRight(1.0, "Robot should be strafing right");

        Robot.setServos(FlipperPosition.DOWN, 1, "Servos should be in DOWN position");
        Robot.strafeLeft(1.0, "Robot should be strafing left");

        // Step 2:  Turn Right
        Robot.turnRight(1.0, "Robot should be turning right");

        // Step 3:  Drive Forwards for 1 Second
        Robot.goForward(1, "Robot should be moving forward");

        // Step 4:  Drive Backwards for 1 Second
        Robot.goBack(1.0, "Robot should be moving backward");

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}