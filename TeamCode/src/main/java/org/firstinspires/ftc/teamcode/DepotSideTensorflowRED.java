package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;

import java.util.List;

/**
 * The code is structured as a LinearOpMode
 * <p>
 * The desired path in this example is:
 * <p>
 * The code is written in a simple form with no optimizations.
 * However, there are several ways that this type of sequence could be streamlined,
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
//12/8/19 demo code idea for tensorflow combination
@Autonomous(name = "DepotSideTensorFlowRED")

public class DepotSideTensorflowRED extends LinearOpMode {
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
        TensorFlowDetection.init(hardwareMap, telemetry);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Strafe Right and move forward to get into position

        Robot.setServos(FlipperPosition.UP, 0, "Lifting servos");
        Robot.setClawServo(ClawPosition.UP, 0, "Getting claw ready");
        Robot.setForwardSpeed(0.5);
        Robot.goForward(0.1, "Getting into position to read blocks");
        Robot.strafeLeft(1, "Getting ready to read blocks");
        Robot.stopMoving();
        sleep(2000);
        //Step 2: Begin first read of blocks using Tensorflow (UNFINISHED, seems to crash with Index out of bounds error)
        boolean moving = false;
        boolean END = false;
        while (!END && opModeIsActive()) {
            if (TensorFlowDetection.getRecognitions() != null
                    && TensorFlowDetection.getRecognitions().size() >= 1) {
                moving = true;
            }
            // Update the recognitions if the moving is false.
            if (!moving) {
                TensorFlowDetection.updateRecognitions();
            } else {
                List<Recognition> rec = TensorFlowDetection.getRecognitions();
                telemetry.addData("Status", rec.get(0));
                telemetry.update();
                double itemWidth = rec.get(0).getWidth();
                if (itemWidth >= 600) {
                    END = true;
                } else {
                    Robot.setForwardSpeed(0.5);
                    Robot.strafeLeft(0.1, "Inching closer");
                    Robot.stopMoving();
                    sleep(500);
                }
            }
        }
        //Step 3: Check if approached stone is a Skystone. (UNFINISHED)
        moving = false;
        END = false;
        int forwardMoveTime = 0;
        while (!END && opModeIsActive()) {
            if (TensorFlowDetection.getRecognitions() != null
                    && TensorFlowDetection.getRecognitions().size() == 1) {
                moving = true;
            }
            // Update the recognitions if the moving is false.
            if (!moving) {
                TensorFlowDetection.updateRecognitions();
            } else {
                List<Recognition> rec = TensorFlowDetection.getRecognitions();

                if (rec.get(0).getLabel().equals("Skystone")) {
                    moving = false;
                    END = true;
                } else {
                    Robot.goBack(0.5, "Moving to next Stone");
                    forwardMoveTime += 0.5;
                    moving = false;
                }
            }
        }
        //Step 4: Claw operation
        Robot.setForwardSpeed(0.5);
        Robot.strafeLeft(0.5, "MOVING TO THE SKYSTONE");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone!");

        //Step 5: Move to midline and release Claw
        Robot.strafeRight(0.6, "Moving to midline");
        Robot.goForward(5 + forwardMoveTime, "Moving to midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 6: Go back to new Skystone
        Robot.strafeLeft(0.6, "Moving to other block");
        Robot.goBack(5 + forwardMoveTime - 2, "Moving to other block");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone");

        //Step 7: Go to midline again and release Claw
        Robot.strafeRight(0.6, "Moving to midline");
        Robot.goForward(3 + forwardMoveTime, "Moving to midline");

        //Step 8: Line up with Midline
        Robot.goBack(0.5,"Going to midline");

        Robot.stopMoving();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
        TensorFlowDetection.shutdown();
    }
}

