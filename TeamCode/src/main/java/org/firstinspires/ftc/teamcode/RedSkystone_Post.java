package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.teamcode.utils.Robot.centerRobotRear;
import static org.firstinspires.ftc.teamcode.utils.Robot.centerRobotRight;
import static org.firstinspires.ftc.teamcode.utils.Robot.strafeLeft;

import org.firstinspires.ftc.teamcode.utils.BooleanFunction;
import org.firstinspires.ftc.teamcode.utils.FlipperPosition;
import org.firstinspires.ftc.teamcode.utils.Robot;

/**
 * This 2019-2020 OpMode illustrates the basics of using the Vuforia localizer to determine
 * positioning and orientation of robot on the SKYSTONE FTC field.
 * The code is structured as a LinearOpMode
 *
 * When images are located, Vuforia is able to determine the position and orientation of the
 * image relative to the camera.  This sample code then combines that information with a
 * knowledge of where the target images are on the field, to determine the location of the camera.
 *
 * From the Audience perspective, the Red Alliance station is on the right and the
 * Blue Alliance Station is on the left.

 * Eight perimeter targets are distributed evenly around the four perimeter walls
 * Four Bridge targets are located on the bridge uprights.
 * Refer to the Field Setup manual for more specific location details
 *
 * A final calculation then uses the location of the camera on the robot to determine the
 * robot's location and orientation on the field.
 *
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  skystone/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */

@Autonomous(name="RedSkystone_Post")
public class RedSkystone_Post extends LinearOpMode {


    // IMPORTANT: If you are using a USB WebCam, you must select CAMERA_CHOICE = BACK; and PHONE_IS_PORTRAIT = false;
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AQqx7Hv/////AAABmeHmysAgekSTg8A/T+sJO28b/TqN3z6xf6Dxrsqx/fV0NyN91JIaf1/r4s9SAhIFBarof770EinO7ACBxPgaRv8luUQcQWyyh1UUaC0EDVzgCDbZDLPAoj7+gch3RnNLYfRWt/mcyVs2oKHF/5hArN1Rk7ubxrjnUSOCnOLd7ncXo5W+O9XGnaVgEhD+lmuR0jjWFcSuRgYOuA7X6dXaeVdzumrQu7AH42+VtN4TIuSsEfTvH/wGKcGGehiX5MvWIvm7P+uMj256blkXPJLol4hksCfybiJpN2Zr7h4q0CALaHsdaBD5cSJBAadYdQuh6KxhXKQGwFybQty8ee0on4Dfmt6hq2i/aCXIGslJqol+";


    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;                                 // Units are degrees
    private static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField = 36 * mmPerInch;

    // Class Members
    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;

    /**
     * This is the webcam we are to use. As with other hardware devices such as motors and
     * servos, this device is identified using the robot configuration tool in the FTC application.
     */
    WebcamName webcamName = null;

    private boolean alignStatus = true;
    private long startTime = 0;
    private float xcoord = -99999;
    private double skystonePosition2 = 0;
    private float skystonePosition = 0;
    private boolean firsttime = true;
    private boolean targetVisible = false;
    private float phoneXRotate = 0;
    private float phoneYRotate = 0;
    private float phoneZRotate = 0;

