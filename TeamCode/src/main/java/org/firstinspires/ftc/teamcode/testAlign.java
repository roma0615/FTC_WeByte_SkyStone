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


@Autonomous(name = "testAlign")

public class testAlign extends LinearOpMode {
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
        Robot.checkDistanceSensors(0.01);
        /*
        double averageRight = 0;
        double averageLeft = 0;
        for(int i = 0; i < 20; i++){
            averageRight += Robot.frontRightSensor.getDistance(DistanceUnit.INCH);
            averageLeft += Robot.frontLeftSensor.getDistance(DistanceUnit.INCH);
        }
        averageLeft = averageLeft / 20;
        averageRight = averageRight / 20;
        double difference = averageLeft - averageRight;
        while(difference > 1 || difference < -1) {
            if (difference > 1) {
                Robot.turnRight(0.01, "adjusting for drift");
                //checkDistanceSensors(duration);
                //Robot.massTelemetryDump(30);
            } else if (difference < -1) {
                Robot.turnLeft(0.01, "adjusting for drift");
                //checkDistanceSensors(duration);
                //Robot.massTelemetryDump(30);
            }
            averageRight = 0;
            averageLeft = 0;
            for(int i = 0; i < 20; i++){
                averageRight += Robot.frontRightSensor.getDistance(DistanceUnit.INCH);
                averageLeft += Robot.frontLeftSensor.getDistance(DistanceUnit.INCH);
            }
            averageLeft = averageLeft / 20;
            averageRight = averageRight / 20;
            difference = averageLeft - averageRight;
        }

         */

    }
}