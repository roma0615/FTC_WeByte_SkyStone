package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.*;
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

public class DepotSideTensorflowRED extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() { return opModeIsActive(); }
        });
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        TensorFlowDetection.init(hardwareMap,telemetry);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        while(opModeIsActive()) {
            // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

            // Step 1:  Strafe Right and move forward to get into position
            Robot.setServos(FlipperPosition.SIDE, 0, "Lifting servos");
            Robot.strafeLeft(3,"Getting ready to read blocks");
            Robot.goForward(0.5,"Getting into position to read blocks");

            //Step 2: Begin first read of blocks using Tensorflow (UNFINISHED)
            boolean moving = false;
            boolean END = false;
            while (END == false) {
                if (TensorFlowDetection.getRecognitions() != null
                        && TensorFlowDetection.getRecognitions().size() == 1) {
                    moving = true;
                }
                // Update the recognitions if the moving is false.
                if (!moving) {
                    TensorFlowDetection.updateRecognitions();
                } else {
                    List<Recognition> rec = TensorFlowDetection.getRecognitions();

                    double itemWidth = rec.get(0).getWidth();
                    if(itemWidth >= 600){
                        END = true;
                    } else {
                        Robot.strafeLeft(0.2,"Inching closer");
                    }
                }
            }
            //Step 3: Check if approached stone is a Skystone. (UNFINISHED)
            moving = false;
            END = false;
            int forwardMoveTime = 0;
            while (END == false) {
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
                        Robot.goForward(0.5,"Moving to next Stone");
                        forwardMoveTime += 0.5;
                        moving = false;
                    }
                }
            }
            //Step 4: Claw operation
            //CLAW GRAB

            //Step 5: Move to midline and release Claw
            Robot.strafeRight(0.6, "Moving to midline");
            Robot.goForward(5 + forwardMoveTime, "Moving to midline");
            // RELEASE CLAW AND RESET IT

            //Step 6: Go back to original position
            Robot.strafeLeft(0.6, "Moving back to blocks");
            Robot.goBack(5 + forwardMoveTime, "Moving back to blocks");

            //Step 7: Find Skystone based on position of last Skystone
            Robot.goForward(2, "Moving to other Skystone");
            // CLAW GRAB


            //Step 8: Go to midline again and release Claw
            Robot.strafeRight(0.6, "Moving to midline");
            Robot.goForward(3 + forwardMoveTime, "Moving to midline");

            //Step 9: Line up with Midline
            Robot.goBack(0.5,"Going to midline");

            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);
        }
    }
}

