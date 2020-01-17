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
import java.util.Locale;

// ASSUMES STRAFE WORKS
// 1/14/20 demo code idea for tensorflow combination
// 11. Reformat Code (CTRL+ALT+L / Command+Option+L):
// Front mounted claw and phone cominbantion with 3 distance Sensors

//TO DO
// Code in blocks the full auto using the IR sensor
// Replace all incremental movements with distance sensor movements
// Functionalize the code? Function for finding Skystone and for capturing it?
@Autonomous(name = "RED_SKYSTONE_3DISTANCEsensor")

public class RED_SKYSTONE_3DISTANCEsensor extends LinearOpMode {
    @Override
    public void runOpMode() {
        int inchPause = 500;
        double skystonePosition = 0;
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
        while (Robot.distanceSensor.getDistance(DistanceUnit.INCH) > 13) {
            Robot.strafeLeftContinous();
        }
        //Robot.checkDistanceSensors(0.05);
        /*
        while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) > 44){
            Robot.goForward();
        }
         */
        //Robot.goForward(0.1, "Going 4 inches forward");
        Robot.stopMoving();
        sleep(4000);


        //Step 2: Begin first read of blocks using Tensorflow
        //REWORK
        boolean FOUND = false;
        Robot.setForwardSpeed(0.4);
        if (TensorFlowDetection.skystonesFound() == 0) {
            int i = 0;
            for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                telemetry.addData("Object " + i, String.format(Locale.ENGLISH,
                        recognition.getLabel()));
                telemetry.addData("Object " + i,
                        (recognition.getRight()
                                + recognition.getLeft()) / 2);
            }
            telemetry.update();
            //Robot.checkDistanceSensors(0.05);
            /*
            while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) > 36){
                Robot.goForward();
            }
             */
            //Robot.goBack(0.3, "No Skystone");
        } else {
            FOUND = true;
            skystonePosition = 44;
        }
        if (FOUND == false) {
            sleep(4000);
        }
        if (TensorFlowDetection.skystonesFound() == 0 && FOUND == false) {
            /*
            //Robot.checkDistanceSensors(0.05);
            while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) > 28){
                Robot.goForward();
            }
            */
            //Robot.goBack(0.3, "No Skystone");
            //backCompensate += 0.3;
            FOUND = true;
            skystonePosition = 28;
        } else {
            FOUND = true;
            skystonePosition = 36;
        }
        telemetry.addData("Skystone", " seen!");
        Robot.stopMoving();
        boolean END = false;
        //while (!END) {
        if (TensorFlowDetection.skystonesFound() >= 1) {
            if ((TensorFlowDetection.getSkystone().getRight()
                    + TensorFlowDetection.getSkystone().getLeft()) / 2 < 500) {
                Robot.goForward(0.1, "Moving towards Skystone");
            }
            if ((TensorFlowDetection.getSkystone().getRight()
                    + TensorFlowDetection.getSkystone().getLeft()) / 2 > 700) {
                Robot.goBack(0.1, "Moving towards Skystone");
                int i = 0;
                for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                    telemetry.addData("Object " + i, String.format(Locale.ENGLISH,
                            recognition.getLabel()));
                    telemetry.addData("Object " + i,
                            (recognition.getRight()
                                    + recognition.getLeft()) / 2);
                }
                telemetry.update();

            }
            Robot.stopMoving();
            while (Robot.distanceSensor.getDistance(DistanceUnit.INCH) > 3) {
                Robot.strafeLeftContinous();
            }
            Robot.stopMoving();
            Robot.setClawServo(ClawPosition.DOWN, 2, "GRABBING");
            END = true;
            // This move doesn't need to be changed since the block sensor is blind
            Robot.strafeRight(0.5, "move");
                /*
                //Robot.checkDistanceSensors(0.05);
                while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) > 44){
                    Robot.goForward();
                }
                 */
            Robot.goForward(2, "move");
            Robot.setClawServo(ClawPosition.UP, 2, "release");
            Robot.goBack(2, "move");
            /*
            //Robot.checkDistanceSensors(0.05);
            while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) > 44){
                Robot.goForward();
            }
             */
        }

        /*
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

         */

    }
}
