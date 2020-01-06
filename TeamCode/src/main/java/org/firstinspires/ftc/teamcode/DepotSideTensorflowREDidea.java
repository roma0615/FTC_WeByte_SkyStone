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

/*
THINGS TO DO:

MAKE SURE THE backwardMoveTime is consistent
Shave off time by setting forward speed to 1 when executing the routine for the 2nd Skystone
Make sure the "servoing in" on the block's center works properly.
 */




// ASSUMES STRAFE WORKS
// 1/3/2020 An idea to massively simplify the Tensorflow code. Assuming the center line up works,
// this would be much more consistent than the old code.
// Strafing has a HUGE drift, so there had to be adjustments made.
// 11. Reformat Code (CTRL+ALT+L / Command+Option+L):
@Autonomous(name = "DepotSideTensorFlowREDidea")

public class DepotSideTensorflowREDidea extends LinearOpMode {
    @Override
    public void runOpMode() {
        int inchPause = 500;
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

        Robot.goForward(0.35, "Getting into position to read blocks");
        Robot.strafeLeft(1.06, "Getting ready to read blocks");
        Robot.stopMoving();
        sleep(1500);


        //Step 2: Begin first read of blocks using Tensorflow
        boolean identified = false;
        boolean END = false;

        while (!END && opModeIsActive()) {
            if (TensorFlowDetection.getRecognitions() != null
                    && TensorFlowDetection.getRecognitions().size() >= 1) {
                identified = true;
            }
            // Update the recognitions if the moving is false.
            if (!identified) {
                TensorFlowDetection.updateRecognitions();
            } else {
                int i = 0;
                for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());

                    telemetry.addData(String.format("   height, width (%d)", i), "%.03f , %.03f",

                            recognition.getHeight(), recognition.getWidth());
                    i++;
                }
                telemetry.addData("Status", TensorFlowDetection.getRecognitions().get(0));
                telemetry.update();

                if (TensorFlowDetection.getRecognitions() == null) {
                    Robot.setForwardSpeed(0.5);
                    Robot.strafeLeft(0.1, "inching closer");
                    Robot.stopMoving();
                    Robot.goForward(0.02, "Inching away from crack");
                    sleep(inchPause);
                    TensorFlowDetection.updateRecognitions();
                    identified = false;
                } else if (TensorFlowDetection.getRecognitions().size() > 1) {
                    //sleep(2000);
                    Robot.goForward(0.02, "Inching away from crack");
                    sleep(2 * inchPause);
                    TensorFlowDetection.updateRecognitions();
                    identified = false;
                } else {
                    double itemWidth = TensorFlowDetection.getRecognitions().get(0).getWidth();
                    //ideal distance is 18 inches
                    if (itemWidth >= 1050) {
                    } else {
                        Robot.setForwardSpeed(0.5);
                        Robot.strafeLeft(0.1, "inching closer");
                        Robot.stopMoving();
                        sleep(inchPause);
                        TensorFlowDetection.updateRecognitions();
                        identified = false;
                    }
                }
            }
        }
        //Step 3: Check if approached stone is a Skystone. (UNFINISHED)
        // identified = false;
        END = false;
        int backwardMoveTime = 0;
        while (!END && opModeIsActive()) {
            if(TensorFlowDetection.getRecognitions().get(0).getLabel().equals("Skystone")){
                sleep(inchPause * 3);
                if(TensorFlowDetection.getRecognitions().get(0).getLabel().equals("Skystone")) {
                    END = true;
                }
            } else {
                Robot.setForwardSpeed(0.25);
                Robot.goForward(0.01, "Going through the blocks");
                backwardMoveTime += 0.0025;
            }
        }
        /*
        //List<Recognition> rec = TensorFlowDetection.getRecognitions();
        while (!END && opModeIsActive()) {
            if (TensorFlowDetection.getRecognitions() != null
                    && TensorFlowDetection.getRecognitions().size() == 1) {
                identified = true;
            } else if (TensorFlowDetection.getRecognitions() != null && TensorFlowDetection.getRecognitions().size() > 1) {
                if(TensorFlowDetection.getRecognitions().size() == 2){
                    if(TensorFlowDetection.getRecognitions().get(0).getLabel().equals("Skystone")
                            && TensorFlowDetection.getRecognitions().get(1).getLabel().equals("Stone")){
                        Robot.goForward(0.1, "Going back");
                        backwardMoveTime += 0.1;
                        identified = false;
                        sleep(inchPause*2);
                        TensorFlowDetection.updateRecognitions();
                    }
                }
                Robot.goBack(0.1, "Going back");
                backwardMoveTime -= 0.1;
                identified = false;
                sleep(inchPause*2);
                TensorFlowDetection.updateRecognitions();
            }
            // Update the recognitions if the moving is false.
            if (!identified) {
                TensorFlowDetection.updateRecognitions();
            } else {

                if (TensorFlowDetection.getRecognitions().get(0).getLabel().equals("Skystone")) {
                    identified = false;
                    END = true;
                } else {
                    //sleep(1000);
                    Robot.goForward(0.18, "Moving to next Stone");
                    backwardMoveTime += 0.18;
                    identified = false;
                    sleep(inchPause*2);
                    TensorFlowDetection.updateRecognitions();
                }
            }
        }

         */
        //Step 4: Claw operation
        Robot.setForwardSpeed(0.5);
        //Robot.goForward(0.01,"MOVING TO THE SKYSTONE");
        double skyWidth = TensorFlowDetection.getRecognitions().get(0).getWidth();
        double skyLeftEdge = TensorFlowDetection.getRecognitions().get(0).getLeft();
        double skystoneCenter = ((skyWidth + skyLeftEdge) / 2);

        Robot.strafeLeft(0.35, "MOVING TO THE SKYSTONE");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone!");

        //Step 5: Move to midline and release Claw
        Robot.strafeRight(0.6, "Moving to midline");
        Robot.goBack(2 + backwardMoveTime, "Moving to midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 6: Go back to new Skystone
        // on the way back, plow through the stones, turn 180 degrees, and get to the other block
        Robot.goForward(2.85, "Moving to other block");
        Robot.strafeLeft(1.05, "Plowing through blocks"); // ADJUST THIS VALUE
        Robot.turnRight(1, "Turning 180 degrees"); // ADJUST THIS VALUE
        Robot.goBack(backwardMoveTime, "Moving to other block");
        Robot.strafeLeft(0.7, "Moving to other block");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone");


        //Step 7: Go to midline again and release Claw
        Robot.strafeRight(0.67, "Moving to midline");
        Robot.turnLeft(0.07, "Adjusting for drift");
        Robot.goForward(backwardMoveTime, "Moving to midline");
        Robot.strafeLeft(1.05, "Plowing through blocks"); // ADJUST THIS VALUE (make it the same as above)
        Robot.goForward(2.85, "Moving to midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 8: Line up with Midline
        Robot.goBack(0.7, "Going to midline");

        Robot.stopMoving();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
        TensorFlowDetection.shutdown();
    }
}