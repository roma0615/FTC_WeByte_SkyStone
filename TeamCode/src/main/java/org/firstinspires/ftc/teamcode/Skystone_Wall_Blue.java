package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;

// ASSUMES STRAFE WORKS
//12/10/19 demo code idea for tensorflow combination
// Strafing has a HUGE drift, so there had to be adjustments made.
// 11. Reformat Code (CTRL+ALT+L / Command+Option+L):
@Autonomous(name = "Skystone_Wall_Blue")

public class Skystone_Wall_Blue extends LinearOpMode {
    @Override
    public void runOpMode() {
        int inchPause = 1000;
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
        sleep(2500);


        //Step 2: Begin first read of blocks using Tensorflow
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
                int i = 0;
                for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());

                    telemetry.addData(String.format("   height, width (%d)", i), "%.03f , %.03f",

                            recognition.getHeight(), recognition.getWidth());
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
                    moving = false;
                } else if (TensorFlowDetection.getRecognitions().size() > 1) {
                    //sleep(2000);
                    Robot.goForward(0.02, "Inching away from crack");
                    sleep(2 * inchPause);
                    TensorFlowDetection.updateRecognitions();
                    moving = false;
                } else {
                    double itemWidth = TensorFlowDetection.getRecognitions().get(0).getWidth();

                    if (itemWidth >= 1050) {
                    } else {
                        Robot.setForwardSpeed(0.5);
                        Robot.strafeLeft(0.1, "inching closer");
                        Robot.stopMoving();
                        sleep(inchPause);
                        TensorFlowDetection.updateRecognitions();
                        moving = false;
                    }
                }
            }
        }
        //Step 3: Check if approached stone is a Skystone. (UNFINISHED)
        moving = false;
        END = false;
        int forwardMoveTime = 0;
        //List<Recognition> rec = TensorFlowDetection.getRecognitions();
        while (!END && opModeIsActive()) {
            if (TensorFlowDetection.getRecognitions() != null
                    && TensorFlowDetection.getRecognitions().size() == 1) {
                moving = true;
            } else if (TensorFlowDetection.getRecognitions() != null && TensorFlowDetection.getRecognitions().size() > 1) {

                Robot.goBack(0.1, "Going back");
                forwardMoveTime -= 0.1;
                moving = false;
                sleep(2000);
                TensorFlowDetection.updateRecognitions();
            }
            // Update the recognitions if the moving is false.
            if (!moving) {
                TensorFlowDetection.updateRecognitions();
            } else {

                if (TensorFlowDetection.getRecognitions().get(0).getLabel().equals("Skystone")) {
                    moving = false;
                    END = true;
                } else {
                    //sleep(1000);
                    Robot.goForward(0.18, "Moving to next Stone");
                    forwardMoveTime += 0.18;
                    moving = false;
                    sleep(2000);
                    TensorFlowDetection.updateRecognitions();
                }
            }
        }
        //Step 4: Claw operation
        Robot.setForwardSpeed(0.5);
        //Robot.goForward(0.01,"MOVING TO THE SKYSTONE");
        Robot.strafeLeft(0.35, "MOVING TO THE SKYSTONE");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone!");

        //Step 5: Move to midline and release Claw
        Robot.strafeRight(0.6, "Moving to midline");
        Robot.goBack(2 + forwardMoveTime, "Moving to midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 6: Go back to new Skystone
        // on the way back, plow through the stones, turn 180 degrees, and get to the other block
        Robot.goForward(2.85, "Moving to other block");
        Robot.strafeLeft(1.05, "Plowing through blocks"); // ADJUST THIS VALUE
        Robot.turnRight(1, "Turning 180 degrees"); // ADJUST THIS VALUE
        Robot.goBack(forwardMoveTime, "Moving to other block");
        Robot.strafeLeft(0.7, "Moving to other block");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone");


        //Step 7: Go to midline again and release Claw
        Robot.strafeRight(0.67, "Moving to midline");
        Robot.turnLeft(0.07, "Adjusting for drift");
        Robot.goForward(forwardMoveTime, "Moving to midline");
        Robot.strafeLeft(1.05, "Plowing through blocks"); // ADJUST THIS VALUE (make it the same as above)
        Robot.goForward(2.85, "Moving to midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 8: Line up with Midline
        Robot.strafeLeft(0.7, "Getting close to wall");
        Robot.goBack(0.7, "Going to midline");

        Robot.stopMoving();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
        TensorFlowDetection.shutdown();
    }
}