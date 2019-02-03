package frc.HardwareInterfaces;

import frc.Hardware.Hardware;
// import frc.HardwareInterfaces.Transmission.MecanumTransmission;
// import frc.HardwareInterfaces.Transmission.TankTransmission;
import frc.HardwareInterfaces.Transmission.TransmissionBase;
// import frc.HardwareInterfaces.Transmission.TransmissionBase.TransmissionType;
import frc.Utils.drive.Drive;
import frc.vision.VisionProcessor;
import frc.vision.VisionProcessor.ImageType;
// import frc.vision.VisionProcessor.ImageType;
// import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.Relay.Value;

/**
 * Contains all game specific vision code, including code to drive to the switch
 * using vision
 *
 * @author: Becky Button
 */
public class DriveWithCamera extends Drive
{

// private TankTransmission tankTransmission = null;

// private MecanumTransmission mecanumTransmission = null;

// private KilroyEncoder leftFrontEncoder = null;
// private KilroyEncoder rightFrontEncoder = null;
// private KilroyEncoder leftRearEncoder = null;
// private KilroyEncoder rightRearEncoder = null;

private UltraSonic frontUltrasonic = null;

// private UltraSonic rearUltrasonic = null;

// private GyroBase gyro = null;

private VisionProcessor visionProcessor = null;

// private final TransmissionType transmissionType;

/**
 * Creates the drive with camera object. If a sensor listed is not used (except
 * for encoders), set it to null.
 *
 *
 * @param transmission
 *                              The robot's transmission object
 * @param leftFrontEncoder
 *                              The left-front corner encoder
 * @param rightFrontEncoder
 *                              The right-front corner encoder
 * @param leftRearEncoder
 *                              The left-rear corner encoder
 * @param rightRearEncoder
 *                              The right-rear corner encoder
 * @param ultrasonic
 *                              The sensor that finds distance using sound
 * @param gyro
 *                              A sensor that uses a spinning disk to measure
 *                              rotation.
 * @param visionProcessor
 *                              The camera's vision processing code, as a
 *                              sensor.
 */
public DriveWithCamera (TransmissionBase transmission,
        KilroyEncoder leftFrontEncoder,
        KilroyEncoder rightFrontEncoder, KilroyEncoder leftRearEncoder,
        KilroyEncoder rightRearEncoder,
        GyroBase gyro, VisionProcessor visionProcessor)
{
    super(transmission, leftFrontEncoder, rightFrontEncoder,
            leftRearEncoder, rightRearEncoder, gyro);

    this.visionProcessor = visionProcessor;
    // this.transmissionType = transmission.getType();
    // this.leftFrontEncoder = leftFrontEncoder;
    // this.rightFrontEncoder = rightFrontEncoder;
    // this.leftRearEncoder = leftRearEncoder;
    // this.rightRearEncoder = rightRearEncoder;
    // this.gyro = gyro;

}

/**
 * Creates drive with camera object
 *
 * @param transmission
 *                            The robot's transmission object
 * @param leftEncoder
 *                            The left encoder
 * @param rightEncoder
 *                            The right encoder
 * @param frontUltrasonic
 *                            The robot's front ultrasonic
 * @param rearUltrasonic
 *                            The robots's read ultrasonic
 * @param gyro
 *                            A sensor that uses a spinning disk to measure
 *                            rotation.
 * @param visionProcessor
 *                            The camera's vision processing code, as a sensor.
 *
 */
public DriveWithCamera (TransmissionBase transmission,
        KilroyEncoder leftEncoder, KilroyEncoder rightEncoder,
        UltraSonic frontUltrasonic, UltraSonic rearUltrasonic,
        GyroBase gyro, VisionProcessor visionProcessor)
{
    super(transmission, leftEncoder, rightEncoder, gyro);

    this.frontUltrasonic = frontUltrasonic;
    // this.rearUltrasonic = rearUltrasonic;
    this.visionProcessor = visionProcessor;
    // this.transmissionType = transmission.getType();
    // this.leftRearEncoder = leftEncoder;
    // this.rightRearEncoder = rightEncoder;
    // this.gyro = gyro;
}

/**
 * Creates drive with camera object
 *
 * @param transmission
 *                            The robot's transmission object
 * @param leftEncoder
 *                            The left encoder
 * @param rightEncoder
 *                            The right encoder
 * @param frontUltrasonic
 *                            The robot's front ultrasonic
 * @param rearUltrasonic
 *                            The robots's read ultrasonic
 * @param gyro
 *                            A sensor that uses a spinning disk to measure
 *                            rotation.
 * @param visionProcessor
 *                            The camera's vision processing code, as a sensor.
 * @param ringlightRelay
 *                            The janky fix for relay not working
 *
 */
public DriveWithCamera (TransmissionBase transmission,
        KilroyEncoder leftEncoder, KilroyEncoder rightEncoder,
        UltraSonic frontUltrasonic, UltraSonic rearUltrasonic,
        GyroBase gyro, VisionProcessor visionProcessor,
        DigitalOutput ringlightRelay)
{
    super(transmission, leftEncoder, rightEncoder, gyro);

    this.frontUltrasonic = frontUltrasonic;
    // this.rearUltrasonic = rearUltrasonic;
    this.visionProcessor = visionProcessor;
    // this.transmissionType = transmission.getType();
    // this.leftRearEncoder = leftEncoder;
    // this.rightRearEncoder = rightEncoder;
    // this.gyro = gyro;
}

/**
 * Drives using the camera until it hits CAMERA_NO_LONGER_WORKS inches, where it
 * then drives using the ultrasonic until the stopping distance
 *
 * Multiply the compensationFactor by speed to determine what values we are
 * sending to the motor controller
 *
 *
 * @param speed
 *                  have the speed greater than 0 and less than 1
 * @return true if the robot has driven all the way to the front of the target,
 *         and false if it hasn't
 */
public boolean driveToTarget (double speed)
{

    switch (state)
        {
        case INIT:
            this.visionProcessor.setRelayValue(Value.kOn);
            Hardware.autoTimer.reset();
            // visionProcessor.saveImage(ImageType.RAW);
            // visionProcessor.saveImage(ImageType.PROCESSED);

            // this.resetEncoders();
            double motorspeed = speed;
            double slowAmount;
            double slowestSpeed;
            state = DriveWithCameraState.DRIVE_WITH_CAMERA;
            break;
        case DRIVE_WITH_CAMERA:
            System.out.println("ultrasonic: " + this.frontUltrasonic
                    .getDistanceFromNearestBumper());


            // adjust speed based on distance
            if (this.frontUltrasonic
                    .getDistanceFromNearestBumper() < DISTANCE_FROM_WALL_TO_SLOW1
                    && this.frontUltrasonic
                            .getDistanceFromNearestBumper() > DISTANCE_FROM_WALL_TO_SLOW2)
                {
                slowAmount = SLOW_MODIFIER;
                } else if (this.frontUltrasonic
                        .getDistanceFromNearestBumper() < DISTANCE_FROM_WALL_TO_SLOW2)
                {
                slowAmount = SLOW_MODIFIER * SLOW_MODIFIER;
                } else
                {
                slowAmount = 1;
                }
            System.out.println("slow amount: " + slowAmount);

            motorspeed = speed * slowAmount;

            // adjust speed so that motors never reverse
            if (motorspeed - DRIVE_CORRECTION <= 0)
                {
                slowestSpeed = 0.05;
                } else
                {
                slowestSpeed = motorspeed - DRIVE_CORRECTION;
                }

            System.out.println("slowest speed: " + slowestSpeed);
            System.out.println("motorspeed: " + motorspeed);
            // gets the position of the center
            double centerX = this.getCameraCenterValue();
            // turns on the ring light


            // if the switch center is to the right of our center set by the
            // SWITCH_CAMERA_CENTER, correct by driving faster on the left
            if (centerX > SWITCH_CAMERA_CENTER - CAMERA_DEADBAND)
                {
                // the switch's center is too far right, drive faster on the
                // left
                System.out.println("WE ARE TOO left");
                this.getTransmission().driveRaw(
                        motorspeed + DRIVE_CORRECTION,
                        slowestSpeed);

                }
            // if the switch center is to the left of our center set by the
            // SWITCH_CAMERA_CENTER, correct by driving faster on the right
            else if (centerX < SWITCH_CAMERA_CENTER + CAMERA_DEADBAND)
                {
                // the switch's center is too far left, drive faster on the
                // right
                System.out.println("WE ARE TOO Right");
                this.getTransmission().driveRaw(slowestSpeed,
                        motorspeed + DRIVE_CORRECTION);

                } else
                {
                System.out.println("Driving straight");
                this.getTransmission().driveRaw(motorspeed, motorspeed);
                }

            if (this.frontUltrasonic
                    .getDistanceFromNearestBumper() <= CAMERA_NO_LONGER_WORKS
            /* && isAnyEncoderLargerThan(ENCODER_MIN_DISTANCE) */)
                {

                state = DriveWithCameraState.DRIVE_WITH_US;
                }


            if (this.frontUltrasonic
                    .getDistanceFromNearestBumper() <= DISTANCE_FROM_WALL_TO_STOP
            /* && Hardware.autoTimer.get() > .5 */)
                {
                state = DriveWithCameraState.STOP;
                }

            break;
        case DRIVE_WITH_US:

            // reset encoders gets null pointer
            // driveStraight(speed, 0, USING_GYRO);
            Hardware.drive.drive(speed, speed);

            // take a picture when we start to drive with ultrasonic

            if (this.frontUltrasonic
                    .getDistanceFromNearestBumper() <= DISTANCE_FROM_WALL_TO_STOP
                    && (Hardware.leftFrontDriveEncoder
                            .getDistance() >= MIN_INCHES
                            || Hardware.rightFrontDriveEncoder
                                    .getDistance() >= MIN_INCHES))
                {

                visionProcessor.saveImage(ImageType.RAW);
                visionProcessor.saveImage(ImageType.PROCESSED);
                state = DriveWithCameraState.STOP;
                }

            break;
        default:
        case STOP:
            Hardware.autoTimer.stop();

            // if we are too close to the wall, brake, then set all motors to
            // zero, else drive by ultrasonic
            System.out.println("We are stopping");
            this.getTransmission().driveRaw(0, 0);
            state = DriveWithCameraState.INIT;
            return true;
        }
    return false;
}

public static enum Side
    {
RIGHT, LEFT, NULL
    }

private Side side = Side.NULL;


/**
 * Code for 2019 camera to align to the rocket. returns Side that the camera see
 * vision targets on
 *
 * @author Conner McKevitt
 * @return
 */
public Side getTargetSide ()
{
    if (this.getCameraCenterValue() < SWITCH_CAMERA_CENTER)
        {
        side = Side.RIGHT;
        return side;
        } else if (this.getCameraCenterValue() > SWITCH_CAMERA_CENTER)
        {
        side = Side.LEFT;
        return side;
        }
    side = Side.NULL;
    return side;
}


private DriveWithCameraState state = DriveWithCameraState.INIT;

// private boolean takePicture = false;

private enum DriveWithCameraState
    {
INIT, DRIVE_WITH_CAMERA, DRIVE_WITH_US, STOP
    }

/**
 * Drives using the camera until it hits CAMERA_NO_LONGER_WORKS inches, where it
 * then drives using the ultrasonic, uses the janky relay fix
 *
 * Multiply the compensationFactor by speed to determine what values we are
 * sending to the motor controller
 *
 * @param compensationFactor
 *                               have the compensation factor greater than 1 and
 *                               less than 1.8
 * @param speed
 *                               have the speed greater than 0 and less than 1
 * @return true if the robot has driven all the way to the front of the scale,
 *         and false if it hasn't
 */
public boolean jankyDriveToTarget (double speed)
{
    switch (jankyState)
        {
        case INIT:
            state = DriveWithCameraState.DRIVE_WITH_CAMERA;
            break;
        case DRIVE_WITH_CAMERA:
            // gets the position of the center

            double centerX = this.getCameraCenterValue();

            // System.out.println("center" + centerX);
            // turns on the ring light
            // this.visionProcessor.setRelayValue(true);

            // this.visionProcessor.setDigitalOutputValue(true);
            // if the switch center is to the right of our center set by the
            // SWITCH_CAMERA_CENTER, correct by driving faster on the left
            if (centerX >= SWITCH_CAMERA_CENTER)
                {
                // the switch's center is too far right, drive faster on the
                // left
                // System.out.println("WE ARE TOO RIGHT");
                this.getTransmission().driveRaw(
                        speed + DRIVE_CORRECTION,
                        speed - DRIVE_CORRECTION);
                }
            // if the switch center is to the left of our center set by the
            // SWITCH_CAMERA_CENTER, correct by driving faster on the right
            else
                {
                // the switch's center is too far left, drive faster on the
                // right
                // System.out.println("WE ARE TOO LEFT");
                this.getTransmission().driveRaw(
                        speed - DRIVE_CORRECTION,
                        speed + DRIVE_CORRECTION);
                }

            if (this.frontUltrasonic
                    .getDistanceFromNearestBumper() <= CAMERA_NO_LONGER_WORKS)
                {
                state = DriveWithCameraState.DRIVE_WITH_US;
                }

            break;
        case DRIVE_WITH_US:
            driveStraight(speed, 0, true);

            if (this.frontUltrasonic
                    .getDistanceFromNearestBumper() <= DISTANCE_FROM_WALL_TO_STOP)
                state = DriveWithCameraState.STOP;

            break;
        default:
        case STOP:
            // if we are too close to the wall, brake, then set all motors to
            // zero, else drive by ultrasonic
            this.getTransmission().driveRaw(0, 0);
            state = DriveWithCameraState.INIT;
            return true;
        }
    return false;
}

private DriveWithCameraState jankyState = DriveWithCameraState.INIT;

/**
 * Method to test the vision code without the ultrasonic
 *
 * @param speed
 * @param compensationFactor
 */
public void visionTest (double compensationFactor, double speed)
{
    this.getCameraCenterValue();
    // System.out.println("Center for the vision : " + center);
    if (this.getCameraCenterValue() >= SWITCH_CAMERA_CENTER
            - CAMERA_DEADBAND
            && this.getCameraCenterValue() <= SWITCH_CAMERA_CENTER
                    + CAMERA_DEADBAND)
        {
        // driveStraight(speed, false);
        // System.out.println("We are aligned in the center");
        } else if (this.getCameraCenterValue() > SWITCH_CAMERA_CENTER
                + CAMERA_DEADBAND)
        {
        // center is too far left, drive faster on the right
        this.getTransmission().driveRaw(speed,
                speed * compensationFactor);
        // System.out.println("We're too right");
        } else
        {
        // center is too far right, drive faster on the left
        this.getTransmission().driveRaw(speed * compensationFactor,
                speed);
        // System.out.println("We're too left");
        }
}

// private int currentPictureIteration = 0;

/**
 * Gets the center x value of of the vision targets (average of the x values of
 * both visions targets)
 *
 * @return the current center x value
 */
public double getCameraCenterValue ()
{
    Hardware.axisCamera.setRelayValue(Value.kOn);
    double center = 0;

    visionProcessor.processImage();
    // if we have at least two blobs, the center is equal to the average
    // center
    // x position of the 1st and second largest blobs
    if (visionProcessor.getParticleReports().length >= 2)
        {
        center = (visionProcessor.getNthSizeBlob(0).center.x
                + visionProcessor.getNthSizeBlob(1).center.x) / 2;
        // System.out.println("blob center: " + center);

        // System.out.println("TWO BLOBS");
        }
    // if we only can detect one blob, the center is equal to the center x
    // position of the blob
    else if (visionProcessor.getParticleReports().length == 1)
        {
        center = visionProcessor.getNthSizeBlob(0).center.x;
        // System.out.println("ONE BLOBS");
        }
    // if we don't have any blobs, set the center equal to the constanct
    // center,
    // we can use this to just drive straight
    else
        {
        center = SWITCH_CAMERA_CENTER;
        // System.out.println("NO BLOBS");
        }
    return center;
}

// ================VISION CONSTANTS================
// the distance in inches in which we drive the robot straight using the
// ultrasonic
private final double CAMERA_NO_LONGER_WORKS = 3;

// The minimum encoder distance we must drive before we enable the ultrasonic
// private final double ENCODER_MIN_DISTANCE = 50; // inches
// 38 + 50;

// the number in pixels that the center we are looking for can be off
private final double CAMERA_DEADBAND = 10;

// the distance from the wall (in inches) where we start stopping the robot
private final double DISTANCE_FROM_WALL_TO_STOP = 25;

private final double DISTANCE_FROM_WALL_TO_SLOW1 = 80;

private final double DISTANCE_FROM_WALL_TO_SLOW2 = 50;

private final double SLOW_MODIFIER = .65;


private final double SWITCH_CAMERA_CENTER = 160;// Center of a 320x240 image
// 160 originally

private final double DRIVE_CORRECTION = .15;

private final boolean USING_GYRO = false;

private final double MIN_INCHES = 60;

}
