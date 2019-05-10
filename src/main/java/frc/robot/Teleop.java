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

    double deadband = 0.2;

    if (Math.abs(Hardware.rightOperator.getY()) > deadband)
        {
        double scaledValue = (Math
                .abs(Hardware.rightOperator.getY()) - deadband) * 1.25;

        if (Hardware.rightOperator.getY() < 0)
            scaledValue *= -1;

        Hardware.testboardMotor.set(scaledValue);
        }
    else
        Hardware.testboardMotor.set(0.0);

    System.out.println("Joystick: " + Hardware.rightOperator.getY());
    System.out.println("Motor: " + Hardware.testboardMotor.get());

    // Assignment 2: Write a class that handles a motor, moves it and
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



} // end Periodic()


// Individual testing methods for each programmer. Each programmer should //put
// their testing code inside their own method.
// Author: Guido Visioni



} // end class
