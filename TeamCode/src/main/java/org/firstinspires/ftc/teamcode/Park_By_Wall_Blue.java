package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
@Autonomous(name="Park_By_Wall_Blue")

public class Park_By_Wall_Blue extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() {
                return opModeIsActive();
            }
        });
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run. POSITION ROBOT BACK FACING MIDLINE");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)

        waitForStart();
        Robot.setForwardSpeed(0.5);
        Robot.capstoneServo.setPosition(0.2);
        //ORIGINAL IDEA
        /*
        Robot.goBack(0.1,"");
        Robot.turnLeft(0.8,"");
        Robot.strafeRight(1,"");
        Robot.goBack(0.5,"");
        //Robot.goForward(0.5,"");
        Robot.strafeRight(0.5,"");
        Robot.superServoClaw.setPower(-1);
        //sleep(1000);
        //Robot.superServoClaw.setPower(0);
        Robot.capstoneServo.setPosition(0.2);
        sleep(4000);

         */
        //NEW IDEA
        Robot.goBack(1,"");
        Robot.strafeLeft(0.5,"");
        //Robot.superServoClaw.setPower(-1);
        Robot.waitForSeconds(4,"");
    }
}