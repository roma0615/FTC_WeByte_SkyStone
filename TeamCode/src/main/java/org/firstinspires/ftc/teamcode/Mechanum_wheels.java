package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**

 * Created by isong on 10/17/18.

 */

@TeleOp(name="MechanumWheels")

public class Mechanum_wheels extends LinearOpMode {

    // Declare OpMode members.

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor forwardLeftDrive1 = null;
    private DcMotor backLeftDrive2 = null;
    private DcMotor forwardRightDrive1 = null;
    private DcMotor backRightDrive2 = null;

    private Servo rightServo = null;

    private Servo leftServo = null;

    private DcMotor turn = null;

    private boolean aPrev = false;

    private boolean bPrev = false;

    private boolean xPrev = false;

    private boolean yPrev = false;

    private boolean backPrev =false;

    private boolean rbPrev = false;

    private boolean servosToggle = false;



    private DcMotor linAct = null;





    /*

     * Code to run ONCE when the driver hits INIT

     */

    @Override

    public void runOpMode() throws InterruptedException {

        forwardLeftDrive1  = hardwareMap.get(DcMotor.class, "forwardLeft_drive");
        forwardRightDrive1 = hardwareMap.get(DcMotor.class, "forwardRight_drive");
        backLeftDrive2  = hardwareMap.get(DcMotor.class, "backLeft_drive");
        backRightDrive2 = hardwareMap.get(DcMotor.class, "backRight_drive");
        rightServo = hardwareMap.get(Servo.class, "servoRight");
        leftServo = hardwareMap.get(Servo.class, "servoLeft");

        forwardLeftDrive1.setDirection(DcMotor.Direction.REVERSE);backLeftDrive2.setDirection(DcMotor.Direction.REVERSE);

        forwardRightDrive1.setDirection(DcMotor.Direction.FORWARD);backRightDrive2.setDirection(DcMotor.Direction.FORWARD);

        forwardLeftDrive1.setPower(0);forwardRightDrive1.setPower(0);backLeftDrive2.setPower(0);backRightDrive2.setPower(0);


//        int startPosition = turn.getCurrentPosition();
        int startPosition = 0;

//        linAct = hardwareMap.get(DcMotor.class, "linAct");









        telemetry.addData("Robot", "Initialized");

        telemetry.update();

        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        runtime.reset();

        while (opModeIsActive()) {

            drive();

/*
//            if(turn.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER))
//
//            {
//*/
//
//                if( gamepad2.left_stick_y>0.2 || gamepad2.left_stick_y<-0.2 ||gamepad2.right_stick_y>0.2){
//
//                    turn.setPower(0.45*gamepad2.left_stick_y + 0.3 * gamepad2.right_stick_y);
//
//                }else{
//
////                    turn.setPower(0);
////
////                    turn.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
////
////                    turn.setTargetPosition(0);
//
//                }
//
////            }
////
////            else{
//
//                if(gamepad2.left_bumper)
//
//                {
//
//                    turn.setDirection(DcMotorSimple.Direction.REVERSE);
//
//                    turn.setTargetPosition(startPosition + 100);
//
//                    turn.setPower(0.3);
//
//                }
//
//                if(gamepad2.right_bumper)
//
//                {
//
//                    turn.setDirection(DcMotorSimple.Direction.FORWARD);
//
//                    turn.setTargetPosition(startPosition+10);
//
//                    turn.setPower(0.2);
//
//                }
//
//            // }
//
//
//
////            if(gamepad2.back&& !backPrev && turn.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER))
////
////            {
////
////                turn.setMode(DcMotor.RunMode.RUN_TO_POSITION);
////
////            }
////
////            else if((gamepad2.back) && !backPrev && turn.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION))
////
////            {
////
////                turn.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
////
////            }

            backPrev = gamepad2.back;


            xPrev = gamepad2.x;

            yPrev = gamepad2.y;



//            if(gamepad1.a){
//
//                linAct.setPower(1);
//
//            }else if(gamepad1.b){
//
//                linAct.setPower(-1);
//
//            }else{
//
//                linAct.setPower(0);
//
//            }
            
            if (gamepad2.a) {
                servosToggle = !servosToggle;
            }


            if (servosToggle) {
                rightServo.setPosition(0.55);
                leftServo.setPosition(0.55);
            } else {
                rightServo.setPosition(0.1);
                leftServo.setPosition(0.1);
            }




            telemetry.addData("Wheel Power", "front left (%.2f), front right (%.2f), " +

                            "back left (%.2f), back right (%.2f)", forwardLeftDrive1.getPower(), forwardRightDrive1.getPower(),

                    backLeftDrive2.getPower(), backRightDrive2.getPower());

            telemetry.addData("Status", "Run Time: " + runtime.toString());

//            telemetry.addData("turn", "Power:" + turn.getPower());

//            if(turn.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
//
//                telemetry.addData("turn", "CurrPos" + turn.getCurrentPosition());
//
//                telemetry.addData("turn", "TargetPos" + turn.getTargetPosition());
//
//            }

//            telemetry.addData("linearActuator", "Power:" + linAct.getPower());

//            telemetry.addData((turn.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)?"free":"direct"), 0);

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
        /*
        if(gamepad1.x) {

            v1 *=2;

            v2 *=2;

            v3 *=2;

            v4 *=2;

        }
        */
        forwardLeftDrive1.setPower(v1*0.5);

        forwardRightDrive1.setPower(v2*0.5);

        backLeftDrive2.setPower(v3*0.5);

        backRightDrive2.setPower(v4*0.5);

        //OK YOU GOOD NOW

    }

}