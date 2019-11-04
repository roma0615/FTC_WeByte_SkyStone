package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by isong on 10/17/18.
 */
@TeleOp(name="TeleOpROVERRUCKUS")
public class RoverRuckusTeleOp extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private CRServo intake = null;
    private DcMotor pully = null;
    private DcMotor turn = null;
    private boolean aPrev = false;
    private boolean bPrev = false;
    private boolean xPrev = false;
    private boolean yPrev = false;
    private boolean backPrev =false;
    private boolean rbPrev = false;

    private DcMotor linAct = null;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode() throws InterruptedException {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "fleft");frontRightDrive = hardwareMap.get(DcMotor.class, "fright");
        backLeftDrive = hardwareMap.get(DcMotor.class, "bleft");backRightDrive = hardwareMap.get(DcMotor.class, "bright");
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        frontLeftDrive.setPower(0);frontRightDrive.setPower(0);backLeftDrive.setPower(0);backRightDrive.setPower(0);
        intake = hardwareMap.get(CRServo.class, "intake");
        pully = hardwareMap.get(DcMotor.class, "pully");
        turn = hardwareMap.get(DcMotor.class, "turn");
        intake.setDirection(CRServo.Direction.FORWARD);
        pully.setDirection(DcMotorSimple.Direction.FORWARD);
        turn.setDirection(DcMotorSimple.Direction.FORWARD);
        turn.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int startPosition = turn.getCurrentPosition();
        linAct = hardwareMap.get(DcMotor.class, "linAct");




        telemetry.addData("Robot", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            drive();
            if(turn.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER))
            {
                if( gamepad2.left_stick_y>0.2 || gamepad2.left_stick_y<-0.2 ||gamepad2.right_stick_y>0.2){
                    turn.setPower(0.45*gamepad2.left_stick_y + 0.3 * gamepad2.right_stick_y);
                }else{
                    turn.setPower(0);
                    turn.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    turn.setTargetPosition(0);
                }
            }
            else{
                if(gamepad2.left_bumper)
                {
                    turn.setDirection(DcMotorSimple.Direction.REVERSE);
                    turn.setTargetPosition(startPosition + 100);
                    turn.setPower(0.3);
                }
                if(gamepad2.right_bumper)
                {
                    turn.setDirection(DcMotorSimple.Direction.FORWARD);
                    turn.setTargetPosition(startPosition+10);
                    turn.setPower(0.2);
                }
            }

            if(gamepad2.back&& !backPrev && turn.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER))
            {
                turn.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else if((gamepad2.back) && !backPrev && turn.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION))
            {
                turn.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            backPrev = gamepad2.back;

            if(gamepad2.a){
                pully.setPower(0.75);
            }else if(gamepad2.b){
                pully.setPower(-0.75);
            }else{
                pully.setPower(0);
            }
            /**if(gamepad2.x){
             intake.setPower(1);
             }else if(gamepad2.y){
             intake.setPower(-1);
             }else{
             intake.setPower(0);
             }
             */

            if (gamepad2.x && !xPrev && (intake.getPower() != 1)) {
                intake.setPower(1);
            }else if (gamepad2.x && !xPrev && intake.getPower() != 0) {
                intake.setPower(0);
            }

            if (gamepad2.y && !yPrev && (intake.getPower() != -1)) {
                intake.setPower(-1);
            } else if (gamepad2.y && !yPrev && (intake.getPower() != 0)) {
                intake.setPower(0);
            }

            xPrev = gamepad2.x;
            yPrev = gamepad2.y;

            if(gamepad1.a){
                linAct.setPower(1);
            }else if(gamepad1.b){
                linAct.setPower(-1);
            }else{
                linAct.setPower(0);
            }


            telemetry.addData("Wheel Power", "front left (%.2f), front right (%.2f), " +
                            "back left (%.2f), back right (%.2f)", frontLeftDrive.getPower(), frontRightDrive.getPower(),
                    backLeftDrive.getPower(), backRightDrive.getPower());
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("turn", "Power:" + turn.getPower());
            if(turn.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
                telemetry.addData("turn", "CurrPos" + turn.getCurrentPosition());
                telemetry.addData("turn", "TargetPos" + turn.getTargetPosition());
            }
            telemetry.addData("linearActuator", "Power:" + linAct.getPower());
            telemetry.addData((turn.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)?"free":"direct"), 0);
            telemetry.update();
        }
    }

    private void drive(){
        //DONT TOUCH THIS

        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;
        double v1 = r * Math.cos(robotAngle) - rightX;
        double v2 = r * Math.sin(robotAngle) + rightX;
        double v3 = r * Math.sin(robotAngle) - rightX;
        double v4 = r * Math.cos(robotAngle) + rightX;
        if(gamepad1.x) {
            v1 *=2;
            v2 *=2;
            v3 *=2;
            v4 *=2;
        }
        frontLeftDrive.setPower(v1*0.5);
        frontRightDrive.setPower(v2*0.5);
        backLeftDrive.setPower(v3*0.5);
        backRightDrive.setPower(v4*0.5);
        //OK YOU GOOD NOW
    }
}