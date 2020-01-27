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


@Autonomous(name = "red_skystoneOLDINCHING")


public class red_skystoneOLDINCHING extends LinearOpMode {
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
        Robot.strafeRight(1.2, "");
        sleep(3000);
        //Step 2: Begin first read of blocks using Tensorflow
        //REWORK
        boolean FOUND = false;
        //Robot.massTelemetryDump(15);
        if (TensorFlowDetection.skystonesFound() >= 1 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) > 250 &&
                (TensorFlowDetection.getSkystone().getLeft() +
                        TensorFlowDetection.getSkystone().getRight() / 2) < 900) {
            FOUND = true;
            skystonePosition1 = 33.5;
            additionalDistance = 0;
            Robot.massTelemetryDump(15);
        } else {
            Robot.goBack(0.3, "");
            Robot.massTelemetryDump(15);
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
            skystonePosition1 = 41.5;
            additionalDistance = -0.1;
            Robot.massTelemetryDump(15);
        } else if (FOUND == false) {
            stop();
            Robot.goForward(0.6, "");
            FOUND = true;
            skystonePosition1 = 25.5;
            additionalDistance = 0.1;
            Robot.massTelemetryDump(15);
        }
        telemetry.addData("Skystone", " seen!");
        Robot.stopMoving();
        boolean END = false;
        //Robot.goBack(0.05,"");
        Robot.strafeRight(0.6, "");
        Robot.checkDistanceSensors(0.01);
        Robot.centerRobot(skystonePosition1);
        Robot.moveIn();
        Robot.turnLeft(0.005, "");
        Robot.centerRobot(skystonePosition1);
        Robot.moveIn();
        //Robot.massTelemetryDump(15);
        Robot.setClawServo(ClawPosition.DOWN, 0.3, "");
        //Robot.massTelemetryDump(15);
        Robot.strafeLeft(1.0, "");
        Robot.checkDistanceSensors(0.01);
        Robot.setForwardSpeed(1);
        Robot.goBack(1.4 + additionalDistance, "");
        //Robot.BackwardStraight(26,1.4 + additionalDistance);
        Robot.setClawServo(ClawPosition.MEASURING, 0.7, "");
        //Robot.checkDistanceSensors(0.005);
        Robot.goForward(2.1 + additionalDistance, "");
        /*
        if(skystonePosition1 == 25){
            Robot.ForwardStraight(10,2);
        } else {
            Robot.ForwardStraight(10,skystonePosition1 - 24);
        }
         */
        Robot.setForwardSpeed(0.5);
        Robot.checkDistanceSensors(0.01);
        if (skystonePosition1 == 25) {
            Robot.centerRobot(2);
        } else {
            Robot.centerRobot(skystonePosition1 - 24);
        }
        //Robot.strafeRight(0.2,"");
        Robot.moveOver();
        Robot.checkDistanceSensors(0.01);
        Robot.moveIn();
        Robot.turnLeft(0.005, "");
        if (skystonePosition1 == 25) {
            Robot.centerRobot(2);
        } else {
            Robot.centerRobot(skystonePosition1 - 24);
        }
        Robot.moveIn();
        //Robot.massTelemetryDump(15);
        Robot.setClawServo(ClawPosition.DOWN, 0.3, "");
        Robot.checkDistanceSensors(0.01);
        Robot.strafeLeft(1.0, "");
        Robot.checkDistanceSensors(0.01);
        Robot.setForwardSpeed(1);
        Robot.goBack(2.4 + additionalDistance, "");
        //Robot.BackwardStraight(26,2.4 + additionalDistance);
        Robot.setForwardSpeed(0.5);
        Robot.setClawServo(ClawPosition.UP, 0.7, "");
        Robot.goForward(0.6 + additionalDistance, "");
        Robot.stopMoving();
    }
    //TensorFlowDetection.shutdown();
}
