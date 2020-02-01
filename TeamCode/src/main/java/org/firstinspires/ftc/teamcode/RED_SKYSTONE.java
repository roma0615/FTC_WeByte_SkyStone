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
@Autonomous(name = "RED_SKYSTONE")


public class RED_SKYSTONE extends LinearOpMode {
    @Override
    public void runOpMode() {
        int inchPause = 500;
        double skystonePosition1 = 0;
        double skystonePosition2 = 0;
        double checkDistanceVar = 0.01;
        double additionalDistance = 0;
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

        Robot.setServos(FlipperPosition.BOTTOM, 0, "Lifting servos");
        Robot.setClawServo(ClawPosition.MEASURING, 0, "Getting claw ready");
        Robot.setForwardSpeed(0.5);
        Robot.strafeRight(1.32, "");
        Robot.centerRobot(30.8);
        //Robot.checkDistanceSensors(0.01);
        sleep(1500);
        //Robot.massTelemetryDump(15);

        //Step 2: Begin first read of blocks using Tensorflow
        boolean FOUND = false;
        /*
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
         */
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                TensorFlowDetection.detectCenterSkystone()) {
            FOUND = true;
            skystonePosition1 = 32.0;
            additionalDistance = 0.3;
            //Robot.massTelemetryDump(30);
        } else {
            Robot.goBack(0.21, "");
            //Robot.massTelemetryDump(15);
        }

        if (FOUND == false) {
            sleep(1500);
        }
        /*
        if (TensorFlowDetection.skystonesFound() >= 1 && FOUND == false &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
         */
        if (FOUND == false && TensorFlowDetection.skystonesFound() >= 1 &&
                TensorFlowDetection.detectCenterSkystone()) {
            FOUND = true;
            skystonePosition1 = 39.75;
            additionalDistance = 0.0;

        } else if (FOUND == false) {
            Robot.goForward(0.4, "");
            FOUND = true;
            skystonePosition1 = 24.0;
            additionalDistance = 0.7;
            //Robot.massTelemetryDump(15);
        }

        Robot.strafeRight(0.35, "");
        //Robot.checkDistanceSensors(0.01);
        Robot.centerRobot(skystonePosition1);
        Robot.moveIn();
        //Robot.centerRobot(skystonePosition1);
        Robot.strafeRight(0.05, "");

        Robot.turnLeft(0.005, "");
        //Robot.centerRobot(skystonePosition1);
        Robot.setClawServo(ClawPosition.DOWN, 0.5, "");
        Robot.goForward(0.04, "");
        Robot.goBack(0.08, "");


        Robot.strafeLeft(0.7, "");
        if (skystonePosition1 < 35) {
            Robot.checkDistanceSensors(0.01);
        }

        //Robot.turnRight(0.01,"");
        Robot.BackwardStraight(20,1.3 + additionalDistance);
        Robot.setClawServo(ClawPosition.UP,0.5,"");

        Robot.goForward(0.7, "");
        //Robot.turnRight(0.01,"");

     if (skystonePosition1 > 30) {
         Robot.ForwardStraight(20, skystonePosition1 - 10);
         Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

         Robot.checkDistanceSensors(0.01);
         Robot.strafeRight(0.3, "");
         Robot.centerRobot(skystonePosition1 - 24);
         Robot.moveOver();
         Robot.moveIn();
         Robot.turnLeft(0.01, "");
         Robot.checkDistanceSensors(0.01);
         Robot.centerRobot(skystonePosition1 - 24);
         Robot.strafeRight(0.05, "");


         Robot.setClawServo(ClawPosition.DOWN, 0.5, "");
         Robot.goBack(0.04,"");
         Robot.goForward(0.08,"");

         Robot.strafeLeft(0.7, "");
         Robot.checkDistanceSensors(0.01);
         Robot.turnRight(0.02, "");

         //Robot.BackwardStraight(19, 2.3 + additionalDistance);

         Robot.BackwardStraight(20, 1.2 + additionalDistance);
         Robot.moveOverLeft(20);
         Robot.BackwardStraight(20, 1.1 );

         Robot.setClawServo(ClawPosition.UP, 0.3, "");
         Robot.turnRight(0.01, "");
         Robot.goForward(0.8, "");
            }
     else {
         skystonePosition1 = 41.0;
         Robot.ForwardStraightTime(19, 1.1);
         Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

         //Robot.checkDistanceSensors(0.01);
         //Robot.strafeRight(0.45, "");

         Robot.checkDistanceSensors(0.01);
         Robot.strafeRight(0.3, "");
         Robot.centerRobot(skystonePosition1);
         Robot.moveOver();
         Robot.moveIn();
         Robot.turnLeft(0.01, "");
         Robot.checkDistanceSensors(0.01);
         Robot.centerRobot(skystonePosition1);
         Robot.strafeRight(0.05, "");
         //Robot.massTelemetryDump(15);

         Robot.setClawServo(ClawPosition.DOWN, 0.5, "");
         Robot.goBack(0.04,"");
         Robot.goForward(0.08,"");

         Robot.strafeLeft(0.7, "");
         Robot.checkDistanceSensors(0.01);
         Robot.turnRight(0.01, "");

         //Robot.BackwardStraight(19, 1.3);

         Robot.BackwardStraight(20, 0.5);
         Robot.moveOverLeft(20);
         Robot.BackwardStraight(20, 0.8 );

         Robot.setClawServo(ClawPosition.UP, 0.3, "");
         Robot.turnRight(0.01,"");
         Robot.goForward(0.7, "");
     }
        stop();

    }
    //TensorFlowDetection.shutdown();
}
