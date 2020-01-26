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


@Autonomous(name = "testAlgorithm2")

public class testAlgorithm2 extends LinearOpMode {
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


        Robot.setForwardSpeed(0.5);
        Robot.setTurnSpeed(0.5);
        //F11inches
        Robot.strafeRight(1,"");
        Robot.massTelemetryDump(15);
        Robot.goForward(0.2,"");
        Robot.massTelemetryDump(15);
        Robot.goBack(0.37,"");
        Robot.massTelemetryDump(15);
        Robot.strafeRight(0.25,"");
        Robot.massTelemetryDump(15);
        Robot.checkDistanceSensors(0.01);
        Robot.centerRobot(40.8);
        Robot.moveIn();
        Robot.turnLeft(0.01,"");
        Robot.centerRobot(40.8);
        Robot.setClawServo(ClawPosition.DOWN,2,"");
        /*
        //F8Inches
        Robot.goForward(0.2,"");
        Robot.massTelemetryDump(30);
        Robot.goForward(0.06,"");
        Robot.massTelemetryDump(30);
        Robot.strafeRight(0.4,"");
        Robot.massTelemetryDump(30);
        //F1Inch
        Robot.goForward(0.01,"");
        Robot.massTelemetryDump(30);
        //F2Inch turn
        //Robot.turnRight(0.01,"");
        //Robot.massTelemetryDump(300);
         */
    }
}
