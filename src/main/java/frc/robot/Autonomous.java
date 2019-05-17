/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/
// ====================================================================
// FILE NAME: Autonomous.java (Team 339 - Kilroy)
//
// CREATED ON: Jan 13, 2015
// CREATED BY: Nathanial Lydick
// MODIFIED ON:
// MODIFIED BY:
// ABSTRACT:
// This file is where almost all code for Kilroy will be
// written. Some of these functions are functions that should
// override methods in the base class (IterativeRobot). The
// functions are as follows:
// -----------------------------------------------------
// Init() - Initialization code for autonomous mode
// should go here. Will be called each time the robot enters
// autonomous mode.
// -----------------------------------------------------
// Periodic() - Periodic code for autonomous mode should
// go here. Will be called periodically at a regular rate while
// the robot is in autonomous mode.
// -----------------------------------------------------
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================
package frc.robot;

import frc.Hardware.Hardware;
import frc.HardwareInterfaces.LightSensor;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Utils.drive.Drive;
import frc.Utils.drive.Drive.BrakeType;
import edu.wpi.first.cameraserver.CameraServer;


/**
 * An Autonomous class. This class <b>beautifully</b> uses state machines in
 * order to periodically execute instructions during the Autonomous period.
 *
 * This class contains all of the user code for the Autonomous part of the
 * match, namely, the Init and Periodic code
 *
 *
 * @author Michael Andrzej Klaczynski
 * @written at the eleventh stroke of midnight, the 28th of January, Year of our
 *          LORD 2016. Rewritten ever thereafter.
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */
public class Autonomous
{
// when it starts, first wait for 1 second, then motor goes forward at .5 speed
// for two seconds, then it stops for .5 seconds, go backwards at full speed for
// five seconds, then stop for .75 seconds, forward again for .25 seconds at .6
// speed. then
// stop, wait for 1 second, then end auto. When waiting, print one statement
// saying "We are waiting" then when moving print once "We are moving"
/**
 * User Initialization code for autonomous mode should go here. Will run once
 * when the autonomous first starts, and will be followed immediately by
 * periodic().
 */
public static void init ()
{
    timer.start();

} // end Init

/**
 * State of autonomous as a whole; mainly for init, delay, finish, and choosing
 * which autonomous path is being used
 */
public static enum State
    {
    INIT, DELAY, CHOOSE_PATH, CROSS_AUTOLINE, DEPOSIT_STRAIGHT_CARGO_HATCH, DEPOSIT_ROCKET_HATCH, DEPOSIT_SIDE_CARGO_BALL, BLIND_ROCKET_HATCH, JANKY_DEPOSIT_STRAIGHT, FINISH
    }

/**
 * Starting position and which side of the field the robot is going to
 */

public static enum Position
    {
    LEFT, RIGHT, CENTER, NULL
    }

public static enum Level
    {
    LEVEL_ONE, LEVEL_TWO, DISABLE, NULL
    }

public static enum StateAuto
    {
    START, WAIT_1, WAIT_2, WAIT_3, WAIT_4, MOVE_1, MOVE_2, MOVE_3, END
    }

// variable that controls the state of autonomous as a whole (init, delay
// which path is being used, etc.)
public static State autoState = State.INIT;

public static StateAuto stateAuto = StateAuto.START;

// variable that controls the starting position/side (Left, Right, or Center) of
// the robot

public static Position autoPosition = Position.NULL;

// variable that controls the level the robot is starting on (Level 1 or level 2
// or disabled)

public static Level autoLevel = Level.NULL;

/**
 * User Periodic code for autonomous mode should go here. Will be called
 * periodically at a regular rate while the robot is in autonomous mode.
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 *
 *          FYI: drive.stop cuts power to the motors, causing the robot to
 *          coast. drive.brake results in a more complete stop.
 *          Meghan Brown; 10 February 2019
 *
 */
public static boolean canceledAuto = false;

public static void periodic ()
{
    System.out.println(timer.get());
    System.out.println(hasDoneTheThing());
    autoTest();

    if (hasDoneTheThing())
        SetNextEnum();
}

// ---------------------------------
// Methods
// ---------------------------------

public static boolean hasDoneTheThing ()
{
    boolean isThingDone = false;

    for (int extraneousInt = 0; extraneousInt < timeMatrix.length; extraneousInt++)
        {
        if (timer.get() >= timeMatrix[extraneousInt][0]
                && timer.get() < timeMatrix[extraneousInt][1]
                && realityMatrix[extraneousInt] != true)
            {
            isThingDone = true;
            realityMatrix[extraneousInt] = true;
            }
        }


    return isThingDone;
}

public static void SetNextEnum ()
{
    System.out.println("we have achieved SetNextEnum");
    switch (stateAuto)
        {
        case START:
            System.out.println("we are waiting");
            stateAuto = stateAuto.WAIT_1;
            break;
        case WAIT_1:
            System.out.println("and we're moving!");
            stateAuto = stateAuto.MOVE_1;
            break;
        case MOVE_1:
            System.out.println("we are waiting");
            stateAuto = stateAuto.WAIT_2;
            break;
        case WAIT_2:
            System.out.println("and we're moving!");
            stateAuto = stateAuto.MOVE_2;
            break;
        case MOVE_2:
            System.out.println("we are waiting");
            stateAuto = stateAuto.WAIT_3;
            break;
        case WAIT_3:
            System.out.println("and we're moving!");
            stateAuto = stateAuto.MOVE_3;
            break;
        case MOVE_3:
            System.out.println("we are waiting");
            stateAuto = stateAuto.WAIT_4;
            break;
        case WAIT_4:
            System.out.println("And we've finished");
            stateAuto = stateAuto.END;
            break;
        case END:
            System.out.println("The road goes ever on");
            stateAuto = stateAuto.START;
            break;
        }
}

public static void autoTest ()
{
    switch (stateAuto)
        {
        case START:
            break;
        case WAIT_1:
            break;
        case MOVE_1:
            break;
        case WAIT_2:
            break;
        case MOVE_2:
            break;
        case WAIT_3:
            break;
        case MOVE_3:
            break;
        case WAIT_4:
            break;
        case END:
            break;
        }
}
// *************************************************
// variables
// *************************************************

public static Timer timer = new Timer();

public static double[][] timeMatrix =
    {
            {0.9, 2.9, 3.4, 8.4, 9.2, 9.4, 10.4},
            {1.1, 3.1, 3.6, 8.6, 9.3, 9.6, 10.6}};

public static boolean[] realityMatrix =
    {false, false, false, false, false, false, false};
}
