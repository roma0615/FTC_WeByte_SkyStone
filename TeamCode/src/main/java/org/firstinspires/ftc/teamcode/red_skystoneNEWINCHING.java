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
@Autonomous(name = "red_skystoneNEWINCHING")


public class red_skystoneNEWINCHING extends LinearOpMode {
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
        //Robot.setClawServo(ClawPosition.MEASURING, 0, "Getting claw ready");
        Robot.setForwardSpeed(0.5);
        Robot.strafeRight(1.05,"");
        sleep(1000);


        //Step 2: Begin first read of blocks using Tensorflow
        //REWORK
        boolean FOUND = false;
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            skystonePosition1 = 32.6;
            additionalDistance = 0.3;
         //   Robot.massTelemetryDump(15);
        } else {
            Robot.goBack(0.2,"");
        }

        if (FOUND == false) {
            sleep(1000);
        }
        if (TensorFlowDetection.skystonesFound() >= 1 && FOUND == false &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 300 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            skystonePosition1 = 40.6;
            additionalDistance = 0.0;
           // Robot.massTelemetryDump(15);
        } else if(FOUND == false) {
            Robot.goForward(0.44,"");
            FOUND = true;
            skystonePosition1 = 24.6;
            additionalDistance = 0.6;
         //   Robot.massTelemetryDump(15);
        }
        boolean END = false;
        //Robot.goBack(0.05,"");
        Robot.strafeRight(0.27,"");
        Robot.checkDistanceSensors(0.01);
        Robot.centerRobot(skystonePosition1);
        Robot.moveIn();
        Robot.turnLeft(0.005,"");
        Robot.centerRobot(skystonePosition1);
        //Robot.moveIn();
        Robot.setClawServo(ClawPosition.DOWN,0.9,"");
        Robot.goBack(0.01,"");


        Robot.strafeLeft(0.7,"");
        Robot.checkDistanceSensors(0.01);
        //Robot.setForwardSpeed(1);
        //Robot.goBack(1.4 + additionalDistance,"");
        Robot.BackwardStraight(36,1.0 + additionalDistance);
        Robot.setClawServo(ClawPosition.UP,0.3,"");
        //Robot.massTelemetryDump(60);
        Robot.goForward(0.4, "");
        //Robot.strafeRight(0.3,"");
        //Robot.turnRight(0.01,"");
     if (skystonePosition1 > 30) {
         Robot.ForwardStraight(42, skystonePosition1 - 6);


         Robot.setForwardSpeed(0.5);
         Robot.checkDistanceSensors(0.01);
         Robot.strafeRight(0.5, "");
         Robot.checkDistanceSensors(0.01);
         Robot.centerRobot(skystonePosition1 - 24);
         //Robot.moveOver();
         Robot.moveIn();
         Robot.centerRobot(skystonePosition1 - 24);
         //Robot.massTelemetryDump(15);
         Robot.setClawServo(ClawPosition.DOWN, 0.3, "");
         Robot.checkDistanceSensors(0.01);
         Robot.strafeLeft(0.7, "");
         Robot.checkDistanceSensors(0.01);
         //Robot.setForwardSpeed(1);
         //Robot.goBack(2.4 + additionalDistance,"");
         Robot.BackwardStraight(36, 1.9 + additionalDistance);
         Robot.setClawServo(ClawPosition.UP, 0.3, "");
         Robot.goForward(0.6, "");
     }
     else {
         Robot.goForward(0.4, "");

     }
        stop();

    }
    //TensorFlowDetection.shutdown();
}