    @Override
    public void runOpMode() {
        /*
         * Retrieve the camera we are to use.
         */
        Robot.init(hardwareMap, telemetry, new BooleanFunction() {
            @Override
            public boolean get() {
                return opModeIsActive();
            }
        });
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        /**
         * We also indicate which camera on the RC we wish to use.
         */
        parameters.cameraName = webcamName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");

        VuforiaTrackableDefaultListener listener;



        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
        // Rotated it to to face forward, and raised it to sit on the ground correctly.
        // This can be used for generic target-centric approach algorithms
        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));


        //
        // Create a transformation matrix describing where the phone is on the robot.
        //
        // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
        // Lock it into Portrait for these numbers to work.
        //
        // Info:  The coordinate frame for the robot looks the same as the field.
        // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
        // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
        //
        // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
        // pointing to the LEFT side of the Robot.
        // The two examples below assume that the camera is facing forward out the front of the robot.

        // We need to rotate the camera around it's long axis to bring the correct camera forward.
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90;
        }


        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
        final float CAMERA_FORWARD_DISPLACEMENT = 0.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot-center
        final float CAMERA_VERTICAL_DISPLACEMENT = 2.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));


        listener = (VuforiaTrackableDefaultListener) stoneTarget.getListener();
        listener.setPhoneInformation(robotFromCamera, parameters.cameraDirection);


        // WARNING:
        // In this sample, we do not wait for PLAY to be pressed.  Target Tracking is started immediately when INIT is pressed.
        // This sequence is used to enable the new remote DS Camera Preview feature to be used with this sample.
        // CONSEQUENTLY do not put any driving commands in this loop.
        // To restore the normal opmode structure, just un-comment the following line:

        // waitForStart();

        // Note: To use the remote camera preview:
        // AFTER you hit Init on the Driver Station, use the "options menu" to select "Camera Stream"
        // Tap the preview window to receive a fresh image.

        targetsSkyStone.activate();
        while (!isStopRequested() && (skystonePosition == 0)) {

            // check all the trackable targets to see which one (if any) is visible.
            targetVisible = false;
            if (((VuforiaTrackableDefaultListener) stoneTarget.getListener()).isVisible()) {
                telemetry.addData("Visible Target", stoneTarget.getName());
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) stoneTarget.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
            }

            // Provide feedback as to where the robot is located (if we know).
            if (targetVisible) {
                // express position (translation) of robot in inches.
                VectorF translation = lastLocation.getTranslation();
                xcoord = translation.get(1);
                telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                        translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);


                // express the rotation of the robot in degrees.
                Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
            } else {
                telemetry.addData("Visible Target", "none");
            }
            telemetry.update();

            if (opModeIsActive()) {
                if (firsttime) {
                    Robot.setForwardSpeed(0.7);
                    Robot.goBack(0.3, "");
                    firsttime = false;
                    startTime = System.currentTimeMillis();
                }

                if ((skystonePosition == 0) && ((System.currentTimeMillis() - startTime) > 2000)) {

                    if (xcoord > 0) {
                        skystonePosition = 1;
                        break;
                    } else {
                        if (xcoord == -99999) {
                            skystonePosition = 3;
                            break;
                        }
                        else {skystonePosition = 2;
                            break;
                        }
                    }
                    // express the rotation of the robot in degrees.
                    //Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                    //telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
                }

            }

        }

        // Disable Tracking when we are done;


        //   targetsSkyStone.deactivate();
        //telemetry.addData("Skystone position", String.format(Locale.ENGLISH, "%.01f", skystonePosition));
        //telemetry.update();

        Robot.setForwardSpeed(0.7);

        if (skystonePosition == 1) {
            skystonePosition2 = 43;
            Robot.strafeLeft(0.6, "");
            Robot.goBack(0.3, "end jump");
            Robot.centerRobotRight(skystonePosition2, 0.6, 0.04);
            Robot.moveIn();
            Robot.goBack(0.02, "");
        }

        if (skystonePosition == 2) {
            skystonePosition2 = 35.5;
            Robot.strafeLeft(0.1, "");
            Robot.goBack(0.3, "end jump");
            Robot.centerRobotRight(skystonePosition2, 0.6, 0.04);
            Robot.moveIn();
            Robot.goBack(0.02, "");
        }
        if (skystonePosition == 3) {
            skystonePosition2 = 26;
            Robot.strafeRight(0.26, "");
            Robot.goBack(0.3, "end jump");
            Robot.centerRobotRight(skystonePosition2, 0.6, 0.04);
            Robot.moveIn();
            Robot.goBack(0.02, "");
        }

        Robot.superServoClaw.setPower(-1);
        Robot.waitForSeconds(1.0,"");

        Robot.setForwardSpeed(1.0);
        Robot.goForward(0.25, "");
        Robot.turnRight(1.0, "");
        Robot.alignLeft();

        //  Robot.goBack((0.2), "");
        Robot.centerRobotLeft(24, 2, 0.08);
        Robot.goBack(((48 - skystonePosition2)/50 + 0.9), "");

        Robot.superServoClaw.setPower(1);
        Robot.waitForSeconds(0.2,"");

        Robot.alignLeft();
        Robot.goForward(0.7,"");


        if (skystonePosition == 1) {
            Robot.goForward(1.3, "");
            Robot.turnLeft(0.9, "");
            Robot.setForwardSpeed(0.5);
            Robot.strafeRight(1.0, "");
            Robot.setForwardSpeed(0.7);
            Robot.goBack(0.1, "");
            Robot.strafeLeft(0.36, "");
            Robot.centerRobotRight(17, 0.6, 0.04);
            Robot.moveIn();
            Robot.goBack(0.01, "");
            Robot.superServoClaw.setPower(-1);
            Robot.waitForSeconds(1.0,"");

            Robot.setForwardSpeed(1.0);
            Robot.goForward(0.3, "");
            Robot.strafeLeft(0.3, "");
            Robot.setForwardSpeed(1.0);
            Robot.turnRight(0.9, "");

            Robot.alignLeft();
            Robot.goBack((0.2), "");
            Robot.centerRobotLeft(24, 2, 0.08);
            Robot.goBack(1.3, "");
        }


        if (skystonePosition == 2) {
            Robot.goForward(1.3, "");
            Robot.turnLeft(0.9, "");
            Robot.setForwardSpeed(0.5);
            Robot.strafeRight(1.0, "");
            Robot.setForwardSpeed(0.7);
            Robot.goBack(0.1, "");
            Robot.strafeLeft(0.16, "");
            Robot.centerRobotRight(10.3, 0.6, 0.04);
            Robot.moveIn();
            Robot.goBack(0.01, "");
            Robot.superServoClaw.setPower(-1);
            Robot.waitForSeconds(1.0,"");

            Robot.setForwardSpeed(1.0);
            Robot.goForward(0.3, "");
            Robot.strafeLeft(0.3, "");
            Robot.setForwardSpeed(1.0);
            Robot.turnRight(0.9, "");

            Robot.alignLeft();
            Robot.goBack((0.3), "");
            Robot.centerRobotLeft(24, 2, 0.08);
            Robot.goBack(1.45, "");
        }


        if (skystonePosition == 3) {
            Robot.goForward(1.2, "");
            Robot.turnLeft(0.9, "");
            Robot.setForwardSpeed(0.5);
            Robot.strafeRight(0.7, "");
            Robot.setForwardSpeed(0.7);
            Robot.goBack(0.1, "");
            Robot.moveIn();
            Robot.goBack(0.01, "");
            Robot.superServoClaw.setPower(-1);
            Robot.waitForSeconds(1.0,"");

            Robot.setForwardSpeed(1.0);
            Robot.goForward(0.3, "");
            Robot.strafeLeft(0.3, "");
            Robot.setForwardSpeed(1.0);
            Robot.turnRight(0.9, "");

            Robot.alignLeft();
            Robot.goBack((0.3), "");
            Robot.centerRobotLeft(24, 2, 0.08);
            Robot.goBack((1.6), "");

        }


        Robot.superServoClaw.setPower(1);
        Robot.waitForSeconds(0.5,"");

        Robot.strafeRight(0.15, "");
        Robot.goForward(0.5, "");

        Robot.superServoClaw.setPower(-1);
        Robot.waitForSeconds(0.5,"");
        Robot.capstoneServo.setPosition(0.2);
        sleep(4000);
    }
}
