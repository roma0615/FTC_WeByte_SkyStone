package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;

//Test of the move functions used in robot code

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