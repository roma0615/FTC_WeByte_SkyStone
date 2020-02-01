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


@Autonomous(name = "testDetectCenterSkystone")

public class testDetectCenterSkystone extends LinearOpMode {
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
        TensorFlowDetection.init(hardwareMap, telemetry);
        telemetry.addData("Robot", "Is ready");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        while(opModeIsActive()) {
            if (TensorFlowDetection.detectCenterSkystone()) {
                telemetry.addData("DETECTED", "SKYSTONE");

            }
            Robot.massTelemetryDump(1);
        }


    }
}