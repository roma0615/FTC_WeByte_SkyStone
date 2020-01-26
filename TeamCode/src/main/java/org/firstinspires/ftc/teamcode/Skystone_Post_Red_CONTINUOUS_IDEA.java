package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.ClawPosition;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;


import java.util.List;

// ASSUMES STRAFE WORKS
//12/10/19 demo code idea for tensorflow combination
// Strafing has a HUGE drift, so there had to be adjustments made.
// 11. Reformat Code (CTRL+ALT+L / Command+Option+L):
// Assumes front mounted camera with feeder working

//TO DO
// Maybe look for the Skystone in its entirety instead of just checking the first entry?
@Autonomous(name = "Skystone_Post_Red_CONTINUOUS_IDEA")

public class Skystone_Post_Red_CONTINUOUS_IDEA extends LinearOpMode {
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

        Robot.goForward(1.02, "Getting into position to read blocks");
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
                    Robot.strafeLeft(0.05, "Trying to find stone");
                    Robot.goForward(0.05, "Trying to find stone");
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
                    //ideal distance is 9-10 inches. USE DISTANCE SENSOR HERE
                    // if (Robot.rightSensor.getDistance(DistanceUnit.INCH) < 10.5)
                    if (TensorFlowDetection.getRecognitions().get(0).getWidth() > 550) {
                    } else {
                        Robot.setForwardSpeed(0.5);
                        Robot.goForward(0.1, "inching closer");
                        Robot.stopMoving();
                        sleep(inchPause);
                        TensorFlowDetection.updateRecognitions();
                        identified = false;
                    }
                }
            }
        }
        //Step 3: Check if approached stone is a Skystone. (UNFINISHED)

        identified = false;
        END = false;
        int backwardMoveTime = 0;
        while(!identified){
            if(TensorFlowDetection.getRecognitions().get(0).getLabel().equals("Skystone")) {
                identified = true;
                Robot.setForwardSpeed(0.5);
            } else {
                Robot.setForwardSpeed(0.2);
                Robot.strafeLeft(0.02, "Skystone not found yet...");
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
                        Robot.strafeLeft(0.1, "Going left to the Skystone");
                        backwardMoveTime += 0.1;
                        identified = false;
                        sleep(inchPause*2);
                        TensorFlowDetection.updateRecognitions();
                    } else{
                        Robot.strafeRight(0.1, "Going right to the Skystone");
                        backwardMoveTime -= 0.1;
                        identified = false;
                        sleep(inchPause * 2);
                        TensorFlowDetection.updateRecognitions();
                    }
                } else {
                    Robot.strafeRight(0.1, "Going right to the Skystne");
                    backwardMoveTime -= 0.1;
                    identified = false;
                    sleep(inchPause * 2);
                    TensorFlowDetection.updateRecognitions();
                }
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
                    Robot.strafeLeft(0.18, "Moving to next Stone");
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
        while((1280/2) > skystoneCenter){
            Robot.setForwardSpeed(0.1);
            Robot.strafeLeft(0.01, "positioning");
            backwardMoveTime += 0.002;
            sleep(200);
            skyWidth = TensorFlowDetection.getRecognitions().get(0).getWidth();
            skyLeftEdge = TensorFlowDetection.getRecognitions().get(0).getLeft();
            skystoneCenter = ((skyWidth + skyLeftEdge) / 2);
        }
        while((1280/2) < skystoneCenter){
            Robot.setForwardSpeed(0.1);
            Robot.strafeRight(0.01, "positioning");
            backwardMoveTime -= 0.002;
            skyWidth = TensorFlowDetection.getRecognitions().get(0).getWidth();
            skyLeftEdge = TensorFlowDetection.getRecognitions().get(0).getLeft();
            skystoneCenter = ((skyWidth + skyLeftEdge) / 2);
        }
        Robot.setForwardSpeed(0.5);
        Robot.goForward(0.35, "MOVING TO THE SKYSTONE");
        Robot.activateIntake(0.5,1,"Eating the SKYSTONE!!!");
        //Robot.setClawServo(ClawPosition.DOWN, 1, "Grabbing Skystone!");

        //Step 5: Move to midline and release Claw
        Robot.goBack(0.6, "Moving to midline");
        Robot.strafeRight(2 + backwardMoveTime, "Moving to midline");
        //Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");
        //MUST RELEASE BLOCK IN SOME FASHION

        //Step 6: Go back to new Skystone
        Robot.strafeLeft(4+backwardMoveTime, "Moving to other SKYSTONE");
        Robot.goForward(0.6, "MOVING TO THE OTHER SKYSTONE");
        Robot.activateIntake(0.5, 1, "EATING THE OTHER SKYSTONE!!!");

        //Step 7: Go to midline again and release Claw
        Robot.strafeRight(4+backwardMoveTime, "Moving to midline");
        //RELEASE BLOCK
        //Robot.setClawServo(ClawPosition.UP, 1, "Releasing Skystone");

        //Step 8: Line up with Midline
        Robot.strafeLeft(1, "Going to midline");

        Robot.stopMoving();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
        TensorFlowDetection.shutdown();
    }
}

