package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.Robot;
import org.firstinspires.ftc.teamcode.utils.ServoPosition;
import org.firstinspires.ftc.teamcode.utils.TensorFlowDetection;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.*;

/* "Strafing" strategy: We strafe by each block to see if it is a Skystone.
If it is, knock it out and move it.
If not, move to the next stone.
 */
@Autonomous(name="TensorFlowAutoTest")
public class TensorflowAutoTest extends LinearOpMode{
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
        TensorFlowDetection.init(hardwareMap,telemetry);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
            if(moveCount != 5) {
                if (TensorFlowDetection.getRecognitions() != null
                        && TensorFlowDetection.getRecognitions().size() == 1) {
                    moving = true;
                }
                // Update the recognitions if the moving is false.
                if(!moving) {
                    TensorFlowDetection.updateRecognitions();
                } else {
                    List<Recognition> rec = TensorFlowDetection.getRecognitions();

                    if (rec.get(0).getLabel().equals("Skystone")) {
                        executeCenterGrab();
                        moveToNext();
                        moving = false;
                    } else {
                        moveToNext();
                        moving = false;
                    }
                }
            }
        }
        TensorFlowDetection.shutdown();
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
