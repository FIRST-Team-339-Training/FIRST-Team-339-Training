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
// written. All of these functions are functions that should
// override methods in the base class (IterativeRobot). The
// functions are as follows:
// -----------------------------------------------------
// Init() - Initialization code for teleop mode
// should go here. Will be called each time the robot enters
// teleop mode.
// -----------------------------------------------------
// Periodic() - Periodic code for teleop mode should
// go here. Will be called periodically at a regular rate while
// the robot is in teleop mode.
// -----------------------------------------------------
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================
package frc.robot;

import frc.Hardware.Hardware;
import edu.wpi.first.wpilibj.Relay.Value;
// import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Axis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.vision.VisionProcessor.ImageType;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * This class contains all of the user code for the Autonomous part of the
 * match, namely, the Init and Periodic code
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */
public class Teleop
{

/**
 * User Initialization code for teleop mode should go here. Will be called once
 * when the robot enters teleop mode.
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */
public static void init ()
{


} // end Init



/**
 * User Periodic code for teleop mode should go here. Will be called
 * periodically at a regular rate while the robot is in teleop mode.
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */

public static void periodic ()
{
    // Assignment 1: Move motor based on joystick, with deadband and scaling

    assignment1();

    // Assignment 2: Write an autonomous that:
    // when it starts, first wait for 1 second, then motor goes forward
    // .5 speed for two seconds, then it stops for .5 seconds, go backwards
    // at full speed for five seconds, then stop for .75 seconds, forward
    // again for .25 seconds at .6 speed. then stop, wait for 1 second,
    // then end auto. When waiting, print one statement
    // saying "We are waiting" (or some other message) then when
    // moving print once "We are moving" (or some other message)


    // The code for a working version of assignment 2 is in autonomous


    // Assignment 3 overview
    // : Write a class that handles a motor, moves it and
    // scales it based on final fields in that class, moves it for time
    // if button is pressed, but does not move if IR is triggered
    // an encoder has gone beyond min/ max. Limits max speed is a
    // switch is flipped
    // Maybe add a move to set position as well
    // With overrides
    // Break this into steps for training
    // With proper constructors and stuff

    // Will need: motor, joystick, IR (can be replaced with a switch?),
    // switch, encoder

    // This assignment should be broken into multiple parts, seen below




    // Assignment 4:
    // Do the Java Codeacademy

} // end Periodic()


public static void assignment1 ()
{
    double joyValue = Hardware.rightOperator.getY();

    double deadband = 0.2;

    if (Math.abs(joyValue) > deadband)
        {
        double scaledValue = (Math
                .abs(joyValue) - deadband) * 1.25;

        if (joyValue < 0)
            scaledValue *= -1;

        Hardware.testboardMotor.set(scaledValue);
        }
    else
        Hardware.testboardMotor.set(0.0);
}


// Individual testing methods for each programmer. Each programmer should //put
// their testing code inside their own method.
// Author: Guido Visioni



} // end class
