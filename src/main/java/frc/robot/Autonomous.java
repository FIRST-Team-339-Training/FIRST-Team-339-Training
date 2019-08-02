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

/**
 * User Initialization code for autonomous mode should go here. Will run once
 * when the autonomous first starts, and will be followed immediately by
 * periodic().
 */
public static void init ()
{


} // end Init

/**
 * State of autonomous as a whole; mainly for init, delay, finish, and choosing
 * which autonomous path is being used
 */
public static enum State
    {
    INIT, AUTO, FINISH
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

// variable that controls the state of autonomous as a whole (init, delay
// which path is being used, etc.)
public static State autoState = State.INIT;

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



private static enum Assignment2State
    {
    INIT, WAIT_1, DRIVE_1, WAIT_2, DRIVE_2, WAIT_3, DRIVE_3, WAIT_4, FINISHED
    };


private static Assignment2State assignment2State = Assignment2State.INIT;

private static Timer assignment2Timer = new Timer();

// all times are in seconds since that is what the timer is in
private static double ASSIGN_2_WAIT_1_TIME = 1;

private static double ASSIGN_2_DRIVE_1_TIME = .5;

private static double ASSIGN_2_WAIT_2_TIME = .5;

private static double ASSIGN_2_DRIVE_2_TIME = 5;

private static double ASSIGN_2_WAIT_3_TIME = .75;

private static double ASSIGN_2_DRIVE_3_TIME = .25;

private static double ASSIGN_2_WAIT_4_TIME = 1;

private static double ASSIGN_2_DRIVE_1_SPEED = 1.0;

private static double ASSIGN_2_DRIVE_2_SPEED = -1.0;

private static double ASSIGN_2_DRIVE_3_SPEED = 0.6;

// Assignment 2: Write an autonomous that:
// when it starts, first wait for 1 second, then motor goes forward
// .5 speed for two seconds, then it stops for .5 seconds, go backwards
// at full speed for five seconds, then stop for .75 seconds, forward
// again for .25 seconds at .6 speed. then stop, wait for 1 second,
// then end auto. When waiting, print one statement
// saying "We are waiting" then when moving print once "We are moving"
private static void assignment2 ()
{
    switch (assignment2State)
        {
        case INIT:
            assignment2State = Assignment2State.WAIT_1;
            System.out.println("Entering WAIT_1");
            assignment2Timer.reset();
            assignment2Timer.start();
            break;

        case WAIT_1:

            if (assignment2Timer.get() > ASSIGN_2_WAIT_1_TIME)
                {
                assignment2State = Assignment2State.DRIVE_1;
                System.out.println("Entering DRIVE_1");
                assignment2Timer.reset();
                assignment2Timer.start();
                }

            break;

        case DRIVE_1:

            if (assignment2Timer.get() > ASSIGN_2_DRIVE_1_TIME)
                {
                Hardware.testboardMotor.set(0.0);
                assignment2State = Assignment2State.WAIT_2;
                System.out.println("Entering WAIT_2");
                assignment2Timer.reset();
                assignment2Timer.start();
                }
            else
                {
                Hardware.testboardMotor.set(ASSIGN_2_DRIVE_1_SPEED);
                }

            break;

        case WAIT_2:

            if (assignment2Timer.get() > ASSIGN_2_WAIT_2_TIME)
                {
                assignment2State = Assignment2State.DRIVE_2;
                System.out.println("Entering DRIVE_2");
                assignment2Timer.reset();
                assignment2Timer.start();
                }
            break;

        case DRIVE_2:
            if (assignment2Timer.get() > ASSIGN_2_DRIVE_2_TIME)
                {
                Hardware.testboardMotor.set(0.0);
                assignment2State = Assignment2State.WAIT_3;
                System.out.println("Entering WAIT_3");
                assignment2Timer.reset();
                assignment2Timer.start();
                }
            else
                {
                Hardware.testboardMotor.set(ASSIGN_2_DRIVE_2_SPEED);
                }

            break;

        case WAIT_3:

            if (assignment2Timer.get() > ASSIGN_2_WAIT_3_TIME)
                {
                assignment2State = Assignment2State.DRIVE_3;
                System.out.println("Entering DRIVE_3");
                assignment2Timer.reset();
                assignment2Timer.start();
                }

            break;

        case DRIVE_3:

            if (assignment2Timer.get() > ASSIGN_2_DRIVE_3_TIME)
                {
                Hardware.testboardMotor.set(0.0);
                assignment2State = Assignment2State.WAIT_4;
                System.out.println("Entering WAIT_4");
                assignment2Timer.reset();
                assignment2Timer.start();
                }
            else
                {
                Hardware.testboardMotor.set(ASSIGN_2_DRIVE_3_SPEED);
                }

            break;

        case WAIT_4:

            if (assignment2Timer.get() > ASSIGN_2_WAIT_4_TIME)
                {
                assignment2State = Assignment2State.FINISHED;
                System.out.println("Entering FINISHED");
                assignment2Timer.reset();
                assignment2Timer.start();
                }

            break;

        case FINISHED:
            System.out.println("FINISHED");
            break;

        default:
            System.out.println(
                    "Reached default case in Assignment 2 Auto");
            assignment2State = Assignment2State.FINISHED;
            break;
        }
}


public static void periodic ()
{
    assignment2();
}

// ---------------------------------
// Methods
// ---------------------------------



}
