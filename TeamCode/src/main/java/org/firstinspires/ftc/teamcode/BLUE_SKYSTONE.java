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
        TensorFlowDetection.init(hardwareMap, telemetry);
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Strafe Right and move forward to get into position

        Robot.setServos(FlipperPosition.BOTTOM, 0, "Lifting servos");
        Robot.setClawServo(ClawPosition.MEASURING, 0, "Getting claw ready");
        Robot.setForwardSpeed(0.5);
        Robot.strafeRight(1.32,"");
        Robot.centerRearRobot(27);
        //Robot.checkDistanceRearSensors(0.01);
        sleep(1500);
        //Robot.massTelemetryDump(15);

        //Step 2: Begin first read of blocks using Tensorflow
        boolean FOUND = false;
        //Robot.massTelemetryDump(15);
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                TensorFlowDetection.detectCenterSkystone()) {
            FOUND = true;
            skystonePosition1 = 24.5;
            skystonePosition2 = 2;
            additionalDistance = 0.4;
            //Robot.massTelemetryDump(15);

        } else {
            Robot.goForward(0.21,"");
            //Robot.massTelemetryDump(15);
        }

        if (FOUND == false) {
            sleep(1500);
        }
        if (FOUND == false && TensorFlowDetection.skystonesFound() >= 1 &&
                TensorFlowDetection.detectCenterSkystone()) {
            FOUND = true;
            skystonePosition1 = 32.5;
            skystonePosition2 = 1;
            additionalDistance = 0.0;

        } else if(FOUND == false) {
            Robot.goBack(0.6,"");
            FOUND = true;
            skystonePosition1 = 16;
            skystonePosition2 = 3;
            additionalDistance = 0.7;
            //   Robot.massTelemetryDump(15);
        }

        Robot.strafeRight(0.35,"");
        Robot.checkDistanceRearSensors(0.01);
        Robot.centerRearRobot(skystonePosition1);
        Robot.moveIn();
        Robot.strafeRight(0.05, "");
        Robot.turnLeft(0.01,"");
        //Robot.centerRearRobot(skystonePosition1);
        Robot.setClawServo(ClawPosition.DOWN,0.5,"");
        Robot.goBack(0.04,"");
        Robot.goForward(0.08,"");

        Robot.strafeLeft(0.7,"");
        Robot.checkDistanceRearSensors(0.01);
        Robot.turnLeft(0.01, "");
        //Robot.massTelemetryDump(15);

        Robot.ForwardStraightTime(20,1.3 + additionalDistance);
        Robot.setClawServo(ClawPosition.UP,0.3,"");

        Robot.goBack(0.4, "");
        //Robot.turnLeft(0.01,"");

        if (skystonePosition2 == 1) {
            Robot.BackwardStraightDistance(20, skystonePosition1 - 6);
            Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

            Robot.checkDistanceRearSensors(0.01);
            Robot.strafeRight(0.1, "");
            //Robot.checkDistanceRearSensors(0.01);

            Robot.centerRearRobot(skystonePosition1 - 24);
            Robot.moveOver();
            Robot.moveIn();
            Robot.turnLeft(0.01, "");
            //Robot.checkDistanceRearSensors(0.01);
            //Robot.centerRearRobot(skystonePosition1 - 24);
            //Robot.strafeRight(0.05, "");

            Robot.setClawServo(ClawPosition.DOWN, 0.5, "");
            Robot.goBack(0.04,"");
            Robot.goForward(0.08,"");

            Robot.strafeLeft(0.8, "");
            Robot.checkDistanceRearSensors(0.01);
            //Robot.turnLeft(0.01, "");

            //Robot.ForwardStraightTime(20, 2.2 + additionalDistance);

            Robot.ForwardStraightTime(20, 1.0 + additionalDistance);
            Robot.moveOverLeft(20);
            Robot.ForwardStraightTime(20, 0.9);

            Robot.setClawServo(ClawPosition.UP, 0.3, "");
            Robot.turnRight(0.01, "");
            Robot.goBack(0.7, "");
        }
    else if (skystonePosition2 == 2) {

            Robot.BackwardStraightDistance(20, skystonePosition1 - 4);
            Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

            Robot.checkDistanceRearSensors(0.01);
            Robot.strafeRight(0.3, "");
            //Robot.checkDistanceRearSensors(0.01);

            Robot.centerRearRobot(skystonePosition1 - 20);
            Robot.moveOver();
            Robot.moveIn();

            Robot.setForwardSpeed(0.2);
            Robot.goBack(0.5, "");
            Robot.setForwardSpeed(0.5);
            Robot.strafeRight(0.05, "");

            //Robot.massTelemetryDump(15);
            Robot.setClawServo(ClawPosition.DOWN, 0.5, "");
            Robot.goBack(0.04,"");
            Robot.goForward(0.08,"");

            Robot.strafeLeft(0.8, "");
            Robot.checkDistanceRearSensors(0.01);
            //Robot.turnLeft(0.01, "");

            //Robot.ForwardStraightTime(20, 2.2 + additionalDistance);

            Robot.ForwardStraightTime(20, 0.8 + additionalDistance);
            Robot.moveOverLeft(20);
            Robot.ForwardStraightTime(20, 1.2 );

            Robot.setClawServo(ClawPosition.UP, 0.3, "");

            Robot.turnRight(0.01, "");
            Robot.goBack(0.7, "");
        }
        else {
            skystonePosition1 = 32;
            Robot.BackwardStraight(20, 1.1);
            Robot.setClawServo(ClawPosition.MEASURING, 0.3, "");

            Robot.turnLeft(0.01, "");
            Robot.checkDistanceRearSensors(0.01);
            Robot.strafeRight(0.3, "");
            //Robot.checkDistanceRearSensors(0.01);

            Robot.centerRearRobot(skystonePosition1);
            Robot.moveOver();
            Robot.moveIn();
            Robot.turnLeft(0.01, "");
            //Robot.checkDistanceRearSensors(0.01);
            //Robot.centerRearRobot(skystonePosition1);
            //Robot.strafeRight(0.05, "");


            Robot.setClawServo(ClawPosition.DOWN, 0.5, "");
            Robot.goBack(0.04,"");
            Robot.goForward(0.08,"");

            Robot.strafeLeft(0.7, "");
            Robot.checkDistanceRearSensors(0.01);
            Robot.turnLeft(0.01, "");

            //Robot.ForwardStraightTime(20, 1.5);

            Robot.ForwardStraightTime(20, 0.4);
            Robot.moveOverLeft(20);
            Robot.ForwardStraightTime(20, 0.9);

            Robot.setClawServo(ClawPosition.UP, 0.3, "");

            Robot.turnRight(0.01, "");
            Robot.goBack(0.8, "");
        }



        stop();

    }
    //TensorFlowDetection.shutdown();
}
