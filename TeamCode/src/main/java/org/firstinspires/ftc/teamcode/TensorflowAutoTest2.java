package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.TensorFlowWebcamDetection;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.*;

/* "Strafing" strategy: We strafe by each block to see if it is a Skystone.
If it is, knock it out and move it.
If not, move to the next stone.
 */
@Disabled
@Autonomous(name="TensorFlowAutoTest2")
public class TensorflowAutoTest2 extends LinearOpMode{
    int moveCount = 0;
    @Override
    public void runOpMode(){

        boolean moving = false;
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() { return opModeIsActive(); }
        });
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        TensorFlowWebcamDetection.init(hardwareMap,telemetry);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        boolean END = false;
        while (opModeIsActive()) {
            if (END == true) {
                telemetry.addData("Status", "Skystone FOUND!");    //
                telemetry.update();
            } else {
                if (moveCount != 5) {
                    if (TensorFlowWebcamDetection.getRecognitions() != null
                            && TensorFlowWebcamDetection.getRecognitions().size() == 1) {
                        moving = true;
                    }
                    // Update the recognitions if the moving is false.
                    if (!moving) {
                        TensorFlowWebcamDetection.updateRecognitions();
                    } else {
                        List<Recognition> rec = TensorFlowWebcamDetection.getRecognitions();

                        if (rec.get(0).getLabel().equals("Skystone")) {
                            executeCenterGrab();
                            moveToNext();
                            moving = false;
                            END = true;
                        } else {
                            moveToNext();
                            moving = false;
                        }
                    }
                }
            }
        }
        TensorFlowWebcamDetection.shutdown();
    }
    public void executeLeftGrab(){
        telemetry.addData("Status", "Execute pushing the left block!");    //
        telemetry.update();
    }
    public void executeCenterGrab(){
        telemetry.addData("Status", "Execute pushing the center block!");    //
        telemetry.update();
    }
    public void executeRightGrab(){
        telemetry.addData("Status", "Execute pushing the right block!");    //
        telemetry.update();
    }
    public void moveToNext(){
        telemetry.addData("Status", "Move to next!");    //
        telemetry.update();
        moveCount++;
    }
}
