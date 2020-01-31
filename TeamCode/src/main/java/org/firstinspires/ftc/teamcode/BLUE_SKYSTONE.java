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
@Autonomous(name = "BLUE_SKYSTONE")


public class BLUE_SKYSTONE extends LinearOpMode {
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
        Robot.strafeRight(1.32,"");
        Robot.centerRearRobot(30.5);
        //Robot.checkDistanceRearSensors(0.01);
        sleep(1500);

        //Step 2: Begin first read of blocks using Tensorflow
        boolean FOUND = false;
        Robot.massTelemetryDump(15);
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            skystonePosition1 = 23.5;
            skystonePosition2 = 2;
            additionalDistance = 0.4;
            Robot.massTelemetryDump(15);

        } else {
            Robot.goForward(0.21,"");
            Robot.massTelemetryDump(15);
        }

        if (FOUND == false) {
            sleep(1500);
        }
        if (TensorFlowDetection.skystonesFound() >= 1 && FOUND == false &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            skystonePosition1 = 32;
            skystonePosition2 = 1;
            additionalDistance = 0.0;

        } else if(FOUND == false) {
            Robot.goBack(0.4,"");
            FOUND = true;
            skystonePosition1 = 16;
            skystonePosition2 = 3;
            additionalDistance = 0.7;
            //   Robot.massTelemetryDump(15);
        }

        Robot.strafeRight(0.3,"");
        //Robot.checkDistanceRearSensors(0.01);
        Robot.centerRearRobot(skystonePosition1);
        Robot.moveIn();
        Robot.turnRight(0.005,"");
        Robot.centerRearRobot(skystonePosition1);
        Robot.setClawServo(ClawPosition.DOWN,0.9,"");
        Robot.goBack(0.005,"");
        Robot.goForward(0.01,"");

        Robot.strafeLeft(0.7,"");
        Robot.checkDistanceRearSensors(0.01);
        Robot.turnLeft(0.01, "");
        //Robot.massTelemetryDump(15);

        Robot.ForwardStraightTime(19,1.3 + additionalDistance);
        Robot.setClawServo(ClawPosition.UP,0.3,"");

        Robot.goBack(0.4, "");
        Robot.turnLeft(0.01,"");

        if (skystonePosition2 == 1) {
            Robot.BackwardStraightDistance(19, skystonePosition1 - 4);
            Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

            Robot.checkDistanceRearSensors(0.01);
            Robot.strafeRight(0.45, "");
            Robot.checkDistanceRearSensors(0.01);

            Robot.centerRearRobot(skystonePosition1 - 24);
            Robot.moveOver();
            Robot.moveIn();
            Robot.centerRearRobot(skystonePosition1 - 24);

            Robot.setClawServo(ClawPosition.DOWN, 0.9, "");
            Robot.goBack(0.005,"");
            Robot.goForward(0.01,"");

            Robot.strafeLeft(0.7, "");
            Robot.checkDistanceRearSensors(0.01);
            Robot.turnLeft(0.01, "");

            Robot.ForwardStraightTime(19, 2.1 + additionalDistance);

            // Robot.ForwardStraightTime(19, 1.1 + additionalDistance);
            // Robot.moveOverLeft(19);
            // Robot.ForwardStraightTime(19, 1.0 );

            Robot.setClawServo(ClawPosition.UP, 0.3, "");
            Robot.turnLeft(0.01, "");
            Robot.goBack(0.7, "");
        }
    else if (skystonePosition2 == 2) {

            Robot.BackwardStraightDistance(19, skystonePosition1 - 4);
            Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

            Robot.checkDistanceRearSensors(0.01);
            Robot.strafeRight(0.45, "");
            Robot.checkDistanceRearSensors(0.01);

            Robot.centerRearRobot(skystonePosition1 - 20);
            Robot.moveOver();
            Robot.moveIn();

            Robot.setForwardSpeed(0.2);
            Robot.goBack(0.5, "");
            Robot.setForwardSpeed(0.5);

            //Robot.massTelemetryDump(15);
            Robot.setClawServo(ClawPosition.DOWN, 0.9, "");
            Robot.goBack(0.005,"");
            Robot.goForward(0.01,"");

            Robot.strafeLeft(0.7, "");
            Robot.checkDistanceRearSensors(0.01);
            Robot.turnLeft(0.01, "");

            Robot.ForwardStraightTime(19, 2.1 + additionalDistance);

            // Robot.ForwardStraightTime(19, 1.1 + additionalDistance);
            // Robot.moveOverLeft(19);
            // Robot.ForwardStraightTime(19, 1.0 );

            Robot.setClawServo(ClawPosition.UP, 0.3, "");

            Robot.turnLeft(0.01, "");
            Robot.goBack(0.7, "");
        }
        else {
            skystonePosition1 = 32;
            Robot.BackwardStraight(19, 1.1);

            Robot.checkDistanceRearSensors(0.01);
            Robot.strafeRight(0.45, "");
            Robot.checkDistanceRearSensors(0.01);

            Robot.centerRearRobot(skystonePosition1);
            Robot.moveOver();
            Robot.moveIn();
            Robot.centerRearRobot(skystonePosition1);
            //Robot.massTelemetryDump(15);

            Robot.setClawServo(ClawPosition.DOWN, 0.9, "");
            Robot.goBack(0.005,"");
            Robot.goForward(0.01,"");

            Robot.strafeLeft(0.7, "");
            Robot.checkDistanceRearSensors(0.01);
            Robot.turnLeft(0.01, "");

            Robot.ForwardStraightTime(19, 1.3);
            Robot.setClawServo(ClawPosition.UP, 0.3, "");

            Robot.turnLeft(0.01, "");
            Robot.goBack(0.7, "");
        }



        stop();

    }
    //TensorFlowDetection.shutdown();
}
