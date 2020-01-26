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


import java.util.Locale;

// ASSUMES STRAFE WORKS
// 1/14/20 demo code idea for tensorflow combination
// 11. Reformat Code (CTRL+ALT+L / Command+Option+L):
// Front mounted claw and phone cominbantion with 3 distance Sensors

//TO DO
// Code in blocks the full auto using the IR sensor
// Replace all incremental movements with distance sensor movements
// Functionalize the code? Function for finding Skystone and for capturing it?
@Autonomous(name = "BLUE_SKYSTONE_4DISTANCEsensor")

public class BLUE_SKYSTONE_4DISTANCEsensor extends LinearOpMode {
    @Override
    public void runOpMode() {
        int inchPause = 500;
        double skystonePosition = 0;
        double distanceVar = 24;
        double checkDistanceVar = 0.01;
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
        while (Robot.rightSensor.getDistance(DistanceUnit.INCH) > 12 && opModeIsActive()) {
            Robot.strafeRightContinuous();
        }
        Robot.stopMoving();
        //Robot.turnLeft(0.1, "turn");
        sleep(250);
        Robot.setForwardSpeed(0.4);
        while (Robot.rearLeftSensor.getDistance(DistanceUnit.INCH) < (distanceVar + 8) && opModeIsActive()) {
            Robot.goForwardContinuous();
        }
        //Robot.turnLeft(0.02,"adjust for drift");

        //Robot.goForward(0.1, "Going 4 inches forward");
        Robot.stopMoving();
        sleep(3000);


        //Step 2: Begin first read of blocks using Tensorflow
        //REWORK
        boolean FOUND = false;
        Robot.setForwardSpeed(0.4);
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            //Robot.checkDistanceSensors(checkDistanceVar);
            skystonePosition = distanceVar + 7;
            int i = 0;
            for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                telemetry.addData("Object " + i, String.format(Locale.ENGLISH,
                        recognition.getLabel()));
                telemetry.addData("Object " + i,
                        (recognition.getRight()
                                + recognition.getLeft()) / 2);
            }
            telemetry.update();


        } else {
            int i = 0;
            for (Recognition recognition : TensorFlowDetection.getRecognitions()) {
                telemetry.addData("Object " + i, String.format(Locale.ENGLISH,
                        recognition.getLabel()));
                telemetry.addData("Object " + i,
                        (recognition.getRight()
                                + recognition.getLeft()) / 2);
            }
            telemetry.update();
            Robot.setForwardSpeed(0.2);
            while(Robot.rearLeftSensor.getDistance(DistanceUnit.INCH) > (distanceVar + 6.5) && opModeIsActive()){
                Robot.goBackContinuous();
            }
            Robot.stopMoving();
            Robot.strafeRight(0.15,"adjusting");
            //Robot.turnLeft(0.01, "adjusting");

        }

        if (FOUND == false) {
            sleep(3000);
        }
        if (TensorFlowDetection.skystonesFound() >= 1 && FOUND == false &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 300 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            skystonePosition = distanceVar + 5.4;

        } else if(FOUND == false) {
            Robot.setForwardSpeed(0.2);
            while(Robot.rearLeftSensor.getDistance(DistanceUnit.INCH) > (distanceVar) && opModeIsActive()){
                Robot.goBackContinuous();
            }
            Robot.stopMoving();
            //Robot.goBack(0.3, "No Skystone");
            //backCompensate += 0.3;
            FOUND = true;
            skystonePosition = (distanceVar-2.5);
            //Robot.checkDistanceSensors(checkDistanceVar);
        }
        telemetry.addData("Skystone", " seen!");
        Robot.stopMoving();
        boolean END = false;
        //while (!END) {
            /*
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
            */
        Robot.setForwardSpeed(0.4);
        while(Robot.rearLeftSensor.getDistance(DistanceUnit.INCH) < skystonePosition && opModeIsActive()){
            Robot.goForwardContinuous();
        }
        Robot.stopMoving();
        while (Robot.rightSensor.getDistance(DistanceUnit.INCH) > 1 && opModeIsActive()) {
            Robot.strafeRightContinuous();
        }
        Robot.stopMoving();
        Robot.setForwardSpeed(0.12);
        Robot.checkDistanceSensors(checkDistanceVar);
        while(Robot.rearLeftSensor.getDistance(DistanceUnit.INCH) < (skystonePosition+0.5) && opModeIsActive()){
            Robot.goForwardContinuous();
        }
        Robot.stopMoving();

        Robot.setClawServo(ClawPosition.DOWN, 1.5, "GRABBING");
        END = true;
        // This move doesn't need to be changed since the block sensor is blind
        // speed 0.4 strafeLeft 0.65 goBack 2 goForward 2 worked
        Robot.setForwardSpeed(1);
        Robot.strafeRight(0.35, "move");


        while(Robot.rearLeftSensor.getDistance(DistanceUnit.INCH) < 40 && opModeIsActive()){
            Robot.goBackContinuous();
        }
        Robot.goForward(1, "move");
        Robot.setClawServo(ClawPosition.UP, 0.1, "release");
        Robot.goBack(0.75, "move");
        Robot.setForwardSpeed(0.2);
        /*
        if(skystonePosition == (distanceVar +5.4)){
            skystonePosition -= 0.1;
        }
        while(Robot.frontRightSensor.getDistance(DistanceUnit.INCH) > (skystonePosition - 17.8) && opModeIsActive()){
            Robot.goForwardContinuous();
        }
        Robot.stopMoving();
        //Robot.checkDistanceSensors(checkDistanceVar);
        //Robot.turnLeft(0.02, "adjust");
        Robot.setForwardSpeed(0.5);
        while (Robot.rightSensor.getDistance(DistanceUnit.INCH) > 1 && opModeIsActive()) {
            Robot.strafeRightContinuous();
        }
        Robot.stopMoving();

        Robot.setForwardSpeed(0.12);
        Robot.checkDistanceSensors(checkDistanceVar);
        while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) < (skystonePosition-17.25) && opModeIsActive()){
            Robot.goBackContinuous();
        }
        Robot.stopMoving();

        Robot.setClawServo(ClawPosition.DOWN, 1.5, "grab");
        Robot.setForwardSpeed(0.4);
        Robot.strafeLeft(0.7,"");

        while(Robot.frontLeftSensor.getDistance(DistanceUnit.INCH) > (distanceVar + 22) && opModeIsActive()){
            Robot.goBackContinuous();
        }
        // speed 0.4 back 3 forward 0.6 works
        // speed 0.7 back 2.7 forward 0.7 works
        Robot.setForwardSpeed(1);
        Robot.stopMoving();
        Robot.goBack(2.3,"going to midfield");
        Robot.setClawServo(ClawPosition.UP, 0.1, "grab");
        Robot.goForward(0.65,"going to midfield line");

         */

    }
    //TensorFlowDetection.shutdown();
}

