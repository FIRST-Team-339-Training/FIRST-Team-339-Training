package frc.Utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import frc.Hardware.Hardware;
import frc.HardwareInterfaces.KilroyEncoder;
import frc.HardwareInterfaces.RobotPotentiometer;
import frc.HardwareInterfaces.LightSensor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.HardwareInterfaces.QuickSwitch;

/**
 *
 *
 * @author Cole (mostly) 2019 build season
 *         Other Contributors: Ashley
 */
public class GamePieceManipulator
{

private SpeedController armMotor = null;

private RobotPotentiometer armPot = null;

private KilroyEncoder armEncoder = null;

private RollerIntakeMechanism intake = null;




/**
 * constructor to use in hardware
 */
public GamePieceManipulator (SpeedController armMotor,
        RobotPotentiometer armPot, SpeedController armRollers,
        LightSensor photoSwitch)
{
    this.armMotor = armMotor;
    this.armPot = armPot;
    this.intake = new RollerIntakeMechanism(armRollers, photoSwitch);
}

/**
 * constructor to use in hardware
 */
public GamePieceManipulator (SpeedController armMotor,
        KilroyEncoder armEncoder, SpeedController armRollers,
        LightSensor photoSwitch)
{
    this.armMotor = armMotor;
    this.armEncoder = armEncoder;
    this.intake = new RollerIntakeMechanism(armRollers, photoSwitch);
}

public static enum GamePiece
    {
    HATCH_PANEL, CARGO, NONE, BOTH
    }

// placeholder, will need to do something
// used to tell us things also
public static enum DeployState
    {
    DEPLOYED, MIDDLE, RETRACTED
    }

public static enum DeployMovementState
    {
    MOVING_TO_POSITION, MOVING_BY_JOY, STAY_AT_POSITION, STOP
    }

private static enum DeployMovementDirection
    {
    MOVING_UP, MOVING_DOWN, NEUTRAL
    }

// =========================================================================
// General Methods
// =========================================================================

// placeholder function since Forklift will need to understand which piece
// the manipulator has
// public GamePiece hasWhichGamePiece ()
// {
// if (this.intake.hasCargo() /* and does not have a Hatch */)
// {
// return GamePiece.CARGO;
// }

// return GamePiece.NONE;
// }


public void initiliazeConstantsFor2018 ()
{
    UP_JOYSTICK_SCALER = UP_JOYSTICK_SCALER_2018;
    DOWN_JOYSTICK_SCALER = DOWN_JOYSTICK_SCALER_2018;
    UPWARD_ARM_MOVEMENT_SCALER = UPWARD_ARM_MOVEMENT_SCALER_2018;
    DOWNWARD_ARM_MOVEMENT_SCALER = DOWNWARD_ARM_MOVEMENT_SCALER_2018;
    MAX_ARM_POSITION_ADJUSTED = MAX_ARM_POSITION_ADJUSTED_2018;
    MIN_ARM_POSITION_ADJUSTED = MIN_ARM_POSITION_ADJUSTED_2018;
    RETRACTED_ARM_POSITION_ADJUSTED = RETRACTED_ARM_POSITION_ADJUSTED_2018;
    PARALLEL_TO_GROUND_ADJUSTED = PARALLEL_TO_GROUND_ADJUSTED_2018;
    ARM_POT_SCALE_TO_DEGREES = ARM_POT_SCALE_TO_DEGREES_2018;
    STAY_UP_WITH_CARGO = STAY_UP_WITH_CARGO_2018;
    STAY_UP_NO_PIECE = STAY_UP_NO_PIECE_2018;
    ARM_POT_RAW_HORIZONTAL_VALUE = ARM_POT_RAW_HORIZONTAL_VALUE_2018;
    DEPLOYED_ARM_POSITION_ADJUSTED = DEPLOYED_ARM_POSITION_ADJUSTED_2018;
    DEFAULT_DEPLOY_SPEED_UNSCALED = DEFAULT_DEPLOY_SPEED_UNSCALED_2018;
    DEFAULT_RETRACT_SPEED_UNSCALED = DEFAULT_RETRACT_SPEED_UNSCALED_2018;
}

/**
 * Returns true is the manipulator is holding cargo, based on the photo
 * switch. False otherwise
 */
public boolean hasCargo ()
{
    return this.intake.hasCargo();
}


/**
 * Returns true is the deploy mechanism has been deployed (based on the
 * deploy potentiometer/ encoder), false otherwise
 */
public boolean isDeployed ()
{
    return this.getDeployState() == DeployState.DEPLOYED;
}

/**
 * Gets the current state of the deploy mechanism (DEPLOYED, MIDDLE
 * (not deployed or retracted), and RETRACTED). Not to be confused
 * with the deployMovementState, which is used by the state machine
 * to determine how the deploy should be moving
 *
 * @return the current state of the deploy mechanism
 */
public DeployState getDeployState ()
{
    if (this.getCurrentArmPosition() >= RETRACTED_ARM_POSITION_ADJUSTED
            - ACCEPTABLE_ERROR)
        return DeployState.RETRACTED;
    if (this.getCurrentArmPosition() <= DEPLOYED_ARM_POSITION_ADJUSTED
            + ACCEPTABLE_ERROR)
        return DeployState.DEPLOYED;
    return DeployState.MIDDLE;
}

/** Update all the states machines for this class */
public void masterUpdate ()
{
    this.intake.update();
    this.deployUpdate();
}


// =========================================================================
// armMotor methods
// =========================================================================


/**
 * call during teleop to move the arm up and down based on the joystick controls
 */
public void moveArmByJoystick (Joystick armJoystick,
        boolean overrideButton)
{
    if (Math.abs(armJoystick
            .getY()) > DEPLOY_JOYSTICK_DEADBAND)
        {
        // assuming up is a positive value
        // speed is not scaled based off the DEADBAND_BAND
        // because if it was, we might give it too weak of
        // a value at low joystick values outside the deadband
        // (i.e.: if we are just .01 outside the deadband, and
        // we sent the motor .01, then it might actually move
        // down due to gravity)
        double speed = armJoystick.getY();

        // if override button is pressed, ignore potentiometer/ encoder.

        // If we are trying to move up and past the max angle, or
        // trying to move down and below the min height, tell the
        // arm to stay where it is
        if (overrideButton == false)
            {
            if ((speed > 0
                    && this.getCurrentArmPosition() > currentDeployMaxAngle)
                    || (speed < 0 && this
                            .getCurrentArmPosition() < currentDeployMinAngle))
                {
                this.deployMovementState = DeployMovementState.STAY_AT_POSITION;
                // return so we exit the method and do not accidentally set
                // deployMovementState to MOVING_BY_JOY;
                return;
                }
            }

        // scales the speed based on whether it is going up or down
        if (speed > 0)
            deployTargetSpeed = speed * UP_JOYSTICK_SCALER;
        else
            deployTargetSpeed = speed * DOWN_JOYSTICK_SCALER;

        this.deployMovementState = DeployMovementState.MOVING_BY_JOY;

        }

}

/**
 * Returns angle of the arm by scaling the potentiometer value
 * for the deploy.
 *
 * @returns the angle of the arm in degrees, with 0 representing when
 *          the arm is parallel to the ground, and +90 when the arm
 *          is straight up and down
 */
public double getCurrentArmPosition ()
{
    // if (armPot != null)
    // {
    // scales the value from the arm pot so parallet to the ground is
    // zero, and perpenciular to the ground and pointing up is 90
    return (this.armPot.get()
            - ARM_POT_RAW_HORIZONTAL_VALUE)
            * ARM_POT_SCALE_TO_DEGREES;


    // } else // if we are not using an armPot, we should be using an encoder
    // {
    // // assumes that the value from the encoder is reset to 0
    // // when the robot is started and negative when the manipulator
    // // is below the starting position
    // // TODO should getDistance be used instead of get?
    // double valueFromHorizontal = (armEncoder.get()
    // - ARM_ENCODER_RAW_HORIZONTAL_VALUE)
    // * ARM_ENCODER_SCALE_TO_DEGREES;

    // return valueFromHorizontal;
    // }
}



/**
 * Method for setting the deploy arm to a preset angle using a button.
 * For use in teleop. The button just needs to be pressed once (not held)
 * and the dpeloy state machine will start moving to the necessary angle.
 * This can be interruted at any time by moving the joysticks past their
 * deadzones (causing joystick control to take over).
 *
 * This should be called directly as is in teleop and does not need to
 * be surrounded by any if statements
 *
 *
 * @param angle
 *                     the angle the arm will be moved to
 * @param armSpeed
 *                     the desired speed the arm will be moved at
 * @param button
 *                     the QuickSwitch we are using to say when we want
 *                     to move to the specified angle
 *
 */
public void moveArmByButton (double angle,
        double armSpeed, QuickSwitch button)
{
    // if the button is being held down and was not being held down before
    if (button.getCurrentValue() == true)
        {
        isSetDeployPositionInitReady = true;
        this.moveArmToPosition(angle, armSpeed);
        }
}

/**
 * Function to move the deploy arm to a specified angle. For use
 * in autonomous only (not in teleop). For teleop, please use
 * moveArmByButton.
 *
 * @param angle
 *                  the target angle the arm will move towards
 *
 * @param speed
 *                  the speed the arm will move at
 *
 *
 * @return true when the arm has finished moving to the proper
 *         position, false otherwise
 */
public boolean moveArmToPosition (double angle, double speed)
{
    // Sets the target position and speed, enables "moving-to-position"
    // state.
    if (isSetDeployPositionInitReady == true)
        {
        this.deployTargetAngle = angle;

        this.deployTargetSpeed = Math.abs(speed);

        deployDirection = DeployMovementDirection.NEUTRAL;
        this.deployMovementState = DeployMovementState.MOVING_TO_POSITION;

        if (deployTargetAngle < this.getCurrentArmPosition())
            {
            deployTargetSpeed *= DOWNWARD_ARM_MOVEMENT_SCALER;
            }
        else // if the forklift will move down
            {
            deployTargetSpeed *= UPWARD_ARM_MOVEMENT_SCALER;
            }

        isSetDeployPositionInitReady = false;
        }

    // return true is we are done moving, false is we are still going
    if (this.deployMovementState == DeployMovementState.STAY_AT_POSITION)
        {
        isSetDeployPositionInitReady = true;
        return true;
        }
    return false;
}

/**
 * Tells the state machine to deploy the arm, if it is not
 * already retracted
 *
 * Can be called once to tell the deploy to move using the state
 * machine in the background, or called continually for autonomous
 * code that waits until the arm deploys before moving on
 *
 * @returns true if the arm finished deploying, or was already deployed
 */
public boolean deployArm ()
{
    if (this.getDeployState() != DeployState.DEPLOYED)
        {
        isSetDeployPositionInitReady = true;
        return this.moveArmToPosition(DEPLOYED_ARM_POSITION_ADJUSTED,
                DEFAULT_DEPLOY_SPEED_UNSCALED);
        }
    return true; // if we are already deployed
}

/**
 * Tells the state machine to retract the arm, if it is not
 * already retracted
 *
 * Can be called once to tell the deploy to move using the state
 * machine in the background, or called continually for autonomous
 * code that waits until the arm retracts before moving on
 *
 * @returns true if the arm finished retracting, or was already retracted
 */
public boolean retractArm ()
{
    if (this.getDeployState() != DeployState.RETRACTED)
        {
        isSetDeployPositionInitReady = true;
        return this.moveArmToPosition(RETRACTED_ARM_POSITION_ADJUSTED,
                DEFAULT_RETRACT_SPEED_UNSCALED);
        }
    return true; // if we are already deployed
}

/**
 * Update method for the deploy state machine. Is what actually tells
 * the armMotor what to do based off the current deployMovementState.
 * This method needs to be called in Teleop or Autonomous periodic
 * in order for the deploy to be used in either function, respectively
 */
public void deployUpdate ()
{
    this.printDeployDebugInfo();

    if (deployMovementState != DeployMovementState.STAY_AT_POSITION)
        this.stayAtPosition2018InitIsReady = true;

    switch (deployMovementState)
        {
        case MOVING_TO_POSITION:
            SmartDashboard.putString("Target Position",
                    "" + this.deployTargetAngle);
            if ((this.deployTargetAngle > currentDeployMaxAngle)
                    || (this.deployTargetAngle < currentDeployMinAngle))
                {
                deployMovementState = DeployMovementState.STAY_AT_POSITION;
                break;
                }

            // Begins by stating whether we are increasing or decreasing
            if (deployDirection == DeployMovementDirection.NEUTRAL)
                {
                if (deployTargetAngle < this.getCurrentArmPosition())
                    deployDirection = DeployMovementDirection.MOVING_DOWN;
                else
                    deployDirection = DeployMovementDirection.MOVING_UP;
                }

            // Differentiate moving up from down
            if (deployDirection == DeployMovementDirection.MOVING_UP)
                {
                // If we have passed the value we wanted...
                if (this.getCurrentArmPosition() > deployTargetAngle)
                    {
                    deployMovementState = DeployMovementState.STAY_AT_POSITION;
                    // Reset the direction for next time.
                    deployDirection = DeployMovementDirection.NEUTRAL;
                    break;
                    }
                // we have NOT passed the value , keep going up.
                this.armMotor.set(deployTargetSpeed);
                }
            else
                {
                // If we have passed the value we wanted...
                if (this.getCurrentArmPosition() < deployTargetAngle)
                    {
                    deployMovementState = DeployMovementState.STAY_AT_POSITION;
                    // Reset the direction for next time.
                    deployDirection = DeployMovementDirection.NEUTRAL;
                    break;
                    }
                // we have NOT passed the value , keep going down.
                this.armMotor.set(-deployTargetSpeed);
                }
            break;
        case MOVING_BY_JOY:
            isSetDeployPositionInitReady = true;
            this.armMotor.set(deployTargetSpeed);
            // If we are no longer holding the joystick, then it will
            // automatically stay at position. If we are holding the
            // joysticks, then other functions will set
            // deployMovementState back to MOVINg_BY_JOY before we get
            // back here
            deployMovementState = DeployMovementState.STAY_AT_POSITION;
            break;

        default:
        case STAY_AT_POSITION:
            // TODO the new armMotor might not even need a voltage to
            // stay in place, so we might be able to just give the arm motor
            // 0.0 no matter what game piece we have

            // Depending on what piece the manipulator has, send the appropriate
            // value to the motor so the forklift does not slide down due to
            // gravity
            // If the manipulator has a cargo piece, send the appropriate
            // value to the motor so the forklift does not slide down due to
            // gravity
            if (Hardware.whichRobot == Hardware.RobotYear.KILROY_2018)
                {
                if (this.stayAtPosition2018InitIsReady == true)
                    {
                    if (this.hasCargo() == true)
                        stayAtPositionTempSpeed = Math
                                .abs(STAY_UP_WITH_CARGO_2018
                                        * Math.cos(this
                                                .getCurrentArmPosition()));
                    else
                        stayAtPositionTempSpeed = Math
                                .abs(STAY_UP_NO_PIECE_2018
                                        * Math.cos(this
                                                .getCurrentArmPosition()));
                    this.stayAtPosition2018InitIsReady = false;
                    }
                this.armMotor.set(stayAtPositionTempSpeed);
                }
            else
                {
                if (this.hasCargo() == true)
                    this.armMotor.set(STAY_UP_WITH_CARGO);
                else
                    this.armMotor.set(STAY_UP_NO_PIECE);
                }
            // Reset the direction for next move-to-position.
            deployDirection = DeployMovementDirection.NEUTRAL;
            isSetDeployPositionInitReady = true;
            break;

        case STOP:
            this.armMotor.set(0.0);
            break;

        }
}

public void printDeployDebugInfo ()
{
    SmartDashboard.putString("Arm Potentiometer Raw",
            "" + armPot.get());
    SmartDashboard.putString("Arm Angle Adjusted",
            "" + this.getCurrentArmPosition());
    SmartDashboard.putString("Deploy Movement State",
            "" + this.deployMovementState);
    SmartDashboard.putString("Arm Motor Value",
            "" + this.armMotor.get());
    SmartDashboard.putString("Deploy State",
            "" + this.getDeployState());
    SmartDashboard.putString("Is Deployed", "" + this.isDeployed());
    SmartDashboard.putNumber("Left Operator",
            Hardware.leftOperator.getY());
    SmartDashboard.putNumber("UP_JOYSTICK_SCALER",
            UP_JOYSTICK_SCALER);
    SmartDashboard.putNumber("DOWN_JOYSTICK_SCALER",
            DOWN_JOYSTICK_SCALER);
    SmartDashboard.putNumber("deployTargetSpeed",
            deployTargetSpeed);
}

// =========================================================================
// Hatch Panel Methods
// =========================================================================





// =========================================================================
// Roller methods
// =========================================================================

// TODO do we just want it so if you hit the override, even without pulling
// trigger, it intakes?
/**
 * Method for calling intake and outtake when one button is for moving the
 * rollers, and the other determines which direction they are being moved in
 *
 * This is private because this control scheme is not the one that the
 * operators want. intakeOuttakeByButtonsSeperated should be used instead.
 *
 * @param intakeButtonValue
 *                                      value of the button used for intake/
 *                                      outtake
 * @param reverseIntakeButtonValue
 *                                      value of the button that, if held, will
 *                                      reverse the direction on the intake
 *                                      motors when the intakeButton is held
 *                                      (causing the manipulator to outake
 *                                      instead of intale when both buttons are
 *                                      held)
 * @param intakeOverrideButtonValue
 *                                      value of the override button for intake,
 *                                      used if the photoSwitch is failing
 */
private void intakeOuttakeByButtons (boolean intakeButtonValue,
        boolean reverseIntakeButtonValue,
        boolean intakeOverrideButtonValue)
{
    this.intake.intakeOuttakeByButtons(intakeButtonValue,
            reverseIntakeButtonValue, intakeOverrideButtonValue);
}


/**
 * Method for calling intake and outtake when they are both mapped to two
 * different buttons. This is in contrast to the intakeOuttakeByButtons
 * method, which has one button for intake, and another that
 * reverses the intake
 *
 * @param intakeButtonValue
 *                                      value of the button used for intake
 * @param reverseIntakeButtonValue
 *                                      value of the button used for outtake
 * @param intakeOverrideButtonValue
 *                                      value of the override button for intake,
 *                                      used if the photoSwitch is failing
 */
public void intakeOuttakeByButtonsSeperated (boolean intakeButtonValue,
        boolean outtakeButtonValue, boolean intakeOverrideButtonValue)
{
    this.intake.intakeOuttakeByButtonsSeperated(intakeButtonValue,
            outtakeButtonValue, intakeOverrideButtonValue);
}




// =========================================================================
// Constants
// =========================================================================

// used to scale all relevant values for 2019 since the max speed
// the 2019 deploy arm can go is .2
private static double MAX_DEPLOY_SPEED_2019 = .2;

// ----- Joystick Constants 2019 -----
private static final double DEPLOY_JOYSTICK_DEADBAND = 0.2;

private static double UP_JOYSTICK_SCALER = .5 * MAX_DEPLOY_SPEED_2019;

private static double DOWN_JOYSTICK_SCALER = .5 * MAX_DEPLOY_SPEED_2019;

// ----- Joystick Constants 2018 -----

private static final double UP_JOYSTICK_SCALER_2018 = .65;

private static final double DOWN_JOYSTICK_SCALER_2018 = .1;



// ----- Deploy Position Constants 2019 -----

private static int MAX_ARM_POSITION_ADJUSTED = 85;

private static int MIN_ARM_POSITION_ADJUSTED = 5;

private static int DEPLOYED_ARM_POSITION_ADJUSTED = 10;

private static int RETRACTED_ARM_POSITION_ADJUSTED = 80;

private static int PARALLEL_TO_GROUND_ADJUSTED = 10;

// value that the arm pot returns when the manipulator is
// parallel to the floor
private static double ARM_POT_RAW_HORIZONTAL_VALUE = 260; // placeholder

private static final double ACCEPTABLE_ERROR = 0.0;

// Temporary values; should be unnecessay on the 2019 robot

// value that is multipled to the value from the arm pot to convert
// it to degrees
private static double ARM_POT_SCALE_TO_DEGREES = -0.428571; // placeholder

// value that is multiplied by the number of ticks to convert it to degrees
private static final double ARM_ENCODER_SCALE_TO_DEGREES = 0.0; // placeholder


// ----- Deploy Position Constants 2018 -----


private static final int MAX_ARM_POSITION_ADJUSTED_2018 = 85;

private static final int MIN_ARM_POSITION_ADJUSTED_2018 = 5;

private static final int RETRACTED_ARM_POSITION_ADJUSTED_2018 = 80;

private static final int DEPLOYED_ARM_POSITION_ADJUSTED_2018 = 10;

private static final int PARALLEL_TO_GROUND_ADJUSTED_2018 = 10;

// private static final int ACCEPTABLE_ERROR = 6;

// Temporary values; should be unnecessay on the 2019 robot

private static final double ARM_POT_RAW_RETRACTED_VALUE_2018 = 45;
// no higher than 70

// value that the arm pot returns when the manipulator is
// parallel to the floor
private static final double ARM_POT_RAW_HORIZONTAL_VALUE_2018 = 225; // placeholder

// value that the arm encoder returns when the manipulator is
// parallel to the floor
private static final double ARM_ENCODER_RAW_HORIZONTAL_VALUE_2018 = 0.0; // placeholder

// value that is multipled to the value from the arm pot to convert
// it to degrees
private static final double ARM_POT_SCALE_TO_DEGREES_2018 = -0.486486; // placeholder

// value that is multiplied by the number of ticks to convert it to degrees
private static final double ARM_ENCODER_SCALE_TO_DEGREES_2018 = 0.0; // placeholder

// ----- Deploy Speed Constants 2019 -----

private static double STAY_UP_WITH_CARGO = 0.2;

private static double STAY_UP_NO_PIECE = 0.2;

private static double UPWARD_ARM_MOVEMENT_SCALER = 1.0
        * MAX_DEPLOY_SPEED_2019;

private static double DOWNWARD_ARM_MOVEMENT_SCALER = 0.05
        * MAX_DEPLOY_SPEED_2019;

private static double DEFAULT_DEPLOY_SPEED_UNSCALED = 1.0;

private static double DEFAULT_RETRACT_SPEED_UNSCALED = 1.0;


// ----- Deploy Speed Constants 2018 -----

// should be used in 2018 only
private static final double CLOSE_ENOUGH_TO_PARALLEL_DEADBAND = 3;

// should be used in 2018 only
private static final double STAY_AT_PARALLEL_2018 = .1;

private static final double STAY_UP_WITH_CARGO_2018 = 0.3;

private static final double STAY_UP_NO_PIECE_2018 = 0.3;

private static final double UPWARD_ARM_MOVEMENT_SCALER_2018 = .65;

private static final double DOWNWARD_ARM_MOVEMENT_SCALER_2018 = 0.05;

private static final double DEFAULT_DEPLOY_SPEED_UNSCALED_2018 = 1.0;

private static final double DEFAULT_RETRACT_SPEED_UNSCALED_2018 = 1.0;

// =========================================================================
// Variables
// =========================================================================

private DeployMovementState deployMovementState = DeployMovementState.STAY_AT_POSITION;

private DeployMovementDirection deployDirection = DeployMovementDirection.NEUTRAL;

// The angle the manipulator is trying to move to; 0 is the start angle,
// positive angles are above the start, negative angles are below the starts
private double deployTargetAngle = 0.0;

private double deployTargetSpeed = 0.0;

private double currentDeployMaxAngle = MAX_ARM_POSITION_ADJUSTED;

private double currentDeployMinAngle = MIN_ARM_POSITION_ADJUSTED;

private boolean isSetDeployPositionInitReady = true;

// 2018 requires different speeds depending on the position of the arm
// this is used to determine whether or not we need to calculate a new
// speed
private boolean stayAtPosition2018InitIsReady = true;

private double stayAtPositionTempSpeed = 0.0;

// =========================================================================
// Tuneables
// =========================================================================

/**
 * Deploy goals:
 *
 * have a set max position and variable min positions (the
 * deploy will be able to go low enough to lift the bottom of
 * the robot up, and we probably don't want to do this
 * until we climb)
 *
 *
 *
 */


}
