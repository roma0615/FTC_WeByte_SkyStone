

package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.Comparator;
import java.util.List;

public class TensorFlowDetection {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    public static final String LABEL_FIRST_ELEMENT = "Stone";
    public static final String LABEL_SECOND_ELEMENT = "Skystone";

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
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private static VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private static TFObjectDetector tfod;
    private static List<Recognition> recognitions;

    private static Telemetry telemetry;

    public static void init(HardwareMap hwmap, Telemetry tel) {
        telemetry = tel;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod(hwmap);
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }
    }

    public static Comparator<Recognition> sortByLeft() {
        return new Comparator<Recognition>() {
            @Override
            public int compare(Recognition r1, Recognition r2) {
                return (int)(r1.getLeft() - r2.getLeft());
            }
        };
    }

    public static int skystonesFound() {
        if (getRecognitions() == null) return 0;
        else {
            int c = 0;
            for (Recognition r : recognitions) {
                if (r.getLabel() == LABEL_SECOND_ELEMENT) {
                    c++;
                }
            }
            return c;
        }
    }

    /** Return a list of recognitions from the tensorflow object detector */
    public static List<Recognition> getRecognitions() {
        return recognitions;
    }

    /** Update the recognitions list */
    public static void updateRecognitions() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                recognitions = updatedRecognitions;
                telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                }
                telemetry.update();
            }
        }
    }

    public static void shutdown() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private static void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //FRONT camera is an option
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private static void initTfod(HardwareMap hwmap) {
        int tfodMonitorViewId = hwmap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwmap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        //MINIMUM CONFIDENCE IS HERE - VERY IMPORTANT PARAMETER
        //0.5 is often agreed to be the optimal confidence.
        //Our original confidence was 0.65
        tfodParameters.minimumConfidence = 0.5;


        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}

