package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;

import java.util.List;

//12/10/19 demo code idea for tensorflow combination
// Strafing has a HUGE drift, so there had to be adjustments made. Must be adjusted.
@Autonomous(name = "DepotSideTensorFlowRED2")
@Disabled
public class DepotSideTensorflowRED2 extends LinearOpMode {
    @Override
    public void runOpMode() {
        long intialTensorflowPauseCheck = 3000;
        int inchPause = 1250;
        double intialStrafeAdjust = 0.2;
        double inchStrafeAdjust = 0.1;
        double ninetyDegreeTurn = 0.8;
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
        Robot.setForwardSpeed(0.3);
        Robot.goForward(0.55, "Moving to position");
        Robot.turnLeft(ninetyDegreeTurn+0.05, "Turn 90 degrees left");
        Robot.goForward(0.95, "Get vertical distance for blocks");
        Robot.turnRight(ninetyDegreeTurn-0.05, "Turn 90 degrees right");
        Robot.stopMoving();
        sleep(intialTensorflowPauseCheck);
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
                int i = 0;
                for (Recognition recognition : rec) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("   height, width (%d)",i), "%.03f , %.03f",
                            recognition.getHeight(), recognition.getWidth());
                }
                telemetry.addData("Status", rec.get(0));
                telemetry.update();
                double itemWidth = rec.get(0).getWidth();
                if (itemWidth >= 600) {
                    END = true;
                } else {
                    Robot.setForwardSpeed(0.5);
                    Robot.strafeLeft(0.1, "inching closer");
                    Robot.stopMoving();
                    sleep(inchPause);
                }
            }
        }
        //Step 3: Check if approached stone is a Skystone. (UNFINISHED)
        moving = false;
        END = false;
        int forwardMoveTime = 0;
        List<Recognition> rec = TensorFlowDetection.getRecognitions();
        while (!END && opModeIsActive()) {
            if (rec != null
                    && rec.size() == 1) {
                moving = true;
            }
            else if(rec != null && rec.size() > 1){
                Robot.goForward(0.2, "Moving to next Stone");
                forwardMoveTime -= 0.2;
                moving = false;
                sleep(2000);
                TensorFlowDetection.updateRecognitions();
            }
            // Update the recognitions if the moving is false.
            if (!moving) {
                TensorFlowDetection.updateRecognitions();
            } else {

                if (rec.get(0).getLabel().equals("Skystone")) {
                    moving = false;
                    END = true;
                } else {
                    Robot.turnRight(0.05,"adjusting to drift");
                    Robot.goBack(0.4, "Moving to next Stone");
                    forwardMoveTime += 0.4;

                    moving = false;
                    sleep(2000);
                    TensorFlowDetection.updateRecognitions();
                }
            }
        }
        //Step 4: Claw operation
        Robot.strafeLeft(0.45, "Reaching for the Skystone!");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone!");
        Robot.strafeRight(0.45, "Moving out");

        //Step 5: Move to midline and release Claw
        Robot.turnRight(ninetyDegreeTurn-0.05, "Turn 90 degrees right");
        Robot.goForward(0.6, "Get vertical distance for blocks");
        Robot.turnLeft(ninetyDegreeTurn, "Turn 90 degrees left");
        Robot.goForward(2 + forwardMoveTime, "Moving to midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 6: Go back to new Skystone
        Robot.turnLeft(ninetyDegreeTurn, "Turn 90 degrees left");
        Robot.goForward(0.6, "Get vertical distance for blocks");
        Robot.turnRight(ninetyDegreeTurn-0.05, "Turn 90 degrees right");
        Robot.goBack(3.5 + forwardMoveTime, "Moving to other block");
        Robot.strafeLeft(0.45, "Reaching for the Skystone!");
        Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone");
        Robot.strafeRight(0.45, "Reaching for the Skystone!");

        //Step 7: Go to midline again and release Claw
        Robot.turnRight(ninetyDegreeTurn, "Turn 90 degrees right");
        Robot.goForward(0.6, "Get vertical distance for blocks");
        Robot.turnLeft(ninetyDegreeTurn, "Turn 90 degrees left");
        Robot.goForward(3 + forwardMoveTime, "Moving past midline");
        Robot.setClawServo(ClawPosition.UP, 1, "Grabbing Skystone");

        //Step 8: Line up with Midline
        Robot.goBack(0.5,"Going to midline");

        Robot.stopMoving();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        TensorFlowDetection.shutdown();
    }
}
