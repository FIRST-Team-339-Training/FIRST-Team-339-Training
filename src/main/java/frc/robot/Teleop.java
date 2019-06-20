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
    // Write a joystick program, that scales and with a deadband of .2
    // System.out.println("pot" + Hardware.thing.get());
    SmartDashboard.putNumber("pot", Hardware.thing.get() * 270);
    if (Hardware.testJoystick.getRawButton(10))
        {
        Hardware.testMotor.set(.5);
        }
    else
        {
        Hardware.testMotor.set(0);

        }
    // Scale the joystick such that at the deadband
    // the motor speed is zero and it still reaches its maximum speed
    /*
     * if ((Hardware.testJoystick.getY() <= -.2)
     * || (Hardware.testJoystick.getY() >= .2))
     * {
     * joystickScalar = 1.25
     * (Hardware.testJoystick.getY()) - .25;
     * System.out.println("Joystick is outputting: "
     * + Hardware.testJoystick.getY());
     * Hardware.testMotor.set(joystickScalar);
     * System.out.println("Motor speed is: " + joystickScalar);
     *
     * }
     * else
     * {
     * Hardware.testMotor.set(0.0);
     * }
     * // TrainingB branch test
     * // TrainingC branch test
     */
} // end Periodic()


// Individual testing methods for each programmer. Each programmer should //put
// their testing code inside their own method.
// Author: Guido Visioni

private static double joystickScalar;

} // end class
